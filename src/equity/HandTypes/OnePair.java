package equity.HandTypes;

import equity.sorting.SortByValueDescending;
import model.Card;
import model.Value;

import java.util.ArrayList;
import java.util.Map;

import static equity.mapping.MapCardsToValue.mapCardsToValue;

public class OnePair extends HandType {

    /**
     * Given a list of 7 cards, determine if they can create a pair
     * @param cards The list of 7 cards
     * @return True if a pair is possible, false if not
     */
    public static boolean checkHand(ArrayList<Card> cards) {
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
    public static ArrayList<Card> bestHand(ArrayList<Card> cards) {
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
}
