package equity.HandTypes;

import model.Card;
import model.Value;

import java.util.ArrayList;
import java.util.Map;

import static equity.mapping.MapCardsToValue.mapCardsToValue;

public class RoyalFlush extends HandType {

    /**
     * Given a list of 7 cards, determine if they can create a royal flush
     * @param cards The list of 7 cards
     * @return True if a royal flush is possible, false if not
     */
    public static boolean checkHand(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = mapCardsToValue(cards); // possibly not needed at all
        if (!Flush.checkHand(cards)) { return false; } // immediately return false if a flush is not possible
        ArrayList<Card> flushBestHand = Flush.bestHand(cards); // the best flush hand is a royal flush (always)
        if (flushBestHand.get(0).getValue() == Value.ACE && // these cards are only in the map's keys if they are in the list of cards
                flushBestHand.get(1).getValue() == Value.KING &&
                flushBestHand.get(2).getValue() == Value.QUEEN &&
                flushBestHand.get(3).getValue() == Value.JACK &&
                flushBestHand.get(4).getValue() == Value.TEN) {
            return true;
        }
        return false;
    }

    /**
     * If the best hand is a royal flush, determine which 5 cards make the best hand
     * @param cards The list of 7 cards
     * @return The 5 cards to make the best possible royal flush hand
     */
    public static ArrayList<Card> bestHand(ArrayList<Card> cards) {
        return Flush.bestHand(cards); // the best flush hand is a royal flush (always);
    }
}
