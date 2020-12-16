public class PokerUtil {
  private static final String[] handCats = { "High Card", "One Pair", "Two Pair", "Three of a Kind", "Straight",
      "Flush", "Full House", "Four of a Kind", "Straight Flush" };

  public static String handToString(Card[] hand) {
    String str = "";
    for (Card c : hand) {
      if (c != null)
        str += c.toString() + " ";
      else
        str += "- ";
    }
    return str;
  }

  public static String categoryToString(int handCategory) {
    return handCats[handCategory];
  }

  public static int evaluateHand(Card[] hand, Player p) // takes in 7 cards (side effect is hand shortening to 5)
  {
    // sorts from highest rank to lowest rank ignoring pairs
    for (int i = 1; i < hand.length; i++) {
      for (int j = i; j > 0; j--)
        if (hand[j].getRank() > hand[j - 1].getRank()) {
          Card savedCard = hand[j];
          hand[j] = hand[j - 1];
          hand[j - 1] = savedCard;
        }
    }

    // counts number of cards in a row (of largest rank)
    int largestRow = 1;
    int indexOfLargestRow = 0;
    int indexOfSecondLargestRow = -1; // or same length but lower rank (minimum 2)
    int inaRow = 1;
    for (int i = 1; i < 7; i++) {
      if (hand[i].getRank() == hand[i - 1].getRank()) {
        inaRow++;
      } else {
        if (inaRow > largestRow) {
          if (largestRow > 1 && i - inaRow != indexOfLargestRow) // not currontly on largest row
          {
            // System.out.println("should not get here");
            if (indexOfSecondLargestRow == -1)
              indexOfSecondLargestRow = indexOfLargestRow;
          }
          largestRow = inaRow;
          indexOfLargestRow = i - inaRow; // redundant
          inaRow = 1;
        }
        if (inaRow > 1 && inaRow <= largestRow && largestRow > 1) // cannot be on curent largest row
        {
          // System.out.println("should not get here");
          if (indexOfSecondLargestRow == -1)
            indexOfSecondLargestRow = (i) - inaRow;
        }
        inaRow = 1;
      }
    }
    if (inaRow > 1 && inaRow <= largestRow && largestRow > 1) {
      if (indexOfSecondLargestRow == -1)
        indexOfSecondLargestRow = 7 - inaRow;
    }
    if (inaRow > largestRow) {
      largestRow = inaRow;
      indexOfLargestRow = (7) - inaRow;
    }
    // System.out.println("largest row " + largestRow);
    // System.out.println("indext largest row " + indexOfLargestRow);
    // System.out.println("index 2nd largest row " + indexOfSecondLargestRow);

    // Four of a Kind Case
    if (largestRow == 4) {
      Card[] newHand = new Card[5];
      for (int i = 0; i < 4; i++) {
        newHand[i] = hand[indexOfLargestRow + i];
      }
      if (indexOfLargestRow != 0) {
        newHand[4] = hand[0];
      } else {
        newHand[4] = hand[4];
      }
      p.setShowDownHand(newHand);
      System.out.println(handToString(newHand));
      System.out.println("FourOfAKind");
      return Strategy.FOUR_OF_A_KIND;
    }

    // Full House Case
    if (largestRow == 3 && indexOfSecondLargestRow != -1) {
      Card[] newHand = new Card[5];
      for (int i = 0; i < 3; i++) {
        newHand[i] = hand[indexOfLargestRow + i];
      }
      for (int i = 0; i < 2; i++) {
        newHand[i + 3] = hand[indexOfSecondLargestRow + i];
      }
      p.setShowDownHand(newHand);
      System.out.println(handToString(newHand));
      System.out.println("FullHouse");
      return Strategy.FULL_HOUSE;
    }

    int flushSuit = -1; // -1 means no flush
    int numSuits[] = new int[4];
    for (int i = 0; i < hand.length; i++) {
      numSuits[hand[i].getSuit()]++; // counts number of each suit
    }
    if (numSuits[0] > 4)
      flushSuit = 0;
    if (numSuits[1] > 4)
      flushSuit = 1;
    if (numSuits[2] > 4)
      flushSuit = 2;
    if (numSuits[3] > 4)
      flushSuit = 3;

    int numInOrder = 1; // checks for straight
    int indexOfStraight = -1; // index of highest card in straight
    boolean lowAce = false;
    for (int i = 1; i < 7; i++) {
      if (hand[i].getRank() - hand[i - 1].getRank() == -1) {
        numInOrder++;
        if (numInOrder == 5) {
          indexOfStraight = i - 4;
          break;
        }
      } else {
        numInOrder = 0;
      }
    }
    numInOrder = 2; // Ace Case
    if (indexOfStraight == -1 && hand[0].getRank() == 14 && hand[6].getRank() == 2) {
      for (int i = 5; i > 0; i--) {
        if (hand[i].getRank() - hand[i + 1].getRank() == 1) {
          numInOrder++;
          if (numInOrder > 4) {
            lowAce = true;
            indexOfStraight = i;
          }
        } else {
          break;
        }
      }
    }

    // Straight Flush Case
    if (flushSuit != -1 && indexOfStraight != -1) {
      boolean good = false;
      for (int i = 0; i < 5; i++) {
        if (hand[indexOfStraight + i].getSuit() == flushSuit) {
          if (i == 4)
            good = true;
        } else {
          break;
        }
      }
      if (good) {
        Card[] newHand = new Card[5];
        for (int i = 0; i < 5; i++) {
          newHand[i] = hand[indexOfStraight + i];
        }
        p.setShowDownHand(newHand);
        System.out.println(handToString(newHand));
        System.out.println("StraightFlush");
        return Strategy.STRAIGHT_FLUSH;
      }
    }

    // Flush Case
    if (flushSuit != -1) {
      Card[] newHand = new Card[5];
      int j = 0;
      for (int i = 0; i < 7 && j < 5; i++) {
        if (hand[i].getSuit() == flushSuit) {
          newHand[j] = hand[i];
          j++;
        }
      }
      p.setShowDownHand(newHand);
      System.out.println(handToString(newHand));
      System.out.println("Flush");
      return Strategy.FLUSH;
    }

    // Straight Case
    if (indexOfStraight != -1) {
      Card[] newHand = new Card[5];
      if (lowAce) {
        for (int i = 0; i < 4; i++) {
          newHand[i] = hand[indexOfStraight + i];
        }
        newHand[4] = hand[0];
      } else {
        for (int i = 0; i < 5; i++) {
          newHand[i] = hand[indexOfStraight + i];
        }
      }
      p.setShowDownHand(newHand);
      System.out.println(handToString(newHand));
      System.out.println("Straight");
      return Strategy.STRAIGHT;
    }

    // 3 of a Kind Case
    if (largestRow == 3) // moves 3 of a kind out front (of highest rank if 2 three of kinds present)
    {
      Card[] newHand = new Card[5];
      for (int i = 0; i < 3; i++) {
        newHand[i] = hand[indexOfLargestRow + i];
      }
      if (indexOfLargestRow > 1) {
        newHand[3] = hand[0];
        newHand[4] = hand[1];
        p.setShowDownHand(newHand);
        System.out.println(handToString(newHand));
        System.out.println("threeOfAKind");
        return Strategy.THREE_OF_A_KIND;
      } else if (indexOfLargestRow == 1) {
        newHand[3] = hand[0];
        newHand[4] = hand[4];
        p.setShowDownHand(newHand);
        System.out.println(handToString(newHand));
        System.out.println("threeOfAKind");
        return Strategy.THREE_OF_A_KIND;
      } else // index of largest row is 0
      {
        newHand[3] = hand[3];
        newHand[4] = hand[4];
        p.setShowDownHand(newHand);
        System.out.println(handToString(newHand));
        System.out.println("threeOfAKind");
        return Strategy.THREE_OF_A_KIND;
      }
    }

    // 2 Pair Case
    if (largestRow == 2 && indexOfSecondLargestRow != -1) {
      Card[] newHand = new Card[5];
      for (int i = 0; i < 2; i++) {
        newHand[i] = hand[indexOfLargestRow + i];
      }
      for (int i = 2; i < 4; i++) {
        newHand[i] = hand[indexOfSecondLargestRow + (i - 2)];
      }
      if (indexOfLargestRow != 0) {
        newHand[4] = hand[0];
        p.setShowDownHand(newHand);
        System.out.println(handToString(newHand));
        System.out.println("twoPair");
        return Strategy.TWO_PAIR;
      } else if (indexOfSecondLargestRow != 2) {
        newHand[4] = hand[2];
        p.setShowDownHand(newHand);
        System.out.println(handToString(newHand));
        System.out.println("twoPair");
        return Strategy.TWO_PAIR;
      } else {
        newHand[4] = hand[4];
        p.setShowDownHand(newHand);
        System.out.println(handToString(newHand));
        System.out.println("twoPair");
        return Strategy.TWO_PAIR;
      }
    }

    // Pair Case
    if (largestRow == 2) {
      Card[] newHand = new Card[5];
      for (int i = 0; i < 2; i++) {
        newHand[i] = hand[indexOfLargestRow + i];
      }
      if (indexOfLargestRow == 0) {
        newHand[2] = hand[2];
        newHand[3] = hand[3];
        newHand[4] = hand[4];
        p.setShowDownHand(newHand);
        System.out.println(handToString(newHand));
        System.out.println("pair");
        return Strategy.PAIR;
      } else if (indexOfLargestRow == 1) {
        newHand[2] = hand[0];
        newHand[3] = hand[3];
        newHand[4] = hand[4];
        p.setShowDownHand(newHand);
        System.out.println(handToString(newHand));
        System.out.println("pair");
        return Strategy.PAIR;
      } else if (indexOfLargestRow == 2) {
        newHand[2] = hand[0];
        newHand[3] = hand[1];
        newHand[4] = hand[4];
        p.setShowDownHand(newHand);
        System.out.println(handToString(newHand));
        System.out.println("pair");
        return Strategy.PAIR;
      } else {
        newHand[2] = hand[0];
        newHand[3] = hand[1];
        newHand[4] = hand[2];
        p.setShowDownHand(newHand);
        System.out.println(handToString(newHand));
        System.out.println("pair");
        return Strategy.PAIR;
      }
    }

    // HighCard
    Card[] newHand = new Card[5];
    for (int i = 0; i < 5; i++) {
      newHand[i] = hand[i];
    }
    p.setShowDownHand(newHand);
    System.out.println(handToString(newHand));
    System.out.println("high card");
    return Strategy.HIGH_CARD;
  }

  // returns 1 if hand1 wins, 2 if hand2 wins, 0 if tie
  public static int getWinner(int category1, Card[] hand1, int category2, Card[] hand2) {
    if (category1 > category2)
      return 1;
    if (category2 > category1)
      return 2;
    for (int i = 0; i < 5; i++) {
      if (hand1[i].getRank() > hand2[i].getRank())
        return 1;
      if (hand2[i].getRank() > hand1[i].getRank())
        return 2;
    }
    return 0;
  }
}