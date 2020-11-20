package equity.mapping;

import equity.sorting.SortByValueDescending;
import model.Card;
import model.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapCardsToValue {

    /**
     * Creates a map that holds a count of the numerical frequency of the list of cards (by value)
     * @param cards The list of 7 cards
     * @return The map
     */
    public static Map<Value, Integer> mapCardsToValue(ArrayList<Card> cards) {
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
}
