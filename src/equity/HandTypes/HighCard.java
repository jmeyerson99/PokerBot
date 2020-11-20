package equity.HandTypes;

import equity.sorting.SortByValueDescending;
import model.Card;
import model.Value;

import java.util.ArrayList;
import java.util.Map;

import static equity.mapping.MapCardsToValue.mapCardsToValue;

public class HighCard extends HandType {
    
    public static boolean checkHand(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = mapCardsToValue(cards);
        cards.sort(new SortByValueDescending()); //sort the cards by value (ace is high)

        // ensure each card value appears only once
        for (Value v : valueMap.keySet()) {
            if (valueMap.get(v) != 1) {
                return false;
            }
        }

        // ensure there is no straight or flush or straight flush
        return !Straight.checkHand(cards) && !Flush.checkHand(cards) && !StraightFlush.checkHand(cards);
    }

    /**
     * If the best hand is high card, determine which 5 cards make the best hand
     * @param cards The list of 7 cards
     * @return The 5 cards to make the best possible high card hand
     */
    public static ArrayList<Card> bestHand(ArrayList<Card> cards) {
        cards.sort(new SortByValueDescending()); //sort the cards by value, high to low (ace is high)
        ArrayList<Card> bestFiveCards = new ArrayList<>(); //create array to hold the final hand

        while (bestFiveCards.size() < 5) {
            bestFiveCards.add(cards.get(0));
            cards.remove(0);
        }
        return bestFiveCards;
    }
}
