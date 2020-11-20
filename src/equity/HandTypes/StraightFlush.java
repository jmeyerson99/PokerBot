package equity.HandTypes;

import equity.sorting.SortByValueDescending;
import model.Card;
import model.Suit;

import java.util.ArrayList;
import java.util.Map;

import static equity.mapping.MapCardsToSuit.mapCardsToSuit;

public class StraightFlush extends HandType {

    /**
     * Given a list of 7 cards, determine if they can create a straight flush
     * @param cards The list of 7 cards
     * @return True if a straight flush is possible, false if not
     */
    public static boolean checkHand(ArrayList<Card> cards) {
        if (!Flush.checkHand(cards)) {return false;} // if a flush cannot happen, stop here
        if (!Straight.checkHand(cards)) {return false;} // if a straight cannot happen from the flush cards, stop here

        cards.sort(new SortByValueDescending()); //sort the list by value
        //Determine the cards that make a flush
        Map<Suit, Integer> suitMap = mapCardsToSuit(cards);
        Suit flushSuit = null;
        for (Suit s : suitMap.keySet()) {
            if (suitMap.get(s) >= 5) {
                flushSuit = s;
            }
        }
        ArrayList<Card> flushCards = new ArrayList<>();
        for (Card c : cards) {
            if (c.getSuit() == flushSuit) {
                flushCards.add(c);
            }
        }
        flushCards.sort(new SortByValueDescending());

        return Straight.checkHand(flushCards);
    }

    /**
     * If the best hand is a straight flush, determine which 5 cards make the best hand
     * @param cards The list of 7 cards
     * @return The 5 cards to make the best possible straight flush hand
     */
    public static ArrayList<Card> bestHand(ArrayList<Card> cards) {
        // If the player's hand is a straight flush, create the best hand

        cards.sort(new SortByValueDescending()); //sort the list by value
        //Determine the cards that make a flush
        Map<Suit, Integer> suitMap = mapCardsToSuit(cards);
        Suit flushSuit = null;
        for (Suit s : suitMap.keySet()) {
            if (suitMap.get(s) >= 5) {
                flushSuit = s;
            }
        }
        ArrayList<Card> flushCards = new ArrayList<>();
        for (Card c : cards) {
            if (c.getSuit() == flushSuit) {
                flushCards.add(c);
            }
        }
        flushCards.sort(new SortByValueDescending());

        ArrayList<Card> bestFiveCards = Straight.bestHand(flushCards);
        cards.removeAll(bestFiveCards); // remove all the cards added to the best hand from the remaining cards

        return bestFiveCards;
    }
}
