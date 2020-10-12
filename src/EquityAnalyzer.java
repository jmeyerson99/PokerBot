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
            getBestHand(p.getHand(), this.table.getBoard(), p);
            System.out.println("Player " + p.getName() + " has " + p.getHandRanking() + ": " + p.getBestPossibleHand());
        }
    }

    private void getBestHand(Hand hand, Board board, Player p) {
        ArrayList<Card> allCards = new ArrayList<>();
        allCards.add(hand.getCard1());
        allCards.add(hand.getCard2());
        allCards.addAll(board.getBoard());

        boolean royalFlush = checkRoyalFlush(allCards);
        if (royalFlush) {
            p.setHandRanking(HandRanking.ROYAL_FLUSH);
            return;
        }
        boolean straightFlush = checkStraightFlush(allCards);
        if (straightFlush) {
            p.setHandRanking(HandRanking.STRAIGHT_FLUSH);
            return;
        }
        boolean quads = checkQuads(allCards);
        if (quads) {
            p.setHandRanking(HandRanking.QUADS);
            p.setBestPossibleHand(quadsBestHand(allCards));
            return;
        }
        boolean fullHouse = checkFullHouse(allCards);
        if (fullHouse) {
            p.setHandRanking(HandRanking.FULL_HOUSE);
            p.setBestPossibleHand(fullHouseBestHand(allCards));
            return;
        }
        boolean flush = checkFlush(allCards);
        if (flush) {
            p.setHandRanking(HandRanking.FLUSH);
            p.setBestPossibleHand(flushBestHand(allCards));
            return;
        }
        boolean straight = checkStraight(allCards);
        if (straight) {
            p.setHandRanking(HandRanking.STRAIGHT);
            return;
        }
        boolean trips = checkTrips(allCards);
        if (trips) {
            p.setHandRanking(HandRanking.TRIPS);
            p.setBestPossibleHand(tripsBestHand(allCards));
            return;
        }
        boolean twoPair = checkTwoPair(allCards);
        if (twoPair) {
            p.setHandRanking(HandRanking.TWO_PAIR);
            p.setBestPossibleHand(twoPairBestHand(allCards));
            return;
        }
        boolean pair = checkPair(allCards);
        if (pair) {
            p.setHandRanking(HandRanking.ONE_PAIR);
            p.setBestPossibleHand(onePairBestHand(allCards));
            return;
        }
        p.setHandRanking(HandRanking.HIGH_CARD);
        p.setBestPossibleHand(highCardBestHand(allCards));
    }

    private Map<Value, Integer> mapCardsToValue(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = new HashMap<>();
        int valueCount = 0;
        for (Card c : cards) {
            if (valueMap.containsKey(c.getValue())) {
                valueCount = valueMap.get(c.getValue()) + 1; }
            else {
                valueCount = 1;
            }
            valueMap.put(c.getValue(), valueCount);
        }
        return valueMap;
    }

    private Map<Suit, Integer> mapCardsToSuit(ArrayList<Card> cards) {
        Map<Suit, Integer> suitMap = new HashMap<Suit, Integer>();
        int suitCount;
        for (Card c : cards) {
            if (suitMap.containsKey(c.getSuit())) {
                suitCount = suitMap.get(c.getSuit()) + 1; }
            else {
                suitCount = 1;
            }
            suitMap.put(c.getSuit(), suitCount);
        }
        return suitMap;
    }

    private boolean checkFlush(ArrayList<Card> cards) {
        Map<Suit, Integer> suitMap = mapCardsToSuit(cards);
        cards.sort(new SortByValue()); //sort the cards by value (ace is high)

        for (Suit s : suitMap.keySet()) {
            if (suitMap.get(s) >= 5) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Card> flushBestHand(ArrayList<Card> cards) {
        Map<Suit, Integer> suitMap = mapCardsToSuit(cards);
        cards.sort(new SortByValue()); //sort the cards by value (ace is high)
        ArrayList<Card> bestFiveCards = new ArrayList<>(); //create array to hold the final hand
        Suit flushSuit = null; //does not matter what this is initialized to
        for (Suit s : suitMap.keySet()) {
            if (suitMap.get(s) >= 5) {
                flushSuit = s;
            }
        }

        for (Card c : cards) {
            if (c.getSuit() == flushSuit && bestFiveCards.size() < 5) {
                bestFiveCards.add(c);
            }
        }
        return bestFiveCards;
    }

    private ArrayList<Card> highCardBestHand(ArrayList<Card> cards) {
        cards.sort(new SortByValue()); //sort the cards by value, high to low (ace is high)
        ArrayList<Card> bestFiveCards = new ArrayList<>(); //create array to hold the final hand

        while (bestFiveCards.size() < 5) {
            bestFiveCards.add(cards.get(0));
            cards.remove(0);
        }
        return bestFiveCards;
    }

    private boolean checkPair(ArrayList<Card> cards) {
        boolean pairFlag = false;
        Map<Value, Integer> valueMap = mapCardsToValue(cards);
        cards.sort(new SortByValue()); //sort the cards by value (ace is high)

        for (Value v : valueMap.keySet()) {
            // if the map shows 2 of the same card, then you have a pair!
            if (valueMap.get(v) == 2) {
                pairFlag = true;
            }
        }
        return pairFlag;
    }

    private ArrayList<Card> onePairBestHand(ArrayList<Card> cards) {
        // If the player's hand is 1 pair, create the best hand
        Value pairValue = null; //doesn't matter what this is
        Map<Value, Integer> valueMap = mapCardsToValue(cards); //create counts of card values
        cards.sort(new SortByValue()); //sort the cards by value, high to low (ace is high)
        ArrayList<Card> bestFiveCards = new ArrayList<>(); //create array to hold the final hand
        for (Value v : valueMap.keySet()) { //
            if (valueMap.get(v) == 2) {
                pairValue = v;
            }
        }

        for (Card c : cards) { // iterate through all cards (highest to lowest)
            if (c.getValue() == pairValue) { // get a card that makes up the pair
                bestFiveCards.add(c); //add that card to the hand
            }
        }

        cards.removeAll(bestFiveCards); // remove all the cards added to the best hand from the remaining cards
        while (bestFiveCards.size() < 5) { //go through the sorted card values and add them until there are 5 cards in the final hand
            bestFiveCards.add(cards.get(0)); //add the last card in the sorted array to the best hand
            cards.remove(cards.get(0)); //remove the added card from the list
        }
        return bestFiveCards;
    }

    private boolean checkTwoPair(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = mapCardsToValue(cards);
        boolean onePair = false;
        for (Value v : valueMap.keySet()) {
            if (valueMap.get(v) == 2) {
                if (onePair) {
                    return true;
                } else {
                    onePair = true;
                }
            }
        }
        return false;
    }

    private ArrayList<Card> twoPairBestHand(ArrayList<Card> cards) {
        // If the player's hand is 2 pair, create the best hand
        Value biggerPair = null; //doesn't matter what this is
        Value smallerPair = null;
        boolean foundBiggerPair = false; //did I find the first pair?
        Map<Value, Integer> valueMap = mapCardsToValue(cards); //create counts of card values
        cards.sort(new SortByValue()); //sort the cards by value (high to low)
        ArrayList<Card> bestFiveCards = new ArrayList<>(); //create array to hold the final hand
        for (Value v : valueMap.keySet()) { //
            if (valueMap.get(v) == 2) {
                if (foundBiggerPair) {smallerPair = v;}
                else { biggerPair = v; foundBiggerPair = true;}
            }
        }

        for (Card c : cards) { // iterate through all cards (high to low)
            if (c.getValue() == smallerPair || c.getValue() == biggerPair) { // get a card that makes up either pair
                bestFiveCards.add(c); //add that card to the hand
            }
        }

        cards.removeAll(bestFiveCards); // remove all the cards added to the best hand from the remaining cards
        while (bestFiveCards.size() < 5) { //go through the sorted card values and add them until there are 5 cards in the final hand
            bestFiveCards.add(cards.get(0)); //add the last card in the sorted array to the best hand
            cards.remove(cards.get(0)); //remove the added card from the list
        }
        return bestFiveCards;
    }

    private boolean checkTrips(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = mapCardsToValue(cards);
        for (Value v : valueMap.keySet()) {
            if (valueMap.get(v) == 3) {return true; }
        }
        return false;
    }

    private ArrayList<Card> tripsBestHand(ArrayList<Card> cards) {
        // If the player's hand is trips, create the best hand
        Value tripsValue = null; //doesn't matter what this is
        Map<Value, Integer> valueMap = mapCardsToValue(cards); //create counts of card values
        cards.sort(new SortByValue()); //sort the cards by value (ace is high)
        ArrayList<Card> bestFiveCards = new ArrayList<>(); //create array to hold the final hand
        for (Value v : valueMap.keySet()) { //
            if (valueMap.get(v) == 3) {
                tripsValue = v;
            }
        }

        for (Card c : cards) { // iterate through all cards (high to low)
            if (c.getValue() == tripsValue) { // get a card that makes up the trips
                bestFiveCards.add(c); //add that card to the hand
            }
        }

        cards.removeAll(bestFiveCards); // remove all the cards added to the best hand from the remaining cards
        while (bestFiveCards.size() < 5) { //go through the sorted card values and add them until there are 5 cards in the final hand
            bestFiveCards.add(cards.get(0)); //add the last card in the sorted array to the best hand
            cards.remove(cards.get(0)); //remove the added card from the list
        }
        return bestFiveCards;
    }

    private boolean checkQuads(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = mapCardsToValue(cards);
        for (Value v : Value.values()) {
            if (valueMap.containsKey(v) && valueMap.get(v) == 4) {return true; }
        }
        return false;
    }

    private ArrayList<Card> quadsBestHand(ArrayList<Card> cards) {
        // If the player's hand is quads, create the best hand
        Value quadsValue = null; //doesn't matter what this is
        Map<Value, Integer> valueMap = mapCardsToValue(cards); //create counts of card values
        cards.sort(new SortByValue()); //sort the cards by value (high to low)
        ArrayList<Card> bestFiveCards = new ArrayList<>(); //create array to hold the final hand
        for (Value v : valueMap.keySet()) { //
            if (valueMap.get(v) == 4) {
                quadsValue = v;
            }
        }

        for (Card c : cards) { // iterate through all cards
            if (c.getValue() == quadsValue) { // get a card that makes up the quads
                bestFiveCards.add(c); //add that card to the hand
            }
        }

        cards.removeAll(bestFiveCards); // remove all the cards added to the best hand from the remaining cards
        while (bestFiveCards.size() < 5) { //go through the sorted card values and add them until there are 5 cards in the final hand
            bestFiveCards.add(cards.get(cards.size()-1)); //add the last card in the sorted array to the best hand
            cards.remove(cards.get(cards.size()-1)); //remove the added card from the list
        }
        return bestFiveCards;
    }

    //TODO - need to account for 2 sets of trips that make a full house
    private boolean checkFullHouse(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = mapCardsToValue(cards);
        boolean trips = false;
        boolean pair = false;
        for (Value v : valueMap.keySet()) {
            if (valueMap.get(v) == 2) {pair = true; }
            if (valueMap.get(v) == 3) {trips = true; }
        }
        return pair && trips;
    }

    //TODO - need to account for 2 sets of trips that make a full house
    private ArrayList<Card> fullHouseBestHand(ArrayList<Card> cards) {
        // If the player's hand is a full house, create the best hand
        Value tripsValue = null; //doesn't matter what this is
        Value pairValue = null;
        Map<Value, Integer> valueMap = mapCardsToValue(cards); //create counts of card values
        cards.sort(new SortByValue()); //sort the cards by value (high to low)
        ArrayList<Card> bestFiveCards = new ArrayList<>(); //create array to hold the final hand
        for (Value v : valueMap.keySet()) {
            if (valueMap.get(v) == 3 && tripsValue == null) { //requires second argument not to overwrite the higher trips value if a lower set of trips occurs
                tripsValue = v;
            } else if (valueMap.get(v) == 2 && pairValue == null) {
                pairValue = v;
            }
        }

        for (Card c : cards) { // iterate through all cards to get trips value
            if (c.getValue() == tripsValue) { // get a card that makes up the trips
                bestFiveCards.add(c); //add that card to the hand
            }
        }
        for (Card c : cards) { // iterate through all cards to get pair value
            if (c.getValue() == pairValue) { // get a card that makes up the pair
                bestFiveCards.add(c); //add that card to the hand
            }
        }

        cards.removeAll(bestFiveCards); // remove all the cards added to the best hand from the remaining cards
        return bestFiveCards;
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
                            straightCounter = 0;
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

    // TODO - requires implementation if there are 2 cards that make a straight, but only 1 makes a flush
    // TODO - requires implementation if there are multiple numbers that make a straight (2,3,4,5,6, and the board is 2,2,3,4,5,6,K)
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

    // TODO - requires implementation
    private boolean checkRoyalFlush(ArrayList<Card> cards) {
        return false;
    }


    //Anonymous class, also comparator for sorting from high value to low
    class SortByValue implements Comparator<Card> {
        @Override
        public int compare(Card a, Card b) {
            return Value.getIntValue(b.getValue()) - Value.getIntValue(a.getValue());
        }
    }

}
