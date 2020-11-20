package equity;

import equity.HandTypes.*;
import equity.sorting.SortPlayersByBestHand;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static equity.HandTypes.Straight.isAWheel;

// TODO - consider making an Equity Analyzer package, each group of 2 function (checkPair and pairBestHand) get put together in a class (NEXT TASK!!!!!!!)
public class EquityAnalyzer {

    private Table table;

    public EquityAnalyzer(Table table) {
        this.table = table;
    }

    /**
     * Determine each player's equity in the hand, based on the number of cards dealt so far
     */
    public void determineEquity() {
        Board board = table.getBoard();
        switch (board.getSize()) {
            // Who is leading pre flop?
            case 0:
                break;

            // Who is leading after the flop?
            case 3:
                break;

            // Who is leading after the turn?
            case 4:
                break;

            // Who won?
            case 5:
                determineShowdownWinner();
                break;

            default:
                // ERROR (invalid size)
                break;
        }
    }

    private Map<Integer, ArrayList<Player>> mapOutFinalRankings() {
        ArrayList<Player> playerRankings = new ArrayList<>();
        playerRankings.addAll(this.table.getPlayers());
        playerRankings.sort(new SortPlayersByBestHand());

        int ranking = 1;
        int numChops = 0;
        ArrayList<Player> chops = new ArrayList<>();
        Map<Integer, ArrayList<Player>> rankings = new HashMap<>();
        for (int i = 0; i < playerRankings.size(); i = i + 1 + numChops) {
            chops.add(playerRankings.get(i));
            numChops = 0;
            for (int j = i + 1; j < playerRankings.size(); j++) {
                if (0 == compareBestHands(playerRankings.get(i), playerRankings.get(j))) {
                    // CHOP
                    chops.add(playerRankings.get(j));
                    numChops++;
                }
            }
            rankings.put(ranking, chops);

            ranking++;
            chops = new ArrayList<>(); // TODO - find better way to not waste memory (although Java cleans up for me)
        }

        return rankings;
    }

