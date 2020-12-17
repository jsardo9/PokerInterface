public class HumanStrategy implements Strategy {
  private Card[] hHand;
  private int hSeat;
  private int handCat;
  private Table table;
  private TextDisplay disp;

  public HumanStrategy() {
  }

  public void setDisplay(TextDisplay d) {
    disp = d;
  }

  public void deal(int seat, Table table) {
    hHand = table.getPlayer(seat).getHand();
    hSeat = seat;
    this.table = table;
  }

  public int act() {
    return disp.act(hHand);
  }

  public void roundEnded() {
  }
}