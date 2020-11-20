package equity.mapping;

import equity.sorting.SortByValueDescending;
import model.Card;
import model.Suit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapCardsToSuit {

    /**
     * Creates a map that holds a count of the suit frequency of the list of cards
     * @param cards The list of 7 cards
     * @return The map
     */
    public static Map<Suit, Integer> mapCardsToSuit(ArrayList<Card> cards) {
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
}
