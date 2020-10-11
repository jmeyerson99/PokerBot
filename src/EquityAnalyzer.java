import java.util.*;

public class EquityAnalyzer {

    private Table table;

    public EquityAnalyzer(Table table) {
        this.table = table;
    }

    public void determineEquity() {
        Board board = table.getBoard();
        switch (board.getSize()) {
            case 0:
                break;

            case 3:
                break;

            case 4:
                break;

            case 5:
                determineShowdownWinner();
                break;

            default:
                // ERROR (invalid size)
                break;
        }
    }

    private void determineShowdownWinner() {
        for (Player p : this.table.getPlayers()) {
            HandRanking bestHand = getBestHand(p.getHand(), this.table.getBoard(), p);
            System.out.println("Player " + p.getName() + " has " + bestHand);
        }
    }

    // should probably return the collection of the best 5 cards making the hand
    private HandRanking getBestHand(Hand hand, Board board, Player p) {
        ArrayList<Card> allCards = new ArrayList<>();
        allCards.add(hand.getCard1());
        allCards.add(hand.getCard2());
        allCards.addAll(board.getBoard());

        boolean royalFlush = checkRoyalFlush(allCards);
        if (royalFlush) {p.setHandRanking(HandRanking.ROYAL_FLUSH); return HandRanking.ROYAL_FLUSH;}
        boolean straightFlush = checkStraightFlush(allCards);
        if (straightFlush) {p.setHandRanking(HandRanking.STRAIGHT_FLUSH); return HandRanking.STRAIGHT_FLUSH;}
        boolean quads = checkQuads(allCards);
        if (quads) {p.setHandRanking(HandRanking.QUADS); return HandRanking.QUADS;}
        boolean fullHouse = checkFullHouse(allCards);
        if (fullHouse) {p.setHandRanking(HandRanking.FULL_HOUSE); return HandRanking.FULL_HOUSE;}
        boolean flush = checkFlush(allCards);
        if (flush) {p.setHandRanking(HandRanking.FLUSH); return HandRanking.FLUSH;}
        boolean straight = checkStraight(allCards);
        if (straight) {p.setHandRanking(HandRanking.STRAIGHT); return HandRanking.STRAIGHT;}
        boolean trips = checkTrips(allCards);
        if (trips) {p.setHandRanking(HandRanking.TRIPS); return HandRanking.TRIPS;}
        boolean twoPair = checkTwoPair(allCards);
        if (twoPair) {p.setHandRanking(HandRanking.TWO_PAIR); return HandRanking.TWO_PAIR;}
        boolean pair = checkPair(allCards);
        if (pair) {p.setHandRanking(HandRanking.ONE_PAIR); return HandRanking.ONE_PAIR;}
        return HandRanking.JUNK;
    }

