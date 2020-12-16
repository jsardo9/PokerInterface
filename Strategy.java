public interface Strategy {
  public static final int FOLD = -1;
  public static final int HIGH_CARD = 0;
  public static final int PAIR = 1;
  public static final int TWO_PAIR = 2;
  public static final int THREE_OF_A_KIND = 3;
  public static final int STRAIGHT = 4;
  public static final int FLUSH = 5;
  public static final int FULL_HOUSE = 6;
  public static final int FOUR_OF_A_KIND = 7;
  public static final int STRAIGHT_FLUSH = 8;

  void deal(int seat, int handCategory, Card[] hand, Table t);

  int act();

  void roundEnded();
}