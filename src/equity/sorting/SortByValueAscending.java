package equity.sorting;

import model.Card;
import model.Value;

import java.util.Comparator;

/**
 * Comparator for sorting from low value to high
 */
public class SortByValueAscending implements Comparator<Card> {

    @Override
    public int compare(Card a, Card b) {
        return Value.getIntValue(a.getValue()) - Value.getIntValue(b.getValue());
    }
}