    private boolean checkFlush(ArrayList<Card> cards) {
        Map<Suit, Integer> suitMap = new HashMap<Suit, Integer>();
        Integer suitCount;
        for (Card c : cards) {
            if (suitMap.containsKey(c.getSuit())) {
                suitCount = suitMap.get(c.getSuit()) + 1; }
            else {
                suitCount = 1;
            }
            suitMap.put(c.getSuit(), suitCount);
        }
        for (Suit s : Suit.values()) {
            if (suitMap.containsKey(s) && suitMap.get(s) >= 5) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPair(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = new HashMap<>();
        Integer valueCount;
        for (Card c : cards) {
            if (valueMap.containsKey(c.getValue())) {
                valueCount = valueMap.get(c.getValue()) + 1; }
            else {
                valueCount = 1;
            }
            valueMap.put(c.getValue(), valueCount);
        }
        for (Value v : Value.values()) {
            if (valueMap.containsKey(v) && valueMap.get(v) == 2) {
                return true;
            }
        }
        return false;
    }

    private boolean checkTwoPair(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = new HashMap<>();
        Integer valueCount;
        for (Card c : cards) {
            if (valueMap.containsKey(c.getValue())) {
                valueCount = valueMap.get(c.getValue()) + 1; }
            else {
                valueCount = 1;
            }
            valueMap.put(c.getValue(), valueCount);
        }
        boolean onePair = false;
        for (Value v : Value.values()) {
            if (valueMap.containsKey(v) && valueMap.get(v) == 2) {
                if (onePair) {
                    return true;
                } else {
                    onePair = true;
                }
            }
        }
        return false;
    }

    private boolean checkTrips(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = new HashMap<>();
        Integer valueCount;
        for (Card c : cards) {
            if (valueMap.containsKey(c.getValue())) {
                valueCount = valueMap.get(c.getValue()) + 1; }
            else {
                valueCount = 1;
            }
            valueMap.put(c.getValue(), valueCount);
        }
        for (Value v : Value.values()) {
            if (valueMap.containsKey(v) && valueMap.get(v) == 3) {return true; }
        }
        return false;
    }

    private boolean checkQuads(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = new HashMap<>();
        Integer valueCount;
        for (Card c : cards) {
            if (valueMap.containsKey(c.getValue())) {
                valueCount = valueMap.get(c.getValue()) + 1; }
            else {
                valueCount = 1;
            }
            valueMap.put(c.getValue(), valueCount);
        }
        for (Value v : Value.values()) {
            if (valueMap.containsKey(v) && valueMap.get(v) == 4) {return true; }
        }
        return false;
    }

    private boolean checkFullHouse(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = new HashMap<>();
        Integer valueCount;
        for (Card c : cards) {
            if (valueMap.containsKey(c.getValue())) {
                valueCount = valueMap.get(c.getValue()) + 1; }
            else {
                valueCount = 1;
            }
            valueMap.put(c.getValue(), valueCount);
        }
        boolean trips = false;
        boolean pair = false;
        for (Value v : Value.values()) {
            if (valueMap.containsKey(v) && valueMap.get(v) == 2) {pair = true; }
            if (valueMap.containsKey(v) && valueMap.get(v) == 3) {trips = true; }
        }
        return pair && trips;
    }

    private boolean checkStraight(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = new HashMap<>();
        Integer valueCount;
        for (Card c : cards) {
            if (valueMap.containsKey(c.getValue())) {
                valueCount = valueMap.get(c.getValue()) + 1; }
            else {
                valueCount = 1;
            }
            valueMap.put(c.getValue(), valueCount);
        }
        int straightCounter = 0;
        for (Value v : Value.values()) {
            if (valueMap.containsKey(v) && valueMap.get(v) >= 1) {
                for (Value vNext : Value.values()) {
                    if (Value.getIntValue(vNext) > Value.getIntValue(v)) { //if v = 4, don't start looking until vNext = 5
                        if (straightCounter == 5) {
                            return true;
                        }
                        if (!valueMap.containsKey(vNext)) {
                            break;
                        }
                        else if (valueMap.containsKey(vNext) && valueMap.get(vNext) >= 1) {
                            straightCounter++;
                        }

                    }
                }

            }
        }
        //check for wheel, since ACE can be high or low
        if (valueMap.containsKey(Value.ACE) && valueMap.get(Value.ACE) == 1 &&
                valueMap.containsKey(Value.TWO) && valueMap.get(Value.TWO) == 1 &&
                valueMap.containsKey(Value.THREE) && valueMap.get(Value.THREE) == 1 &&
                valueMap.containsKey(Value.FOUR) && valueMap.get(Value.FOUR) == 1 &&
                valueMap.containsKey(Value.FIVE) && valueMap.get(Value.FIVE) == 1) {return true;}
        return false;
    }

    // requires implementation if there are 2 cards that make a straight, but only 1 makes a flush
    // requires implementation if there are multiple numbers that make a straight (2,3,4,5,6, and the board is 2,2,3,4,5,6,K)
    private boolean checkStraightFlush(ArrayList<Card> cards) {
        ArrayList<Card> straightCards = new ArrayList<>();
        if (!checkStraight(cards)) {return false;}
        cards.sort(new SortByValue()); //sort the list by value
        //Determine the straight values
        Map<Value, Integer> valueMap = new HashMap<>();
        Integer valueCount;
        for (Card c : cards) {
            if (valueMap.containsKey(c.getValue())) {
                valueCount = valueMap.get(c.getValue()) + 1; }
            else {
                valueCount = 1;
            }
            valueMap.put(c.getValue(), valueCount);
        }
        int straightCounter = 0;
        Value straightStart = Value.KING;
        for (Value v : Value.values()) {
            if (valueMap.containsKey(v) && valueMap.get(v) == 1) {
                for (Value vNext : Value.values()) {
                    if (Value.getIntValue(vNext) > Value.getIntValue(v)) { //if v = 4, don't start looking until vNext = 5
                        if (straightCounter == 5) {
                            straightStart = v;
                        }
                        if (valueMap.containsKey(vNext) && valueMap.get(vNext) == 0) {
                            break;
                        } else if (valueMap.containsKey(vNext) && valueMap.get(vNext) == 1) {
                            straightCounter++;
                        }

                    }
                }

            }
        }

        //build the array of cards that make the straight
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getValue() == straightStart) {
                for (int j = 0; j < 4; j++) {
                    straightCards.add(cards.get(j));
                }
            }
        }


        //check for wheel, since ACE can be high or low
        //check for wheel, since ACE can be high or low
        if (valueMap.containsKey(Value.ACE) && valueMap.get(Value.ACE) == 1 &&
                valueMap.containsKey(Value.TWO) && valueMap.get(Value.TWO) == 1 &&
                valueMap.containsKey(Value.THREE) && valueMap.get(Value.THREE) == 1 &&
                valueMap.containsKey(Value.FOUR) && valueMap.get(Value.FOUR) == 1 &&
                valueMap.containsKey(Value.FIVE) && valueMap.get(Value.FIVE) == 1) {
            for (Card c : cards) {
                if (c.getValue() == Value.ACE || c.getValue() == Value.TWO || c.getValue() == Value.THREE || c.getValue() == Value.FOUR || c.getValue() == Value.FIVE) {
                    straightCards.add(c);
                }
            }
        }

        return checkFlush(straightCards);
    }

    //requires implementation
    private boolean checkRoyalFlush(ArrayList<Card> cards) {
        return false;
    }


    //Anonymous class, also comparator for sorting for a straight
    class SortByValue implements Comparator<Card> {
        @Override
        public int compare(Card a, Card b) {
            return Value.getIntValue(a.getValue()) - Value.getIntValue(b.getValue());
        }
    }

}
