import java.util.*;

public class MainBots {
  public static void main(String[] args) {
    int numPlayers = 5;
    int numGames = 20000;

    Table t = new Table(numPlayers, 0, 2);

    SardoStrategy1 myBot = new SardoStrategy1();
    t.setPlayer(0, new Player(myBot, 1000));
    t.setPlayer(1, new Player(new SardoStrategy1(), 1000));
    t.setPlayer(2, new Player(new SardoStrategy1(), 1000));
    t.setPlayer(3, new Player(new SardoStrategy1(), 1000));
    t.setPlayer(4, new Player(new SardoStrategy1(), 1000));

    // for (int seat = 1; seat < numPlayers; seat++)
    // t.setPlayer(seat, new Player(new SardoStrategy1()));

    for (int i = 0; i < numGames; i++) {

      if (i % (numGames * .01) == 0 && testMode) {
        System.out.println(i);
      }
      t.round();
      // System.out.println("end of round");
    }
    for (int i = 0; i < numPlayers; i++) {
      System.out.println("Seat " + i + " Stack: " + t.getPlayer(i).getStack());
    }

  }
}