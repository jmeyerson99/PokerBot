package equity.HandTypes;

import equity.sorting.SortByValueDescending;
import model.Card;
import model.Suit;

import java.util.ArrayList;
import java.util.Map;

import static equity.mapping.MapCardsToSuit.mapCardsToSuit;

public class Flush extends HandType {

    /**
     * Given a list of 7 cards, determine if they can create a flush
     * @param cards The list of 7 cards
     * @return True if a flush is possible, false if not
     */
    public static boolean checkHand(ArrayList<Card> cards) {
        Map<Suit, Integer> suitMap = mapCardsToSuit(cards);
        cards.sort(new SortByValueDescending()); //sort the cards by value (ace is high)

        for (Suit s : suitMap.keySet()) {
            if (suitMap.get(s) >= 5) {
                return true;
            }
        }
        return false;
    }

    /**
     * If a flush is possible in 'cards', determine which 5 cards make the best flush
     * @param cards The list of 7 cards
     * @return The 5 cards to make the best possible flush hand
     */
    public static ArrayList<Card> bestHand(ArrayList<Card> cards) {
        Map<Suit, Integer> suitMap = mapCardsToSuit(cards);
        cards.sort(new SortByValueDescending()); //sort the cards by value (ace is high)
        ArrayList<Card> bestFiveCards = new ArrayList<>(); //create array to hold the final hand
        Suit flushSuit = null; //does not matter what this is initialized to
        for (Suit s : suitMap.keySet()) {
            if (suitMap.get(s) >= 5) {
                flushSuit = s;
            }
        }

        for (Card c : cards) {
            if (c.getSuit() == flushSuit && bestFiveCards.size() < 5) {
                bestFiveCards.add(c);
            }
        }
        return bestFiveCards;
    }

}
