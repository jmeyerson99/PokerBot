package equity.HandTypes;

import equity.sorting.SortByValueDescending;
import model.Card;
import model.Value;

import java.util.ArrayList;
import java.util.Map;

import static equity.mapping.MapCardsToValue.mapCardsToValue;

public class Trips extends HandType {

    /**
     * Given a list of 7 cards, determine if they can create trips
     * @param cards The list of 7 cards
     * @return True if trips is possible, false if not
     */
    public static boolean checkHand(ArrayList<Card> cards) {
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
    public static ArrayList<Card> bestHand(ArrayList<Card> cards) {
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
}
