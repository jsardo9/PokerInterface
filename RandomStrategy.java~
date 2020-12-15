public class RandomStrategy implements Strategy 
{ 
  public void deal(int seat, int handCategory, Card[] hand, TableView tableView)
  {
  }
  
  
// called when it's this player's turn during betting 
// returns FOLD, CALL, or RAISE 
  public int act() 
  { 
    int randomAct = (int)(Math.random() * 3); 
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
