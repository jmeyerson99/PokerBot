package equity;

import model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

// TODO - consider making an Equity Analyzer package, each group of 2 function (checkPair and pairBestHand) get put together in a class
public class EquityAnalyzer {

    private Table table;

    public EquityAnalyzer(Table table) {
        this.table = table;
    }

    /**
     * Determine each player's equity in the hand, based on the number of cards dealt so far
     */
    public void determineEquity() {
        Board board = table.getBoard();
        switch (board.getSize()) {
            // Who is leading pre flop?
            case 0:
                break;

            // Who is leading after the flop?
            case 3:
                break;

            // Who is leading after the turn?
            case 4:
                break;

            // Who won?
            case 5:
                determineShowdownWinner();
                break;

            default:
                // ERROR (invalid size)
                break;
        }
    }

    /**
     * Given a player, determine the best hand that can be made with the board and the player's hole cards
     * @param p The player
     */
    public HandRanking determineBestPossibleHand(Player p) {
        ArrayList<Card> allCards = new ArrayList<>();
        allCards.add(p.getHand().getCard1());
        allCards.add(p.getHand().getCard2());
        allCards.addAll(table.getBoard().getBoard()); //TODO not great, may want to fix later

        boolean royalFlush = checkRoyalFlush(allCards);
        if (royalFlush) {
            return HandRanking.ROYAL_FLUSH;
        }
        boolean straightFlush = checkStraightFlush(allCards);
        if (straightFlush) {
            return HandRanking.STRAIGHT_FLUSH;
        }
        boolean quads = checkQuads(allCards);
        if (quads) {
            return HandRanking.QUADS;
        }
        boolean fullHouse = checkFullHouse(allCards);
        if (fullHouse) {
            return HandRanking.FULL_HOUSE;
        }
        boolean flush = checkFlush(allCards);
        if (flush) {
            return HandRanking.FLUSH;
        }
        boolean straight = checkStraight(allCards);
        if (straight) {
            return HandRanking.STRAIGHT;
        }
        boolean trips = checkTrips(allCards);
        if (trips) {
            return HandRanking.TRIPS;
        }
        boolean twoPair = checkTwoPair(allCards);
        if (twoPair) {
            return HandRanking.TWO_PAIR;
        }
        boolean pair = checkPair(allCards);
        if (pair) {
            return HandRanking.ONE_PAIR;
        }
        return HandRanking.HIGH_CARD;
    }

    // TODO - handle  a CHOP
    private void determineShowdownWinner() {
        for (Player p : this.table.getPlayers()) {
            ArrayList<Card> bestHand = getBestHand(this.table.getBoard(), p);
            System.out.println("Player " + p.getName() + " has " + p.getHandRanking() + ": " + bestHand);
        }

        ArrayList<Player> playerRankings = new ArrayList<>();
        playerRankings.addAll(this.table.getPlayers());
        playerRankings.sort(new SortPlayersByBestHand());
        System.out.println("Official Rankings: ");
        for (Player p : playerRankings) {
            System.out.println(p.getName());
        }
    }

