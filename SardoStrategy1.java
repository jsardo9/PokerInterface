import static java.lang.Math.*;

public class SardoStrategy1 implements Strategy {
  private Card[] myHand;
  private int mySeat;
  private int handCat;
  private Table table;

  // private int round; maybe track current betting round

  private int mltAnte;
  private int mltOppons;
  private int roundsWon;
  private int preHandStack;

  public SardoStrategy1() {
    mltAnte = 0;
    mltOppons = 0;
    roundsWon = 0;
  }

  public void deal(int seat, Table t) {
    mySeat = seat;
    table = t;
    myHand = table.getPlayer(mySeat).getHand();
    preHandStack = table.getPlayer(mySeat).getStack();
  }

  public int act() {
    if (table.getPlayer(mySeat).getStack() > table.getCall()) {
      if (table.getPlayer(mySeat).getBet() != 0) {
        return table.getCall() - table.getPlayer(mySeat).getBet();
      } else {
        if (table.getCall() > 0 && Math.random() > .6) {
          return FOLD;
        } else if (Math.random() > .75) {
          return table.getCall();
        } else {
          return table.getCall() + (int) (Math.random() * 5);
        }
      }
    } else {
      return FOLD;
    }
  }

  public void roundEnded() {
    int roundWinnings = table.getPlayer(mySeat).getStack() - preHandStack;
    if (roundWinnings < 0) {

      mltAnte = mltAnte + 1;
      mltOppons = (mltOppons - roundWinnings) - 1;

    } else {
      roundsWon++;
    }
  }

  public int getMltAnte() {
    return mltAnte;
  }

  public int getMltOppons() {
    return mltOppons;
  }

  public int getRoundsWon() {
    return roundsWon;
  }
}