import java.util.Scanner;

public class TextDisplay {
  private static Scanner userInput = new Scanner(System.in);

  private Table table;
  private boolean hasHuman;

  public TextDisplay(Table t, boolean hasHum) {
    table = t;
    hasHuman = hasHum;
  }

  public void update() {
    if (hasHuman)
      System.out.println(table.toString(table.showDown())); // use this potentially in future --> ||
                                                            // table.getTableView()
  }

  public int act(Card[] hand) {
    System.out.println("Your hand: " + PokerUtil.handToString(hand, true));
    System.out.println("Input your action: (f)old, (b)et |amount|");
    String choice;
    do
      choice = userInput.nextLine().toLowerCase();
    while (choice.length() == 0 || !"fb".contains(choice.substring(0, 1)));
    if (choice.equals("f"))
      return -1;
    else // must've been a b
    {
      if (choice.length() < 3) {
        throw new RuntimeException("kill yourself");
      }
      return Integer.parseInt(choice.substring(2));
    }
    // return "fcr".indexOf(choice.substring(0, 1));
  }

  public boolean[] exchange(Card[] hand) {
    System.out.println("Your hand: " + PokerUtil.handToString(hand));
    System.out.println(
        "Which cards would you like to exchange? (0 = none, 2 = last 2 cards, 5 = all cards, x--x- = 1st and 4th)");
    while (true) {
      String choice = userInput.nextLine().toLowerCase();
      if (choice.equals("0"))
        return new boolean[] { false, false, false, false, false };
      if (choice.equals("1"))
        return new boolean[] { false, false, false, false, true };
      if (choice.equals("2"))
        return new boolean[] { false, false, false, true, true };
      if (choice.equals("3"))
        return new boolean[] { false, false, true, true, true };
      if (choice.equals("4"))
        return new boolean[] { false, true, true, true, true };
      if (choice.equals("5"))
        return new boolean[] { true, true, true, true, true };
      try {
        // assume choice is a string of 5 x's (exchange) or -'s (keep)
        boolean[] b = new boolean[5];
        for (int i = 0; i < 5; i++) {
          if (choice.charAt(i) == 'x')
            b[i] = true;
          else if (choice.charAt(i) == '-')
            b[i] = false;
          else
            continue; // try again
        }
        return b;
      } catch (Exception e) {
        // ignore and let the user try again
      }
    }
  }
}