    /**
     * Determine the best hand a player can make, given a board, and the player. This method returns the best 5 card
     * hand, and sets that hand in the Player field.
     * @param board The board
     * @param p The player (allows access to their hole cards)
     */
    public ArrayList<Card> getBestHand(Board board, Player p) {
        ArrayList<Card> allCards = new ArrayList<>();
        allCards.add(p.getHand().getCard1());
        allCards.add(p.getHand().getCard2());
        allCards.addAll(board.getBoard());

        ArrayList<Card> bestHand;

        boolean royalFlush = checkRoyalFlush(allCards);
        if (royalFlush) {
            p.setHandRanking(HandRanking.ROYAL_FLUSH);
            bestHand = royalFlushBestHand(allCards);
            p.setBestPossibleHand(bestHand);
            return bestHand;
        }
        boolean straightFlush = checkStraightFlush(allCards);
        if (straightFlush) {
            p.setHandRanking(HandRanking.STRAIGHT_FLUSH);
            bestHand = straightFlushBestHand(allCards);
            p.setBestPossibleHand(bestHand);
            return bestHand;
        }
        boolean quads = checkQuads(allCards);
        if (quads) {
            p.setHandRanking(HandRanking.QUADS);
            bestHand = quadsBestHand(allCards);
            p.setBestPossibleHand(bestHand);
            return bestHand;
        }
        boolean fullHouse = checkFullHouse(allCards);
        if (fullHouse) {
            p.setHandRanking(HandRanking.FULL_HOUSE);
            bestHand = fullHouseBestHand(allCards);
            p.setBestPossibleHand(bestHand);
            return bestHand;
        }
        boolean flush = checkFlush(allCards);
        if (flush) {
            p.setHandRanking(HandRanking.FLUSH);
            bestHand = flushBestHand(allCards);
            p.setBestPossibleHand(bestHand);
            return bestHand;
        }
        boolean straight = checkStraight(allCards);
        if (straight) {
            p.setHandRanking(HandRanking.STRAIGHT);
            bestHand = straightBestHand(allCards);
            p.setBestPossibleHand(bestHand);
            return bestHand;
        }
        boolean trips = checkTrips(allCards);
        if (trips) {
            p.setHandRanking(HandRanking.TRIPS);
            bestHand = tripsBestHand(allCards);
            p.setBestPossibleHand(bestHand);
            return bestHand;
        }
        boolean twoPair = checkTwoPair(allCards);
        if (twoPair) {
            p.setHandRanking(HandRanking.TWO_PAIR);
            bestHand = twoPairBestHand(allCards);
            p.setBestPossibleHand(bestHand);
            return bestHand;
        }
        boolean pair = checkPair(allCards);
        if (pair) {
            p.setHandRanking(HandRanking.ONE_PAIR);
            bestHand = onePairBestHand(allCards);
            p.setBestPossibleHand(bestHand);
            return bestHand;
        }
        p.setHandRanking(HandRanking.HIGH_CARD);
        bestHand = highCardBestHand(allCards);
        p.setBestPossibleHand(bestHand);
        return bestHand;
    }

