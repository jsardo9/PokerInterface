import java.util.*;

public class Player {
  private static Set<Integer> usedIDs = new HashSet<Integer>();

  private Card[] hand = null; // always stored in sorted order

  private Card[] showDownHand = null; // only learns this during showdown
  private int handCategory = -1; // only learns handcategory during showdown
  private Card[] finalHand = new Card[7];

  private int stack; // all the money the player has that is not currently in play
  private int bet = 0; // money pushed in by player during current betting round
  private Strategy strategy;
  private boolean folded = false;
  private int allIn = -1; // number of chips that can be won by all in from player (-1 if not all in)
  private int id; // has a unique ID, randomly assigned

  public Player(Strategy strat, int stack) {
    strategy = strat;
    this.stack = stack;

    if (usedIDs.size() >= 900)
      throw new RuntimeException("all ID values used");

    do
      id = (int) (Math.random() * 900 + 100);
    while (usedIDs.contains(id));

    usedIDs.add(id);
  }

  public int getBet() {
    return bet;
  }

  public void fold() {
    folded = true;
  }

  public void unFold() {
    folded = false;
  }

  public void setBet(int newBet) {
    bet = newBet;
  }

  public Card[] getHand() {
    return hand;
  }

  public int getStack() {
    return stack;
  }

  public boolean hasFolded() {
    return folded;
  }

  public int getAllIn() {
    return allIn;
  }

  public void setAllIn(int sidepot) {
    allIn = sidepot;
  }

  public int getHandCategory() {
    return handCategory;
  }

  public void setHandCategory(Card[] board) {
    for (int i = 0; i < 2; i++)
      finalHand[i] = hand[i];
    for (int i = 2; i < 7; i++)
      finalHand[i] = board[i - 2];
    handCategory = PokerUtil.evaluateHand(finalHand, this); // side affect changes showdown hand
  }

  // pre: hand and handCategory already up to date
  public void deal(int seat, Table table) {
    strategy.deal(seat, table);
  }

  public int act() {
    return strategy.act();
  }

  public void addChips(int chips) // adds to stack
  {
    stack = stack + chips;
  }

  public void removeChips(int chips) // removes from stack
  {
    stack = stack - chips;
  }

  public void setHand(Card[] newHand) {
    hand = newHand;
  }

  public void setShowDownHand(Card[] newHand) {
    showDownHand = newHand;
  }

  public Card[] getShowDownHand() {
    return showDownHand;
  }

  public int getID() {
    return id;
  }

  public String toString() // All information, including hand
  {
    return "id#" + id + "||  " + PokerUtil.handToString(hand) + " " + publicToString();
  }

  public String publicToString() {
    String s = String.format("(%3d)", stack);
    if (folded)
      s += " F";
    else {

      if (bet > 0)
        s += " bet=" + bet;
    }
    return s;
  }

  // should never use this player object again
  public void releaseID() {
    usedIDs.remove(id);
  }

  public void roundEnded() {
    strategy.roundEnded();
  }
}
