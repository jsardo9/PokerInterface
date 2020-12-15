public class SardoStrategy1 implements Strategy
{
  private Card[] myHand;
  private int mySeat;
  private int handCat;
  private TableView table;
  private boolean exchanged;
    private int preHandStack;
      private int mltAnte;
  private int mltOppons;
  private int roundsWon;

  public SardoStrategy1()
  {
    mltAnte = 0;
    mltOppons = 0;
    roundsWon = 0;
  } 

  public void deal(int seat, int handCategory, Card[] hand, TableView tableView)
  {
    exchanged = false;
    myHand=hand;
    mySeat=seat;
    handCat=handCategory;
    table=tableView;
     preHandStack = table.getStack(mySeat) + 1;
  }
  public int act()
  {
    if (!exchanged)
    {
    if (handCat > 1)
      return 2;
      
    if (handCat > 0 && myHand[1].getRank() > 9)
      return 2;
    }
    else
    {
      if (handCat == 0)
      return 0;
      
      if (handCat == 1 && myHand[1].getRank() < 10)
        return 0;
      
      if (handCat == 1)
      {
        return 1;
      }
      
      return 2;
    }
      
    
    
    return 1;
  }
  public boolean[] exchange()
  {
    exchanged = true;
    if (handCat == 0)
    return new boolean[]{false, true, true, true, true};
    
    if (handCat == 1)
    return new boolean[]{false, false, true, true, true};
    
     if (handCat == 2)
    return new boolean[]{false, false, false, false, true};
     
      if (handCat == 3)
    return new boolean[]{false, false, false, true, true};
      
     
    return new boolean[]{false, false, false, false, false};
    
  }
  
  public void exchanged(int handCategory, Card[] hand)
  {
    myHand=hand;
    handCat=handCategory;
  }
  public void roundEnded()
  {
    int roundWinnings = table.getStack(mySeat) - preHandStack;
    if (roundWinnings < 0)
    {
      
      mltAnte = mltAnte + 1;
      mltOppons = (mltOppons - roundWinnings) - 1;
     
    }
    else
    {
      roundsWon++;
    }
    exchanged = false; 
  }
  
    public int getMltAnte()
  {
   return mltAnte;
  }
  
  public int getMltOppons()
  {
   return mltOppons;
  }
  
  public int getRoundsWon()
  {
   return roundsWon;
  }
}