import java.util.*;
//  rules (to be fixed):
//    1. Dont raise more chips than you have in your stack
//    2. If you are all in and the action comes to you, bet 0

public class Main {
  public static void main(String[] args) {

    if (true) {
      Table t = new Table(5, 0, 2); // # people, dealer pos, bigBlind

      HumanStrategy human = new HumanStrategy();
      t.setPlayer(0, new Player(human, 1000));
      for (int seat = 1; seat < 5; seat++) {
        t.setPlayer(seat, new Player(new SardoStrategy1(), 1000));// <-----NUMBER OF CHIPS
      }

      TextDisplay d = new TextDisplay(t, true);
      t.setDisplay(d);
      human.setDisplay(d);
      for (int i = 0; i < 25; i++) {
        t.round();
      }

    }
  }
}