    /**
     * Creates a map that holds a count of the numerical frequency of the list of cards (by value)
     * @param cards The list of 7 cards
     * @return The map
     */
    private Map<Value, Integer> mapCardsToValue(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = new HashMap<>();
        cards.sort(new SortByValueDescending());
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

    /**
     * Creates a map that holds a count of the suit frequency of the list of cards
     * @param cards The list of 7 cards
     * @return The map
     */
    private Map<Suit, Integer> mapCardsToSuit(ArrayList<Card> cards) {
        Map<Suit, Integer> suitMap = new HashMap<Suit, Integer>();
        cards.sort(new SortByValueDescending());
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

    /**
     * Given a list of 7 cards, determine if they can create a flush
     * @param cards The list of 7 cards
     * @return True if a flush is possible, false if not
     */
    private boolean checkFlush(ArrayList<Card> cards) {
        Map<Suit, Integer> suitMap = mapCardsToSuit(cards);
        cards.sort(new SortByValueDescending()); //sort the cards by value (ace is high)

        for (Suit s : suitMap.keySet()) {
            if (suitMap.get(s) >= 5) {
                return true;
            }
        }
        return false;
    }

    /**
     * If a flush is possible in 'cards', determine which 5 cards make the best flush
     * @param cards The list of 7 cards
     * @return The 5 cards to make the best possible flush hand
     */
    private ArrayList<Card> flushBestHand(ArrayList<Card> cards) {
        Map<Suit, Integer> suitMap = mapCardsToSuit(cards);
        cards.sort(new SortByValueDescending()); //sort the cards by value (ace is high)
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

    /**
     * If the best hand is high cardd, determine which 5 cards make the best hand
     * @param cards The list of 7 cards
     * @return The 5 cards to make the best possible high card hand
     */
    private ArrayList<Card> highCardBestHand(ArrayList<Card> cards) {
        cards.sort(new SortByValueDescending()); //sort the cards by value, high to low (ace is high)
        ArrayList<Card> bestFiveCards = new ArrayList<>(); //create array to hold the final hand

        while (bestFiveCards.size() < 5) {
            bestFiveCards.add(cards.get(0));
            cards.remove(0);
        }
        return bestFiveCards;
    }

    /**
     * Given a list of 7 cards, determine if they can create a pair
     * @param cards The list of 7 cards
     * @return True if a pair is possible, false if not
     */
    private boolean checkPair(ArrayList<Card> cards) {
        boolean pairFlag = false;
        Map<Value, Integer> valueMap = mapCardsToValue(cards);
        cards.sort(new SortByValueDescending()); //sort the cards by value (ace is high)

        for (Value v : valueMap.keySet()) {
            // if the map shows 2 of the same card, then you have a pair!
            if (valueMap.get(v) == 2) {
                pairFlag = true;
            }
        }
        return pairFlag;
    }

    /**
     * If the best hand is one pair, determine which 5 cards make the best hand
     * @param cards The list of 7 cards
     * @return The 5 cards to make the best possible one pair hand
     */
    private ArrayList<Card> onePairBestHand(ArrayList<Card> cards) {
        // If the player's hand is 1 pair, create the best hand
        Value pairValue = null; //doesn't matter what this is
        Map<Value, Integer> valueMap = mapCardsToValue(cards); //create counts of card values
        cards.sort(new SortByValueDescending()); //sort the cards by value, high to low (ace is high)
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

    /**
     * Given a list of 7 cards, determine if they can create 2 pair
     * @param cards The list of 7 cards
     * @return True if 2 pair is possible, false if not
     */
    private boolean checkTwoPair(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = mapCardsToValue(cards);
        boolean onePair = false;
        for (Value v : Value.values()) { //requires this to be sorted in descending order
            if (valueMap.containsKey(v)) {
                if (valueMap.get(v) == 2) {
                    if (onePair) {
                        return true;
                    } else {
                        onePair = true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * If the best hand is two pair, determine which 5 cards make the best hand
     * @param cards The list of 7 cards
     * @return The 5 cards to make the best possible two pair hand
     */
    private ArrayList<Card> twoPairBestHand(ArrayList<Card> cards) {
        // If the player's hand is 2 pair, create the best hand
        Value biggerPair = null; //doesn't matter what this is
        Value smallerPair = null;
        boolean foundBiggerPair = false; //did I find the first pair?
        Map<Value, Integer> valueMap = mapCardsToValue(cards); //create counts of card values
        cards.sort(new SortByValueDescending()); //sort the cards by value (high to low)
        ArrayList<Card> bestFiveCards = new ArrayList<>(); //create array to hold the final hand
        for (Value v : valueMap.keySet()) {
            if (valueMap.get(v) == 2) {
                if (foundBiggerPair) {
                    smallerPair = v;
                    break;
                }
                else {
                    biggerPair = v;
                    foundBiggerPair = true;
                }
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

    /**
     * Given a list of 7 cards, determine if they can create trips
     * @param cards The list of 7 cards
     * @return True if trips is possible, false if not
     */
    private boolean checkTrips(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = mapCardsToValue(cards);
        for (Value v : valueMap.keySet()) {
            if (valueMap.get(v) == 3) {return true; }
        }
        return false;
    }

    /**
     * If the best hand is trips, determine which 5 cards make the best hand
     * @param cards The list of 7 cards
     * @return The 5 cards to make the best possible trips hand
     */
    private ArrayList<Card> tripsBestHand(ArrayList<Card> cards) {
        // If the player's hand is trips, create the best hand
        Value tripsValue = null; //doesn't matter what this is
        Map<Value, Integer> valueMap = mapCardsToValue(cards); //create counts of card values
        cards.sort(new SortByValueDescending()); //sort the cards by value (ace is high)
        ArrayList<Card> bestFiveCards = new ArrayList<>(); //create array to hold the final hand
        for (Value v : valueMap.keySet()) { //
            if (valueMap.get(v) == 3) {
                tripsValue = v;
                break; // break because if there are 2 sets of trips, this will ignore the lower value
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

    /**
     * Given a list of 7 cards, determine if they can create quads
     * @param cards The list of 7 cards
     * @return True if quads is possible, false if not
     */
    private boolean checkQuads(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = mapCardsToValue(cards);
        for (Value v : valueMap.keySet()) {
            if (valueMap.get(v) == 4) {return true; }
        }
        return false;
    }

    /**
     * If the best hand is quads, determine which 5 cards make the best hand
     * @param cards The list of 7 cards
     * @return The 5 cards to make the best possible quads hand
     */
    private ArrayList<Card> quadsBestHand(ArrayList<Card> cards) {
        // If the player's hand is quads, create the best hand
        Value quadsValue = null; //doesn't matter what this is
        Map<Value, Integer> valueMap = mapCardsToValue(cards); //create counts of card values
        cards.sort(new SortByValueDescending()); //sort the cards by value (high to low)
        ArrayList<Card> bestFiveCards = new ArrayList<>(); //create array to hold the final hand
        for (Value v : valueMap.keySet()) { //
            if (valueMap.get(v) == 4) {
                quadsValue = v;
                break;
            }
        }

        for (Card c : cards) { // iterate through all cards
            if (c.getValue() == quadsValue) { // get a card that makes up the quads
                bestFiveCards.add(c); //add that card to the hand
            }
        }

        cards.removeAll(bestFiveCards); // remove all the cards added to the best hand from the remaining cards
        while (bestFiveCards.size() < 5) { //go through the sorted card values and add them until there are 5 cards in the final hand
            bestFiveCards.add(cards.get(0)); //add the next highest card to the list of best cards
            cards.remove(cards.get(0)); //remove the added card from the list
        }
        return bestFiveCards;
    }

    /**
     * Given a list of 7 cards, determine if they can create a full house
     * @param cards The list of 7 cards
     * @return True if a full house is possible, false if not
     */
    private boolean checkFullHouse(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = mapCardsToValue(cards);
        boolean trips = false;
        boolean pair = false;

        for (Value v : Value.values()) { // get list of values already sorted from high to low
            if (valueMap.containsKey(v)) { // make sure the key exists in the map
                if (valueMap.get(v) == 3 && !trips) {
                    trips = true;
                }
                else if (valueMap.get(v) == 2 && !pair) {
                    pair = true;
                }
                else if (valueMap.get(v) >= 2 && trips && !pair) {
                    pair = true;
                }
            }
        }
        return pair && trips;
    }

    /**
     * If the best hand is a full house, determine which 5 cards make the best hand
     * @param cards The list of 7 cards
     * @return The 5 cards to make the best possible full house hand
     */
    private ArrayList<Card> fullHouseBestHand(ArrayList<Card> cards) {
        // If the player's hand is a full house, create the best hand
        Value tripsValue = null; //doesn't matter what this is
        Value pairValue = null;
        boolean trips = false;
        boolean pair = false;
        Map<Value, Integer> valueMap = mapCardsToValue(cards); //create counts of card values
        cards.sort(new SortByValueDescending()); //sort the cards by value (high to low)
        ArrayList<Card> bestFiveCards = new ArrayList<>(); //create array to hold the final hand

        for (Value v : Value.values()) { // get list of values already sorted from high to low
            if (valueMap.containsKey(v)) { // make sure the key exists in the map
                if (valueMap.get(v) == 3 && !trips) {
                    tripsValue = v;
                    trips = true;
                }
                else if (valueMap.get(v) == 2 && !pair) {
                    pairValue = v;
                    pair = true;
                }
                else if (valueMap.get(v) >= 2 && trips && !pair) {
                    pairValue = v;
                    pair = true;
                }
            }
        }

        for (Card c : cards) { // iterate through all cards to get trips value
            if (c.getValue() == tripsValue) { // get a card that makes up the trips
                bestFiveCards.add(c); //add that card to the hand
            }
        }
        for (Card c : cards) { // iterate through all cards to get pair value
            if (c.getValue() == pairValue && bestFiveCards.size() < 5) { // get a card that makes up the pair
                bestFiveCards.add(c); //add that card to the hand
            }
        }

        cards.removeAll(bestFiveCards); // remove all the cards added to the best hand from the remaining cards
        return bestFiveCards;
    }

    /**
     * Given a list of 7 cards, determine if they can create a straight
     * @param cards The list of 7 cards
     * @return True if a straight is possible, false if not
     */
    private boolean checkStraight(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = mapCardsToValue(cards); //may not be needed
        cards.sort(new SortByValueDescending()); //sort the cards by value (ace is high)

        int straightCounter = 0;
        Value starterValue = null;
        int skipCount = 0; //used to keep track of repeated straight cards (skip an index when comparing values in the array)
        for (int i = 0; i < cards.size(); i++) {
            Card c = cards.get(i);
            starterValue = c.getValue();
            straightCounter = 0;
            skipCount = 0;

            for (int j = 0; j < cards.size() - i; j++) { // loop through remaining cards (j is offset from i)
                Card ca  = cards.get(i+j); // DEBUG
                int startVal = Value.getIntValue(starterValue); // DEBUG
                int compareVal = Value.getIntValue(cards.get(i+j).getValue()) + j; // DEBUG
                if (straightCounter == 5) {
                    return true;
                } else if (Value.getIntValue(cards.get(i + j).getValue()) + j - skipCount == Value.getIntValue(starterValue)) {
                    straightCounter++;
                } else if (Value.getIntValue(cards.get(i + j).getValue()) == Value.getIntValue(cards.get(i + j - 1).getValue())) { // else if (value is equal to previous, then skip and move to next card)
                    skipCount++;
                } else {
                    break;
                }
            }
            if (straightCounter == 5) { // catches the case where the final card of the straight is the final card of the array
                return true;
            }
        }

        //check for wheel, since ACE can be high or low
        return isAWheel(cards);
    }

    /**
     * Helper function for straights to determine if the straight is a wheel
     * @param cards A list of 7 cards
     * @return True if a wheel can be made from the 7 cards
     */
    private boolean isAWheel(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = mapCardsToValue(cards); //may not be needed
        if (valueMap.containsKey(Value.ACE) &&
                valueMap.containsKey(Value.TWO) &&
                valueMap.containsKey(Value.THREE) &&
                valueMap.containsKey(Value.FOUR) &&
                valueMap.containsKey(Value.FIVE))
        {return true;}
        return false;
    }

    /**
     * If the best hand is a straight, determine which 5 cards make the best hand
     * @param cards The list of 7 cards
     * @return The 5 cards to make the best possible straight hand
     */
    private ArrayList<Card> straightBestHand(ArrayList<Card> cards) {
        // If the player's hand is a straight, create the best hand

        Map<Value, Integer> valueMap = mapCardsToValue(cards); //may not be needed
        cards.sort(new SortByValueDescending()); //sort the cards by value (ace is high)
        ArrayList<Card> bestFiveCards = new ArrayList<>(); //create array to hold the final hand

        //Start with a wheel so there is no index out of bounds error later
        if (isAWheel(cards)) {
            boolean aceFlag = false, twoFlag = false, threeFlag = false, fourFlag = false, fiveFlag = false;
            for (Card c : cards) {
                if (!aceFlag && c.getValue() == Value.ACE) {aceFlag = true; bestFiveCards.add(c); }
                if (!twoFlag && c.getValue() == Value.TWO) {twoFlag = true; bestFiveCards.add(c); }
                if (!threeFlag && c.getValue() == Value.THREE) {threeFlag = true; bestFiveCards.add(c); }
                if (!fourFlag && c.getValue() == Value.FOUR) {fourFlag = true; bestFiveCards.add(c); }
                if (!fiveFlag && c.getValue() == Value.FIVE) {fiveFlag = true; bestFiveCards.add(c); }
            }
            return bestFiveCards;
        }

        boolean foundBiggestStraight = false;
        int straightCounter = 0;
        Value starterValue = null;
        int skipCount = 0;
        int startingStraightIndex = -1;
        for (int i = 0; i < cards.size() & !foundBiggestStraight; i++) {
            Card c = cards.get(i);
            starterValue = c.getValue();
            startingStraightIndex = i;
            straightCounter = 0;
            skipCount = 0;

            for (int j = 0; j < cards.size() - i; j++) { // loop through remaining cards (j is offset from i)
                //Card ca  = cards.get(i+j); // DEBUG
                //int startVal = Value.getIntValue(starterValue); // DEBUG
                //int compareVal = Value.getIntValue(cards.get(i+j).getValue()) - j; // DEBUG
                if (straightCounter == 5) {
                    foundBiggestStraight = true;
                } else if (Value.getIntValue(cards.get(i + j).getValue()) + j - skipCount == Value.getIntValue(starterValue)) {
                    straightCounter++;
                } else if (Value.getIntValue(cards.get(i + j).getValue()) == Value.getIntValue(cards.get(i + j - 1).getValue())) { // else if (value is equal to previous, then skip and move to next card)
                    skipCount++;
                } else {
                    break;
                }
            }
            if (straightCounter == 5) {
                foundBiggestStraight = true;
            }
        }


        Value previousValue = cards.get(startingStraightIndex).getValue(); // Get the value of the first card of the straight
        bestFiveCards.add(cards.get(startingStraightIndex)); // Add the first card of the straight
        for (int k = 1; k < 5 + skipCount; k++) { // start at the straight start index and then add the next 4 cards
            if (!(previousValue == cards.get(k + startingStraightIndex).getValue())) {
                bestFiveCards.add(cards.get(k + startingStraightIndex));
            }
            previousValue = cards.get(k + startingStraightIndex).getValue(); //update previous value
        }

        cards.removeAll(bestFiveCards); // remove all the cards added to the best hand from the remaining cards
        return bestFiveCards;
    }

    /**
     * Given a list of 7 cards, determine if they can create a straight flush
     * @param cards The list of 7 cards
     * @return True if a straight flush is possible, false if not
     */
    private boolean checkStraightFlush(ArrayList<Card> cards) {
        if (!checkFlush(cards)) {return false;} // if a flush cannot happen, stop here
        if (!checkStraight(cards)) {return false;} // if a straight cannot happen from the flush cards, stop here

        cards.sort(new SortByValueDescending()); //sort the list by value
        //Determine the cards that make a flush
        Map<Suit, Integer> suitMap = mapCardsToSuit(cards);
        Suit flushSuit = null;
        for (Suit s : suitMap.keySet()) {
            if (suitMap.get(s) >= 5) {
                flushSuit = s;
            }
        }
        ArrayList<Card> flushCards = new ArrayList<>();
        for (Card c : cards) {
            if (c.getSuit() == flushSuit) {
                flushCards.add(c);
            }
        }
        flushCards.sort(new SortByValueDescending());

        return checkStraight(flushCards);
    }

    // TODO - implemented (requires testing)
    /**
     * If the best hand is a straight flush, determine which 5 cards make the best hand
     * @param cards The list of 7 cards
     * @return The 5 cards to make the best possible straight flush hand
     */
    private ArrayList<Card> straightFlushBestHand(ArrayList<Card> cards) {
        // If the player's hand is a straight flush, create the best hand

        cards.sort(new SortByValueDescending()); //sort the list by value
        //Determine the cards that make a flush
        Map<Suit, Integer> suitMap = mapCardsToSuit(cards);
        Suit flushSuit = null;
        for (Suit s : suitMap.keySet()) {
            if (suitMap.get(s) >= 5) {
                flushSuit = s;
            }
        }
        ArrayList<Card> flushCards = new ArrayList<>();
        for (Card c : cards) {
            if (c.getSuit() == flushSuit) {
                flushCards.add(c);
            }
        }
        flushCards.sort(new SortByValueDescending());

        ArrayList<Card> bestFiveCards = straightBestHand(flushCards);
        cards.removeAll(bestFiveCards); // remove all the cards added to the best hand from the remaining cards

        return bestFiveCards;
    }

    /**
     * Given a list of 7 cards, determine if they can create a royal flush
     * @param cards The list of 7 cards
     * @return True if a royal flush is possible, false if not
     */
    private boolean checkRoyalFlush(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = mapCardsToValue(cards); // possibly not needed at all
        if (!checkFlush(cards)) { return false; } // immediately return false if a flush is not possible
        ArrayList<Card> flushBestHand = flushBestHand(cards); // the best flush hand is a royal flush (always)
        if (flushBestHand.get(0).getValue() == Value.ACE && // these cards are only in the map's keys if they are in the list of cards
                flushBestHand.get(1).getValue() == Value.KING &&
                flushBestHand.get(2).getValue() == Value.QUEEN &&
                flushBestHand.get(3).getValue() == Value.JACK &&
                flushBestHand.get(4).getValue() == Value.TEN) {
            return true;
        }
        return false;
    }

    /**
     * If the best hand is a royal flush, determine which 5 cards make the best hand
     * @param cards The list of 7 cards
     * @return The 5 cards to make the best possible royal flush hand
     */
    private ArrayList<Card> royalFlushBestHand(ArrayList<Card> cards) {
        return flushBestHand(cards); // the best flush hand is a royal flush (always);
    }

    /**
     * Anonymous class, also comparator for sorting from low value to high
     */
    class SortByValueAscending implements Comparator<Card> {
        @Override
        public int compare(Card a, Card b) {
            return Value.getIntValue(a.getValue()) - Value.getIntValue(b.getValue());
        }
    }

    /**
     * Anonymous class, also comparator for sorting from high value to low
     */
    class SortByValueDescending implements Comparator<Card> {
        @Override
        public int compare(Card a, Card b) {
            return Value.getIntValue(b.getValue()) - Value.getIntValue(a.getValue());
        }
    }

    /**
     * Anonymous class, also comparator for sorting players by their best hand
     */
    class SortPlayersByBestHand implements Comparator<Player> {
        @Override
        public int compare(Player a, Player b) {
            if (a.getHandRanking() != b.getHandRanking()) { // if the hand rankings are different, return the better one
                return HandRanking.getRankValue(b.getHandRanking()) - HandRanking.getRankValue(a.getHandRanking());
            }
            // The hand rankings are the same
            HandRanking ranking = a.getHandRanking();
            switch (ranking) {
                case STRAIGHT:
                case STRAIGHT_FLUSH:
                    boolean player1Wheel = isAWheel(a.getBestPossibleHand());
                    boolean player2Wheel = isAWheel(b.getBestPossibleHand());
                    if (player1Wheel & player2Wheel) {return 0;} //both players have a wheel
                    if (player1Wheel & !player2Wheel) {return 1;} //player 1 has a wheel
                    if (player2Wheel & !player1Wheel) {return -1;} //player 2 has a wheel
                    // fall through if no one has a wheel
                    
                case HIGH_CARD:
                case ONE_PAIR:
                case TWO_PAIR:
                case TRIPS:
                case QUADS:
                case FLUSH:
                case FULL_HOUSE:
                    for (int i = 0; i < 5; i++) {
                        Card player1Card = a.getBestPossibleHand().get(i);
                        Card player2Card = b.getBestPossibleHand().get(i);
                        if (player1Card.getValue() != player2Card.getValue()) {return Value.getIntValue(player2Card.getValue()) - Value.getIntValue(player1Card.getValue()); }
                    }
                    break;

                case ROYAL_FLUSH:
                    // The hands are equal (CHOP)
                    break;

                default:
                    // ERROR
                    break;
            }

            // The hands are exactly equal (in value, suit does not matter)
            return 0;
        }
    }

}
