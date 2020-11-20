package equity.HandTypes;

import equity.sorting.SortByValueDescending;
import model.Card;
import model.Value;

import java.util.ArrayList;
import java.util.Map;

import static equity.mapping.MapCardsToValue.mapCardsToValue;

public class Straight extends HandType {

    /**
     * Given a list of 7 cards, determine if they can create a straight
     * @param cards The list of 7 cards
     * @return True if a straight is possible, false if not
     */
    public static boolean checkHand(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = mapCardsToValue(cards); //may not be needed
        cards.sort(new SortByValueDescending()); //sort the cards by value (ace is high)

        int straightCounter = 0;
        Value starterValue = null;
        int skipCount = 0; //used to keep track of repeated straight cards (skip an index when comparing values in the array)
        for (int i = 0; i < cards.size(); i++) {
            Card c = cards.get(i);
            starterValue = c.getValue();
            straightCounter = 0;
            skipCount = 0;

            for (int j = 0; j < cards.size() - i; j++) { // loop through remaining cards (j is offset from i)
                Card ca  = cards.get(i+j); // DEBUG
                int startVal = Value.getIntValue(starterValue); // DEBUG
                int compareVal = Value.getIntValue(cards.get(i+j).getValue()) + j; // DEBUG
                if (straightCounter == 5) {
                    return true;
                } else if (Value.getIntValue(cards.get(i + j).getValue()) + j - skipCount == Value.getIntValue(starterValue)) {
                    straightCounter++;
                } else if (Value.getIntValue(cards.get(i + j).getValue()) == Value.getIntValue(cards.get(i + j - 1).getValue())) { // else if (value is equal to previous, then skip and move to next card)
                    skipCount++;
                } else {
                    break;
                }
            }
            if (straightCounter == 5) { // catches the case where the final card of the straight is the final card of the array
                return true;
            }
        }

        //check for wheel, since ACE can be high or low
        return isAWheel(cards);
    }

    /**
     * Helper function for straights to determine if the straight is a wheel
     * @param cards A list of 7 cards
     * @return True if a wheel can be made from the 7 cards
     */
    public static boolean isAWheel(ArrayList<Card> cards) {
        Map<Value, Integer> valueMap = mapCardsToValue(cards); //may not be needed
        if (valueMap.containsKey(Value.ACE) &&
                valueMap.containsKey(Value.TWO) &&
                valueMap.containsKey(Value.THREE) &&
                valueMap.containsKey(Value.FOUR) &&
                valueMap.containsKey(Value.FIVE))
        {return true;}
        return false;
    }

    /**
     * If the best hand is a straight, determine which 5 cards make the best hand
     * @param cards The list of 7 cards
     * @return The 5 cards to make the best possible straight hand
     */
    public static ArrayList<Card> bestHand(ArrayList<Card> cards) {
        // If the player's hand is a straight, create the best hand

        Map<Value, Integer> valueMap = mapCardsToValue(cards); //may not be needed
        cards.sort(new SortByValueDescending()); //sort the cards by value (ace is high)
        ArrayList<Card> bestFiveCards = new ArrayList<>(); //create array to hold the final hand

        //Start with a wheel so there is no index out of bounds error later
        if (isAWheel(cards)) {
            boolean aceFlag = false, twoFlag = false, threeFlag = false, fourFlag = false, fiveFlag = false;
            for (Card c : cards) {
                if (!aceFlag && c.getValue() == Value.ACE) {aceFlag = true; bestFiveCards.add(c); }
                if (!twoFlag && c.getValue() == Value.TWO) {twoFlag = true; bestFiveCards.add(c); }
                if (!threeFlag && c.getValue() == Value.THREE) {threeFlag = true; bestFiveCards.add(c); }
                if (!fourFlag && c.getValue() == Value.FOUR) {fourFlag = true; bestFiveCards.add(c); }
                if (!fiveFlag && c.getValue() == Value.FIVE) {fiveFlag = true; bestFiveCards.add(c); }
            }
            return bestFiveCards;
        }

        boolean foundBiggestStraight = false;
        int straightCounter = 0;
        Value starterValue = null;
        int skipCount = 0;
        int startingStraightIndex = -1;
        for (int i = 0; i < cards.size() & !foundBiggestStraight; i++) {
            Card c = cards.get(i);
            starterValue = c.getValue();
            startingStraightIndex = i;
            straightCounter = 0;
            skipCount = 0;

            for (int j = 0; j < cards.size() - i; j++) { // loop through remaining cards (j is offset from i)
                //Card ca  = cards.get(i+j); // DEBUG
                //int startVal = Value.getIntValue(starterValue); // DEBUG
                //int compareVal = Value.getIntValue(cards.get(i+j).getValue()) - j; // DEBUG
                if (straightCounter == 5) {
                    foundBiggestStraight = true;
                } else if (Value.getIntValue(cards.get(i + j).getValue()) + j - skipCount == Value.getIntValue(starterValue)) {
                    straightCounter++;
                } else if (Value.getIntValue(cards.get(i + j).getValue()) == Value.getIntValue(cards.get(i + j - 1).getValue())) { // else if (value is equal to previous, then skip and move to next card)
                    skipCount++;
                } else {
                    break;
                }
            }
            if (straightCounter == 5) {
                foundBiggestStraight = true;
            }
        }


        Value previousValue = cards.get(startingStraightIndex).getValue(); // Get the value of the first card of the straight
        bestFiveCards.add(cards.get(startingStraightIndex)); // Add the first card of the straight
        for (int k = 1; k < 5 + skipCount; k++) { // start at the straight start index and then add the next 4 cards
            if (!(previousValue == cards.get(k + startingStraightIndex).getValue())) {
                bestFiveCards.add(cards.get(k + startingStraightIndex));
            }
            previousValue = cards.get(k + startingStraightIndex).getValue(); //update previous value
        }

        cards.removeAll(bestFiveCards); // remove all the cards added to the best hand from the remaining cards
        return bestFiveCards;
    }
}
