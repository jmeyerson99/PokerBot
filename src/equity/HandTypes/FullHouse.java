package equity.HandTypes;

import equity.sorting.SortByValueDescending;
import model.Card;
import model.Value;

import java.util.ArrayList;
import java.util.Map;

import static equity.mapping.MapCardsToValue.mapCardsToValue;

public class FullHouse extends HandType {

    /**
     * Given a list of 7 cards, determine if they can create a full house
     * @param cards The list of 7 cards
     * @return True if a full house is possible, false if not
     */
    public static boolean checkHand(ArrayList<Card> cards) {
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
    public static ArrayList<Card> bestHand(ArrayList<Card> cards) {
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
}
