public class Card
  
{
  public static final int SPADES = 0;
  public static final int CLUBS = 1;
  public static final int HEARTS = 2;
  public static final int DIAMONDS = 3;
  
  public static final int JACK = 11;
  public static final int QUEEN = 12;
  public static final int KING = 13;
  public static final int ACE = 14;
  
  private int rank;
  private int suit;
  
  public Card(int rank, int suit)
  {
    this.rank = rank;
    this.suit = suit;
  }
    
    
    
    public int getRank()
  {
    return rank;
  }
  
  
  
  public int getSuit()
  {
    return suit;
  }
  

  
  
  public String toString()  // returns rank + "of" + suit
  {
    String suit = "";
    //SPADES
    if(getSuit() == 0)
    {
      suit = "s";
    }
    
    //CLUBS
    else if(getSuit() == 1)
    {
      suit = "c";
    }
    
    //HEARTS
    else if(getSuit() == 2)
    {
      suit = "h";
      
    }
    
    //DIAMONDS
    else if(getSuit() == 3)
    {
      suit = "d";
      
    }
    String rank;
    //JACK
    if(getRank() == 11)
    {
      rank = "J";
    }
    
    //QUEEN
    else if(getRank() == 12)
    {
      rank = "Q";
    }
    
    //KING
    else if(getRank() == 13)
    {
      rank = "K";
      
    }
    
    //ACE
    else if(getRank() == 14)
    {
      rank = "A";
      
    }
    
    //TEN
    else if(getRank() == 10)
    {
      rank = "T";
    }
    
    //REGULAR CARDS
    else 
    {
      rank = "" + getRank();
    }
    
    return rank + "" + suit;
  }
}