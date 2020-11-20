package equity.HandTypes;

import equity.sorting.SortByValueDescending;
import model.Card;
import model.Value;

import java.util.ArrayList;
import java.util.Map;

import static equity.mapping.MapCardsToValue.mapCardsToValue;

public class TwoPair extends HandType {

    /**
     * Given a list of 7 cards, determine if they can create 2 pair
     * @param cards The list of 7 cards
     * @return True if 2 pair is possible, false if not
     */
    public static boolean checkHand(ArrayList<Card> cards) {
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
    public static ArrayList<Card> bestHand(ArrayList<Card> cards) {
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
}
