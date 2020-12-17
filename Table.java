import java.util.*;

public class Table {

  private Player[] players; // NEVER LEAVES AN EMPTY SPOT
  private int dealer; // 0 is yourself and rotates left through players[] ???
  private int activeSeat; // Current acting player
  private int pot = 0; // Numer of chips currently in the pot
  private int bigBlind; // Number of chips the big blind costs
  private Card[] deck = new Card[52];
  private int deckPlace = 0; // index of next card in deck to be dealt
  private int call = 0; // the maximum amount bet by anyone
  private Card[] board = new Card[5]; // Cards in the center
  private TextDisplay display;
  private boolean showDown; // true if we're in the showdown

  public Table(int numPlayers, int dealer, int bigBlind) {
    players = new Player[numPlayers];
    this.dealer = dealer;
    this.bigBlind = bigBlind;

    for (int i = 0; i < 5; i++) // creates board
    {
      board[i] = null;
    }

    int index = 0; // fills deck
    for (int suit = 0; suit < 4; suit++) {
      for (int rank = 2; rank < 15; rank++) {
        deck[index] = new Card(rank, suit);
        index++;
      }
    }
  }

  // Randomly shuffles deck
  public void shuffle() {
    for (int i = 0; i < 52; i++) {
      int index = (int) (Math.random() * (52 - i));
      Card placeHolder = deck[index];
      deck[index] = deck[51 - i];
      deck[51 - i] = placeHolder;
    }
  }

  public void setDisplay(TextDisplay display) {
    this.display = display;
  }

  public void round() {
    int tempBB = bigBlind;
    if (players.length < 2)
      throw new RuntimeException("not enough players to play:  " + players.length);

    activeSeat = (dealer + 1) % players.length; // active player is small blind

    System.out.println("Collecting Blinds");
    blinds();
    // activeSeat is now one after big blind since they start first round of betting
    System.out.println("Shuffling Deck");
    shuffle();
    System.out.println("Dealing Cards");
    deal(); // deal always starts from position 0
    System.out.println("Pre-Flop Betting");
    bet(true); // Preflop || not yet tested

    if (getNumActivePlayers() > 1) // Flop
    {
      System.out.println("Flop Betting");
      for (int i = 0; i < 3; i++) {
        board[i] = deck[deckPlace];
        deckPlace++;
      }
      bet(false);
    }

    if (getNumActivePlayers() > 1) // Turn
    {
      System.out.println("Turn Betting");
      board[3] = deck[deckPlace];
      deckPlace++;
      bet(false);
    }

    if (getNumActivePlayers() > 1) // River
    {
      System.out.println("River Betting");
      board[4] = deck[deckPlace];
      deckPlace++;
      bet(false);
    }

    System.out.println("ShowDown");
    showDown = true;
    if (display != null)
      display.update();
    showDown = false;

    ArrayList<Integer> winningSeats = new ArrayList<Integer>();
    for (int i = 0; i < players.length; i++) // creating winning seats
    {
      Player p = players[i];
      if (!p.hasFolded()) {
        for (int k = 0; k < 5; k++) {
          if (board[k] == null) {
            board[k] = new Card(1, 1);
          }
        }
        p.setHandCategory(board); // this sets showdownhand also
        if (winningSeats.size() == 0)
          winningSeats.add(i);
        else // winningSeats is at least 1
        {
          for (int j = winningSeats.size() - 1; j > -1; j--) {
            int cat1 = p.getHandCategory();
            int cat2 = players[winningSeats.get(j)].getHandCategory();
            int winner = PokerUtil.getWinner(cat1, p.getShowDownHand(), cat2,
                players[winningSeats.get(j)].getShowDownHand()); // returns1ifhand1won

            if (winner == 1) {
              if (p.getAllIn() == -1) {
                winningSeats = new ArrayList<Integer>();
                winningSeats.add(i);
                j = -1;
              } else if (players[winningSeats.get(j)].getAllIn() != -1
                  && p.getAllIn() > players[winningSeats.get(j)].getAllIn()) // better hand and mmore equity
              {
                winningSeats.remove(j);
                winningSeats.add(i);
              } else // best hand but lowest equity
              {
                winningSeats.add(j, i); // adding in before j
              }
            } else if (winner == 0) // tie
            {
              winningSeats.add(j, i);
            } else {
              j = -1;
            }
          }
        }
      }
    }
    System.out.println("Did winning seats");

    while (pot != 0) {
      int numTie = 1;
      int i = 0;
      while (i + 1 < winningSeats.size() && PokerUtil.getWinner(players[winningSeats.get(i)].getHandCategory(),
          players[winningSeats.get(i)].getShowDownHand(), players[winningSeats.get(i + 1)].getHandCategory(),
          players[winningSeats.get(i + 1)].getShowDownHand()) == 0) {
        numTie++;
        i++;
      }
      int reward = pot / numTie;
      while (i >= 0) {
        players[winningSeats.get(i)].addChips(reward);
        i--;
      }
      pot = 0;
    }

    System.out.println("Did winnings");

    if (display != null)
      display.update();

    // notify players that round has ended before resetting all for next round
    for (int i = 0; i < players.length; i++) {
      if (players[i] != null)
        players[i].roundEnded();
    }

    deckPlace = 0;
    for (int i = 0; i < 5; i++)
      board[i] = null;
    dealer = (dealer + 1) % players.length;

    for (Player p : players) {
      if (p.getStack() <= 50) {
        p.addChips(50);
      }
      p.unFold();
    }

    bigBlind = tempBB;
  }

