public class RandomStrategy implements Strategy 
{ 
  public Table t;
  public int seat;
  
  public void deal(int seat, Card[] hand, Table table)
  {
    t = table;
    this.seat = seat;
  }
  
  
// called when it's this player's turn during betting 
// returns FOLD, CALL, or RAISE 
  public int act() 
  { 
    int currentBet = t.getPlayer(seat).getBet();
    int randomAct = (int)(Math.random() * 6); 
   // if (randomAct == 0 && t.getCall() - currentBet > 0)
   //  return Strategy.FOLD;
    if (randomAct + currentBet <= t.getCall())
    {
    //System.out.println(randomAct);
    return (t.getCall() - currentBet); //- (t.getCall() - currentBet)%t.getBigBlind(); 
    }
    else
    return randomAct;
  } 
  
  
// returns which cards to exchange; 
// for example, {true, false, true, false, false} means discard cards at index 0 and 2 
  public boolean[] exchange() 
  { 
    boolean[] exchange = new boolean[5]; 
    for (int i=0; i<5; i++) 
    { 
      int random = (int)(Math.random() * 2); 
      if (random==0) 
        exchange[i] = true; 
      else 
        exchange[i] = false; 
    } 
    return exchange; 
  } 
  
  public void exchanged(int handCategory, Card[] hand)
  {
  }
  
  public void roundEnded()
  {
  }
} 