    private int compareBestHands(Player a, Player b) {
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

    /**
     * Given a player, determine the best hand that can be made with the board and the player's hole cards
     * @param p The player
     */
    public HandRanking determineBestPossibleHand(Player p) {
        ArrayList<Card> allCards = new ArrayList<>();
        allCards.add(p.getHand().getCard1());
        allCards.add(p.getHand().getCard2());
        allCards.addAll(table.getBoard().getBoard()); //TODO not great, may want to fix later

        boolean royalFlush = RoyalFlush.checkHand(allCards);
        if (royalFlush) {
            return HandRanking.ROYAL_FLUSH;
        }
        boolean straightFlush = StraightFlush.checkHand(allCards);
        if (straightFlush) {
            return HandRanking.STRAIGHT_FLUSH;
        }
        boolean quads = Quads.checkHand(allCards);
        if (quads) {
            return HandRanking.QUADS;
        }
        boolean fullHouse = FullHouse.checkHand(allCards);
        if (fullHouse) {
            return HandRanking.FULL_HOUSE;
        }
        boolean flush = Flush.checkHand(allCards);
        if (flush) {
            return HandRanking.FLUSH;
        }
        boolean straight = Straight.checkHand(allCards);
        if (straight) {
            return HandRanking.STRAIGHT;
        }
        boolean trips = Trips.checkHand(allCards);
        if (trips) {
            return HandRanking.TRIPS;
        }
        boolean twoPair = TwoPair.checkHand(allCards);
        if (twoPair) {
            return HandRanking.TWO_PAIR;
        }
        boolean pair = OnePair.checkHand(allCards);
        if (pair) {
            return HandRanking.ONE_PAIR;
        }
        boolean highCard = HighCard.checkHand(allCards);
        if (highCard) {
            return HandRanking.HIGH_CARD;
        }
        // ERROR
        return null;
    }

    /**
     * Determine the ranking of players in the given hand
     * TODO - return the player rankings?
     */
    private void determineShowdownWinner() {
        for (Player p : this.table.getPlayers()) {
            ArrayList<Card> bestHand = getBestHand(this.table.getBoard(), p);
            System.out.println("Player " + p.getName() + " has " + p.getHandRanking() + ": " + bestHand);
        }

        ArrayList<Player> playerRankings = new ArrayList<>();
        playerRankings.addAll(this.table.getPlayers());
        playerRankings.sort(new SortPlayersByBestHand());
        System.out.println("Official Rankings (no chop code): ");
        for (Player p : playerRankings) {
            System.out.println(p.getName());
        }

        Map<Integer, ArrayList<Player>> finalRanks = mapOutFinalRankings();
        System.out.println("Official Rankings (with chop code): ");
        int rank = 1;
        while (finalRanks.containsKey(rank)) {
            System.out.print(rank + ": ");
            for (Player p : finalRanks.get(rank)) {
                System.out.print(p.getName() + ", ");
            }
            System.out.println();
            rank++;
        }
    }

    /**
     * Determine the best hand a player can make, given a board, and the player. This method returns the best 5 card
     * hand, and sets that hand in the Player field.
     * @param board The board
     * @param p The player (allows access to their hole cards)
     */
    public ArrayList<Card> getBestHand(Board board, Player p) {
        ArrayList<Card> allCards = new ArrayList<>();
        allCards.add(p.getHand().getCard1());
        allCards.add(p.getHand().getCard2());
        allCards.addAll(board.getBoard());

        ArrayList<Card> bestHand;

        boolean royalFlush = RoyalFlush.checkHand(allCards);
        if (royalFlush) {
            p.setHandRanking(HandRanking.ROYAL_FLUSH);
            bestHand = RoyalFlush.bestHand(allCards);
            p.setBestPossibleHand(bestHand);
            return bestHand;
        }
        boolean straightFlush = StraightFlush.checkHand(allCards);
        if (straightFlush) {
            p.setHandRanking(HandRanking.STRAIGHT_FLUSH);
            bestHand = StraightFlush.bestHand(allCards);
            p.setBestPossibleHand(bestHand);
            return bestHand;
        }
        boolean quads = Quads.checkHand(allCards);
        if (quads) {
            p.setHandRanking(HandRanking.QUADS);
            bestHand = Quads.bestHand(allCards);
            p.setBestPossibleHand(bestHand);
            return bestHand;
        }
        boolean fullHouse = FullHouse.checkHand(allCards);
        if (fullHouse) {
            p.setHandRanking(HandRanking.FULL_HOUSE);
            bestHand = FullHouse.bestHand(allCards);
            p.setBestPossibleHand(bestHand);
            return bestHand;
        }
        boolean flush = Flush.checkHand(allCards);
        if (flush) {
            p.setHandRanking(HandRanking.FLUSH);
            bestHand = Flush.bestHand(allCards);
            p.setBestPossibleHand(bestHand);
            return bestHand;
        }
        boolean straight = Straight.checkHand(allCards);
        if (straight) {
            p.setHandRanking(HandRanking.STRAIGHT);
            bestHand = Straight.bestHand(allCards);
            p.setBestPossibleHand(bestHand);
            return bestHand;
        }
        boolean trips = Trips.checkHand(allCards);
        if (trips) {
            p.setHandRanking(HandRanking.TRIPS);
            bestHand = Trips.bestHand(allCards);
            p.setBestPossibleHand(bestHand);
            return bestHand;
        }
        boolean twoPair = TwoPair.checkHand(allCards);
        if (twoPair) {
            p.setHandRanking(HandRanking.TWO_PAIR);
            bestHand = TwoPair.bestHand(allCards);
            p.setBestPossibleHand(bestHand);
            return bestHand;
        }
        boolean pair = OnePair.checkHand(allCards);
        if (pair) {
            p.setHandRanking(HandRanking.ONE_PAIR);
            bestHand = OnePair.bestHand(allCards);
            p.setBestPossibleHand(bestHand);
            return bestHand;
        }
        boolean highCard = HighCard.checkHand(allCards);
        if (highCard) {
            p.setHandRanking(HandRanking.HIGH_CARD);
            bestHand = HighCard.bestHand(allCards);
            p.setBestPossibleHand(bestHand);
            return bestHand;
        }
        // ERROR
        return null;
    }
}