  public void blinds() {
    int bbPlayer = (activeSeat + 1) % players.length;

    int sbStack = players[activeSeat].getStack();
    int bbStack = players[bbPlayer].getStack();
    if (players[activeSeat].getStack() >= bigBlind / 2) {
      players[activeSeat].removeChips(bigBlind / 2);
      players[activeSeat].setBet(bigBlind / 2);
    } else { // Case when small blind player cannot cover small blind
      players[activeSeat].setAllIn(players[activeSeat].getStack());
      players[activeSeat].removeChips(players[activeSeat].getStack());
      players[activeSeat].setBet(players[activeSeat].getStack());
    }

    if (players[bbPlayer].getStack() >= bigBlind) {
      players[bbPlayer].removeChips(bigBlind);
      players[bbPlayer].setBet(bigBlind);
      activeSeat = (activeSeat + 2) % players.length;
    } else { // Case when Big blind player cannot cover Big blind
      players[bbPlayer].setAllIn(players[bbPlayer].getStack());
      players[bbPlayer].removeChips(players[bbPlayer].getStack());
      players[bbPlayer].setBet(players[bbPlayer].getStack());
      if (bbStack > bigBlind / 2) // bb stack still covers small blind
      {
        bigBlind = bbStack;
      } else if (sbStack >= bigBlind / 2) // sb stack covers small blind
      {
        bigBlind = bigBlind / 2;
      } else // small blind does not cover either
      {
        if (sbStack > bbStack)
          bigBlind = sbStack; // set big blind to largest stack
        else
          bigBlind = bbStack;
      }
    }
  }

  public int getNumActivePlayers() {
    int left = 0;
    for (Player p : players) {
      if (!p.hasFolded() && p.getAllIn() == -1 && p.getStack() > 1)// not folded and not all in
        left++;
    }
    return left;
  }

  public void deal() {
    for (int seat = 0; seat < players.length; seat++) {

      Card[] hand = new Card[2];
      for (int i = 0; i < 2; i++) {
        hand[i] = deck[deckPlace];
        deckPlace++;
      }
      // int handCategory = PokerUtil.evaluateHand(hand);
      players[seat].setHand(hand);
      players[seat].deal(seat, this);

    }
  }

