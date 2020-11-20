package equity.HandTypes;

import equity.sorting.SortByValueDescending;
import model.Card;
import model.Value;

import java.util.ArrayList;
import java.util.Map;

import static equity.mapping.MapCardsToValue.mapCardsToValue;

public class Quads extends HandType {

    /**
     * Given a list of 7 cards, determine if they can create quads
     * @param cards The list of 7 cards
     * @return True if quads is possible, false if not
     */
    public static boolean checkHand(ArrayList<Card> cards) {
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
    public static ArrayList<Card> bestHand(ArrayList<Card> cards) {
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
}
