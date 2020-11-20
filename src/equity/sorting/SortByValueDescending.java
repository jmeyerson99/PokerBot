package equity.sorting;

import model.Card;
import model.Value;

import java.util.Comparator;

/**
 * Comparator for sorting from high value to low
 */
public class SortByValueDescending implements Comparator<Card> {

    @Override
    public int compare(Card a, Card b) {
        return Value.getIntValue(b.getValue()) - Value.getIntValue(a.getValue());
    }
}
