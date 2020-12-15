public class HandEvaluator
{


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
   return 7;
  }
  else if(hand[1].getRank() == hand[4].getRank())
  {
     Card savedCard = hand[0];
      hand[0] = hand[3];
      hand[3] = savedCard;
      return 7;
     
  }
  //moves 3 of a kind out front
  else if (hand[0].getRank() == hand[2].getRank())
  {
    if (hand[3].getRank() == hand[4].getRank())
    {
      return 6;
    }
   return 3;
  }
    else if (hand[1].getRank() == hand[3].getRank())
  {
     
    Card savedCard = hand[0];
      hand[0] = hand[3];
      hand[3] = savedCard;
      return 3;
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
      return 6;
    }
      return 3;
  }
    
   //moves pairs in front
  else
  {
int numPairedCards = 0;
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
      hand[3] = firstSavedCard;
      hand[4] = secondSavedCard;
       numPairedCards = numPairedCards + 2;
      }
      
     
      
      
      if (numPairedCards == 2)
      {
        return 1;
      }
      
      if (numPairedCards == 4)
      {
        return 2;
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
       if (hand[4].getRank() - hand[0].getRank() == 4)
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
         return 8;
        
       if (isFlush)
         return 5;
       
        if (isStraight)
         return 4;
      
      if (numPairedCards == 0)
      {
        return 0;
      }
    
  }
  return 0;
}


public static void printHand(Card[] hand)
{
  for (int i = 0; i < hand.length; i++)
  {
    System.out.println("rank:" + hand[i].getRank() + " suit:" + hand[i].getSuit());
    
  }
}





}