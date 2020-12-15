public class PokerUtil
{
  private static final String[] handCats={"High Card","One Pair","Two Pair","Three of a Kind","Straight",
    "Flush","Full House","Four of a Kind", "Straight Flush"};
  public static String handToString(Card[] hand)
  {
    String str="";
    for(Card c:hand)
      str+=c.toString()+" ";
    return str;
  }
  
  public static String categoryToString(int handCategory)
  {
    return handCats[handCategory];
  }
  
  public static int evaluateHand(Card[] hand)
  {
    //sorts from highest rank to lowest rank ignoring pairs
    for (int i = 1; i < hand.length; i++)
    {
      for (int j = i; j > 0; j--)
        if (hand[j].getRank() > hand[j - 1].getRank())
      {
        Card savedCard = hand[j];
        hand[j] = hand[j - 1];
        hand[j - 1] = savedCard;
      }
    }
    
    //places 4 of a kinds in front
    if (hand[0].getRank() == hand[3].getRank())
    {
      return Strategy.FOUR_OF_A_KIND;
    }
    else if(hand[1].getRank() == hand[4].getRank())
    {
      Card savedCard = hand[0];
      hand[0] = hand[4];
      hand[4] = savedCard;
      return Strategy.FOUR_OF_A_KIND;
      
    }
    //moves 3 of a kind out front
    else if (hand[0].getRank() == hand[2].getRank())
    {
      if (hand[3].getRank() == hand[4].getRank())
      {
        return Strategy.FULL_HOUSE;
      }
      return Strategy.THREE_OF_A_KIND;
    }
    else if (hand[1].getRank() == hand[3].getRank())
    {
      
      Card savedCard = hand[0];
      hand[0] = hand[3];
      hand[3] = savedCard;
      return Strategy.THREE_OF_A_KIND;
    }
    else if (hand[2].getRank() == hand[4].getRank())
    {
      
      Card firstSavedCard = hand[0];
      Card secondSavedCard = hand[1];
      hand[0] = hand[4];
      hand[1] = hand[3];
      hand[4] = secondSavedCard;
      hand[3] = firstSavedCard;  
      
      if (hand[3].getRank() == hand[4].getRank())
      {
        return Strategy.FULL_HOUSE;
      }
      return Strategy.THREE_OF_A_KIND;
    }
    
    //moves pairs in front
    else
    {
      int numPairedCards = 0;
      
      if (hand[1].getRank() == hand[0].getRank())
        numPairedCards = 2;
      
      if (hand[1].getRank() == hand[2].getRank())
      {
        Card savedCard = hand[0];
        hand[0] = hand[2];
        hand[2] = savedCard;
        numPairedCards = numPairedCards + 2;
        
      }
      
      if (hand[2].getRank() == hand[3].getRank())
      {
        Card firstSavedCard = hand[numPairedCards];
        Card secondSavedCard = hand[numPairedCards + 1];
        
        hand[numPairedCards] = hand[2];
        hand[numPairedCards + 1] = hand[3];
        hand[2] = firstSavedCard;
        hand[3] = secondSavedCard;
        numPairedCards = numPairedCards + 2;
      }
      
      if (hand[3].getRank() == hand[4].getRank())
      {
        Card firstSavedCard = hand[numPairedCards];
        Card secondSavedCard = hand[numPairedCards + 1];
        
        hand[numPairedCards] = hand[3];
        hand[numPairedCards + 1] = hand[4];
        
        if(numPairedCards == 2)
          hand[4] = firstSavedCard;
        else
        {
          Card thirdSavedCard = hand[numPairedCards + 2]; 
          hand[2] = firstSavedCard;
          hand[3] = secondSavedCard;
          hand[4] = thirdSavedCard;
        }
        
        numPairedCards = numPairedCards + 2;
      }
      
      if (numPairedCards == 2)
      {
        return Strategy.PAIR;
      }
      
      if (numPairedCards == 4)
      {
        return Strategy.TWO_PAIR;
      }
      
      boolean isFlush = true;
      int firstSuit = hand[0].getSuit();
      for(int i = 1; i < hand.length; i++)
      {
        if (hand[i].getSuit() != firstSuit)
        {
          isFlush = false;
        }
      }
      
      boolean isStraight = false;
      if (hand[0].getRank() - hand[4].getRank() == 4)
      {
        isStraight = true;
      }
      
      
      
      if (hand[0].getRank() == 14)
      {
        if (hand[1].getRank() - 1 == 4)
        {
          Card savedCard = hand[0];
          hand[0] = hand[1];
          hand[1] = hand[2];
          hand[2] = hand[3];
          hand[3] = hand[4];
          hand[4] = savedCard;
          isStraight = true;
        }
      }
      
      
      if (isFlush && isStraight)
        return Strategy.STRAIGHT_FLUSH;
      
      if (isFlush)
        return Strategy.FLUSH;
      
      if (isStraight)
        return Strategy.STRAIGHT;
      
      if (numPairedCards == 0)
      {
        return Strategy.HIGH_CARD;
      }
      
    }
    throw new RuntimeException("should never get here");
  }
  
  //returns 1 if hand1 wins, 2 if hand2 wins, 3 if tie
  public static int getWinner(int category1, Card[] hand1, int category2, Card[] hand2)
  {
    if (category1 > category2)
      return 1;
    if (category2 > category1)
      return 2;
    for (int i = 0; i < hand1.length; i++)
    {
      if (hand1[i].getRank() > hand2[i].getRank())
        return 1;
      if (hand2[i].getRank() > hand1[i].getRank())
        return 2;
    }
    return 0;
  }
}