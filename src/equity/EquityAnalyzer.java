package equity;

import equity.HandTypes.*;
import equity.sorting.SortPlayersByBestHand;
import model.*;

import java.text.DecimalFormat;
import java.util.*;

import static equity.HandTypes.Straight.isAWheel;

public class EquityAnalyzer {

    private Table table;

    public DecimalFormat df = new DecimalFormat("###.##");

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
                //determinePreFlopEquity(); // TODO - requires implementation but takes 37 minutes to execute and fails
                break;

            // Who is leading after the flop?
            case 3:
                determineFlopEquity();
                break;

            // Who is leading after the turn?
            case 4:
                determineTurnEquity();
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

    private void determineTurnEquity() {
        Map<Player, Integer> timesWon = new HashMap<>();
        for (Player p : this.table.getPlayers()) {
            timesWon.put(p, 0);
        }
        int handsSimulated = 0;
        for (Card c : this.table.getDeck().copyDeck()) {
            this.table.getBoard().setRiver(c);
            // determine equities
            Map<Integer, ArrayList<Player>> results = mapOutFinalRankings();
            ArrayList<Player> firstPlacePlayers = results.get(1);
            for (Player p : firstPlacePlayers) {
                int k = timesWon.get(p) + 1;
                timesWon.put(p, k);
            }
            handsSimulated++;
            this.table.getBoard().removeRiver();
        }
        Map<Player, Double> percentageWon = new HashMap<>();
        for (Player p : timesWon.keySet()) {
            percentageWon.put(p, ((double) timesWon.get(p) / (double) handsSimulated));
        }

        System.out.println("Equity going into the river: ");
        for (Player p : percentageWon.keySet()) {
            System.out.println("Player " + p.getName() + " odds are: " + df.format((percentageWon.get(p) * 100)) + "%");
        }
    }

    private void determineFlopEquity() {
        Map<Player, Integer> timesWon = new HashMap<>();
        for (Player p : this.table.getPlayers()) {
            timesWon.put(p, 0);
        }
        int handsSimulated = 0;
        ArrayList<Card> remainingCards = this.table.getDeck().copyDeck();
        // Note- ORDER MATTERS
        for (Card c : remainingCards) {
            for (Card cc : remainingCards) {
                // Skip the same card being placed twice
                if (c != cc) {
                    this.table.getBoard().setTurn(c);
                    this.table.getBoard().setRiver(cc);
                    // determine equities
                    Map<Integer, ArrayList<Player>> results = mapOutFinalRankings();
                    ArrayList<Player> firstPlacePlayers = results.get(1);
                    for (Player p : firstPlacePlayers) {
                        int k = timesWon.get(p) + 1;
                        timesWon.put(p, k);
                    }
                    handsSimulated++;
                    this.table.getBoard().removeTurn();
                    this.table.getBoard().removeRiver();
                }
            }
        }
        Map<Player, Double> percentageWon = new HashMap<>();
        for (Player p : timesWon.keySet()) {
            percentageWon.put(p, ((double) timesWon.get(p) / (double) handsSimulated));
        }

        System.out.println("Equity going into the turn: ");
        for (Player p : percentageWon.keySet()) {
            System.out.println("Player " + p.getName() + " odds are: " + df.format((percentageWon.get(p) * 100)) + "%");
        }
    }

    // TODO - does not work, takes 37 minutes to run
    private void determinePreFlopEquity() {
        Map<Player, Integer> timesWon = new HashMap<>();
        for (Player p : this.table.getPlayers()) {
            timesWon.put(p, 0);
        }
        int handsSimulated = 0;
        ArrayList<Card> remainingCards = this.table.getDeck().copyDeck();
        // Note- ORDER MATTERS
        for (Card c : remainingCards) {
            for (Card cc : remainingCards) {
                for (Card ccc : remainingCards) {
                    for (Card cccc : remainingCards) {
                        for (Card ccccc : remainingCards) {
                            if (noRepeatedCards(c, cc, ccc, cccc, ccccc)) {
                                this.table.getBoard().setFlop(c, cc, ccc);
                                this.table.getBoard().setTurn(cccc);
                                this.table.getBoard().setRiver(ccccc);
                                // determine equities
                                Map<Integer, ArrayList<Player>> results = mapOutFinalRankings();
                                ArrayList<Player> firstPlacePlayers = results.get(1);
                                for (Player p : firstPlacePlayers) {
                                    int k = timesWon.get(p) + 1;
                                    timesWon.put(p, k);
                                }
                                handsSimulated++;
                                this.table.getBoard().removeFlop();
                                this.table.getBoard().removeTurn();
                                this.table.getBoard().removeRiver();
                            }
                        }
                    }
                }
            }
        }
        Map<Player, Double> percentageWon = new HashMap<>();
        for (Player p : timesWon.keySet()) {
            percentageWon.put(p, ((double) timesWon.get(p) / (double) handsSimulated));
        }

        System.out.println("Equity going into the flop: ");
        for (Player p : percentageWon.keySet()) {
            System.out.println("Player " + p.getName() + " odds are: " + df.format((percentageWon.get(p) * 100)) + "%");
        }
    }

    private boolean noRepeatedCards(Card c1, Card c2, Card c3, Card c4, Card c5) {
        Set<Card> set = new HashSet<>();
        set.add(c1);
        set.add(c2);
        set.add(c3);
        set.add(c4);
        set.add(c5);
        return 5 == set.size();
    }


    private Map<Integer, ArrayList<Player>> mapOutFinalRankings() {
        ArrayList<Player> playerRankings = new ArrayList<>();
        playerRankings.addAll(this.table.getPlayers());

        // determine each player's best hand
        for (Player p : this.table.getPlayers()) {
            getBestHand(table.getBoard(), p);
        }

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
            chops = new ArrayList<>(); // TODO - chops.clear() doesn't work (get all 0's for %s, for (Player p : chops) { chops.remove(p); } doesn't work either, ConcurrentModificationException
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
     * Get the best hand a player can make, given a board, and the player. This method returns the best 5 card
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
