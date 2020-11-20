package equity.sorting;

import model.Card;
import model.HandRanking;
import model.Player;
import model.Value;

import java.util.Comparator;

import static equity.HandTypes.Straight.isAWheel;

/**
 * Comparator for sorting players by their best hand
 */
public class SortPlayersByBestHand implements Comparator<Player> {

    @Override
    public int compare(Player a, Player b) {
        if (a.getHandRanking() != b.getHandRanking()) { // if the hand rankings are different, return the better one
            return HandRanking.getRankValue(b.getHandRanking()) - HandRanking.getRankValue(a.getHandRanking());
        }
        // The hand rankings are the same
        HandRanking ranking = a.getHandRanking();
        switch (ranking) {
            case STRAIGHT:
            case STRAIGHT_FLUSH:
                boolean player1Wheel = isAWheel(a.getBestPossibleHand());
                boolean player2Wheel = isAWheel(b.getBestPossibleHand());
                if (player1Wheel & player2Wheel) {return 0;} //both players have a wheel
                if (player1Wheel & !player2Wheel) {return 1;} //player 1 has a wheel
                if (player2Wheel & !player1Wheel) {return -1;} //player 2 has a wheel
                // fall through if no one has a wheel

            case HIGH_CARD:
            case ONE_PAIR:
            case TWO_PAIR:
            case TRIPS:
            case QUADS:
            case FLUSH:
            case FULL_HOUSE:
                for (int i = 0; i < 5; i++) {
                    Card player1Card = a.getBestPossibleHand().get(i);
                    Card player2Card = b.getBestPossibleHand().get(i);
                    if (player1Card.getValue() != player2Card.getValue()) {return Value.getIntValue(player2Card.getValue()) - Value.getIntValue(player1Card.getValue()); }
                }
                break;

            case ROYAL_FLUSH:
                // The hands are equal (CHOP)
                break;

            default:
                // ERROR
                break;
        }

        // The hands are exactly equal (in value, suit does not matter)
        return 0;
    }
}
