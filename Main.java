import java.util.*;
//  rules:
//    1. Dont raise more chips than you have in your stack
//    2. If you are all in and the action comes to you, bet 0

public class Main {
  public static void main(String[] args) {
    // int test2 = PokerUtil.evaluateHand(new Card[]{new Card(9,0),new
    // Card(11,0),new Card(13,0),
    // new Card(9,1),new Card(2,2),new Card(5,3),new Card(14,3)});
    // //System.out.println(test2);
    //
    // int test = PokerUtil.evaluateHand(new Card[]{new Card(9,0),new Card(11,0),new
    // Card(13,0),
    // new Card(9,1),new Card(2,2),new Card(8,1),new Card(2,1)});
    // // System.out.println(test);

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
      // for (int i = 0; i < 3; i++)
      t.round();
    }
  }
}