  public void bet(boolean preflop) {
    call = 0; // biggest bet is 0 so far
    int called = 0; // number of players who have called the biggest bet
    if (preflop)
      call = bigBlind;

    int playersLeft = getNumActivePlayers(); // number of nunfolded nonAllIn players
    while (called < playersLeft && playersLeft > 1) {
      Player activePlayer = players[activeSeat];
      if (!activePlayer.hasFolded() && activePlayer.getAllIn() == -1) // player is not yet all in
      {
        if (display != null)
          display.update();
        int option = activePlayer.act();
        if (option == Strategy.FOLD) // Player Folded
        {
          activePlayer.fold();
          playersLeft--;
        } else if (option > (call - activePlayer.getBet())) // Player Raised
        {
          called = 1;
          for (int i = 0; i < players.length; i++) // makes sure no one was all on a call prior to this raise
          {
            if (!players[i].hasFolded() && players[i].getStack() == 0 && players[i].getBet() == call && call > 0) {
              playersLeft--;
            }
          }
          if (called == playersLeft) // force player to call if raising into all in player(s)
          {
            // System.out.println("All in player getting raised");
            activePlayer.removeChips(call - activePlayer.getBet());
            activePlayer.setBet(call);
          } else {
            if (activePlayer.getStack() - option == 0) // all in
            {
              activePlayer.setAllIn(pot);
            }
            call = activePlayer.getBet() + option;
            activePlayer.removeChips(option);
            activePlayer.setBet(activePlayer.getBet() + option);
          }
        } else if (option == (call - activePlayer.getBet())) // Player Called
        {
          if (activePlayer.getStack() - option == 0) // all in
          {
            activePlayer.setAllIn(pot);
          }
          activePlayer.removeChips(call - activePlayer.getBet());
          activePlayer.setBet(call);
          called++;
        } else if (activePlayer.getStack() - option == 0) // Player is all in and couldnt match the call
        {
          activePlayer.setAllIn(pot);
          activePlayer.setBet(option + activePlayer.getBet());
          activePlayer.removeChips(call - activePlayer.getBet());
          playersLeft--;
        } else
          throw new RuntimeException("illegal action: probably bet less than the call");
      }
      activeSeat = (activeSeat + 1) % players.length;
    }

    if (display != null)
      display.update();

    activeSeat = (dealer + 1) % players.length;

    for (int i = 0; i < players.length; i++) {
      Player p = players[i];
      if (p.getBet() > 0 && p.getBet() < call) // player was all in and placed a bet this round
      {
        for (int j = 0; i < players.length; i++) {
          if (!players[j].hasFolded() && players[j].getBet() >= p.getBet())
            p.setAllIn(p.getAllIn() + p.getBet()); // if all in bet was covered
          else
            p.setAllIn(p.getAllIn() + players[j].getBet()); // all in bet was not covered
        }
      }
    }

    for (Player p : players) {
      pot += p.getBet();// collects all bets into pot
      p.setBet(0);
    }
  }

  public void setPlayer(int seat, Player player) {
    players[seat] = player;
  }

  public int getActiveSeat() {
    return activeSeat;
  }

  public int getPot() {
    return pot;
  }

  public Card[] getBoard() {
    return board;
  }

  public int getDealer() {
    return dealer;
  }

  public int getCall() {
    return call;
  }

  public int getBigBlind() {
    return bigBlind;
  }

  public int getSize() {
    return players.length;
  }

  public Player getPlayer(int seat) {
    return players[seat];
  }

  public String toString() {
    String s = "board: " + PokerUtil.handToString(board) + "\n";
    s += "pot:  " + pot + "\n";
    for (int seat = players.length - 1; seat >= 0; seat--)// cycles backwards through players
    {
      if (seat == dealer)
        s += " D";
      else if (seat == (dealer + 1) % players.length)
        s += "sb";
      else if (seat == (dealer + 2) % players.length)
        s += "bb";
      else
        s += "  ";
      if (seat == activeSeat)
        s += "*";
      else
        s += " ";
      s += seat + ":";
      s += players[seat];
      s += "\n";
    }
    return s;
  }

  public boolean showDown() {
    return showDown;
  }
}
