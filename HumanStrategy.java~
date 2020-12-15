public class HumanStrategy implements Strategy
{
  private Card[] hHand;
  private int hSeat;
  private int handCat;
  private Table table;
  private TextDisplay disp;
  public HumanStrategy()
  {
  }
  public void setDisplay(TextDisplay d)
  {
    disp=d;
  }
  public void deal(int seat, Card[] hand, Table table)
  {
    hHand=hand;
    hSeat=seat;
    this.table=table;
  }
  public int act()
  {
    return -1;
    //return disp.act(hHand);
  }
 
  public void roundEnded()
  {
  }
}