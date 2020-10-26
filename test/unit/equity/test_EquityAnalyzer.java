package unit.equity;

import equity.*;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class test_EquityAnalyzer {

    private EquityAnalyzer CuT; // The component-under-test

    private Player testPlayer;
    private Board testBoard;
    private Table testTable;

    @BeforeEach
    /**
     * TODO - find a way to not create a new table literally every time (change tag or setup() name)
     */
    public void initialSetup() {
        testTable = new Table(1);
        testPlayer = testTable.getPlayers().get(0);
        testBoard = testTable.getBoard();
        CuT = new EquityAnalyzer(testTable);
    }

    @Test
    /**
     * Test that a high card hand is correctly identified, and the best hand is determined correctly.
     */
    public void testHighCardHand() {
        testPlayer.setHand(new Hand(new Card(Value.ACE, Suit.HEART), new Card(Value.THREE, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.JACK, Suit.DIAMOND), new Card(Value.KING, Suit.HEART), new Card(Value.FOUR, Suit.CLUB));
        testBoard.setTurn(new Card(Value.NINE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.TWO, Suit.HEART));
        Assertions.assertEquals(HandRanking.HIGH_CARD, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.ACE, Suit.HEART), new Card(Value.KING, Suit.HEART),
                new Card(Value.JACK, Suit.DIAMOND), new Card(Value.NINE, Suit.CLUB),
                new Card(Value.FOUR, Suit.CLUB)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testPocketPair() {
        testPlayer.setHand(new Hand(new Card(Value.THREE, Suit.HEART), new Card(Value.THREE, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.JACK, Suit.DIAMOND), new Card(Value.KING, Suit.HEART), new Card(Value.FOUR, Suit.CLUB));
        testBoard.setTurn(new Card(Value.NINE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.TWO, Suit.HEART));
        Assertions.assertEquals(HandRanking.ONE_PAIR, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.THREE, Suit.HEART), new Card(Value.THREE, Suit.SPADE),
                new Card(Value.KING, Suit.HEART), new Card(Value.JACK, Suit.DIAMOND),
                new Card(Value.NINE, Suit.CLUB)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testBoardPair() {
        testPlayer.setHand(new Hand(new Card(Value.THREE, Suit.HEART), new Card(Value.ACE, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.JACK, Suit.DIAMOND), new Card(Value.JACK, Suit.HEART), new Card(Value.FOUR, Suit.CLUB));
        testBoard.setTurn(new Card(Value.NINE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.TWO, Suit.HEART));
        Assertions.assertEquals(HandRanking.ONE_PAIR, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.JACK, Suit.DIAMOND), new Card(Value.JACK, Suit.HEART),
                new Card(Value.ACE, Suit.SPADE), new Card(Value.NINE, Suit.CLUB),
                new Card(Value.FOUR, Suit.CLUB)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testOnePair() {
        testPlayer.setHand(new Hand(new Card(Value.TWO, Suit.HEART), new Card(Value.THREE, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.JACK, Suit.DIAMOND), new Card(Value.KING, Suit.HEART), new Card(Value.FOUR, Suit.CLUB));
        testBoard.setTurn(new Card(Value.NINE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.TWO, Suit.SPADE));
        Assertions.assertEquals(HandRanking.ONE_PAIR, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.TWO, Suit.HEART), new Card(Value.TWO, Suit.SPADE),
                new Card(Value.KING, Suit.HEART), new Card(Value.JACK, Suit.DIAMOND),
                new Card(Value.NINE, Suit.CLUB)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testTowPair() {
        testPlayer.setHand(new Hand(new Card(Value.TWO, Suit.HEART), new Card(Value.THREE, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.JACK, Suit.DIAMOND), new Card(Value.KING, Suit.HEART), new Card(Value.FOUR, Suit.CLUB));
        testBoard.setTurn(new Card(Value.THREE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.TWO, Suit.CLUB));
        Assertions.assertEquals(HandRanking.TWO_PAIR, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.THREE, Suit.SPADE), new Card(Value.THREE, Suit.CLUB),
                new Card(Value.TWO, Suit.HEART), new Card(Value.TWO, Suit.CLUB),
                new Card(Value.KING, Suit.HEART)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testTwoPairPocketPair() {
        testPlayer.setHand(new Hand(new Card(Value.TWO, Suit.HEART), new Card(Value.TWO, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.JACK, Suit.DIAMOND), new Card(Value.KING, Suit.HEART), new Card(Value.FOUR, Suit.CLUB));
        testBoard.setTurn(new Card(Value.THREE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.THREE, Suit.HEART));
        Assertions.assertEquals(HandRanking.TWO_PAIR, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.THREE, Suit.CLUB), new Card(Value.THREE, Suit.HEART),
                new Card(Value.TWO, Suit.HEART), new Card(Value.TWO, Suit.SPADE),
                new Card(Value.KING, Suit.HEART)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testTwoPairBoardPair() {
        testPlayer.setHand(new Hand(new Card(Value.QUEEN, Suit.HEART), new Card(Value.KING, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.JACK, Suit.DIAMOND), new Card(Value.JACK, Suit.HEART), new Card(Value.TWO, Suit.CLUB));
        testBoard.setTurn(new Card(Value.THREE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.TWO, Suit.HEART));
        Assertions.assertEquals(HandRanking.TWO_PAIR, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.JACK, Suit.DIAMOND), new Card(Value.JACK, Suit.HEART),
                new Card(Value.TWO, Suit.CLUB), new Card(Value.TWO, Suit.HEART),
                new Card(Value.KING, Suit.SPADE)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testTripsPocketPair() {
        testPlayer.setHand(new Hand(new Card(Value.QUEEN, Suit.HEART), new Card(Value.QUEEN, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.JACK, Suit.DIAMOND), new Card(Value.QUEEN, Suit.CLUB), new Card(Value.ACE, Suit.CLUB));
        testBoard.setTurn(new Card(Value.THREE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.TWO, Suit.HEART));
        Assertions.assertEquals(HandRanking.TRIPS, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.QUEEN, Suit.HEART), new Card(Value.QUEEN, Suit.SPADE),
                new Card(Value.QUEEN, Suit.CLUB), new Card(Value.ACE, Suit.CLUB),
                new Card(Value.JACK, Suit.DIAMOND)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testTripsBoardPair() {
        testPlayer.setHand(new Hand(new Card(Value.QUEEN, Suit.HEART), new Card(Value.KING, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.JACK, Suit.DIAMOND), new Card(Value.QUEEN, Suit.SPADE), new Card(Value.QUEEN, Suit.CLUB));
        testBoard.setTurn(new Card(Value.THREE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.TWO, Suit.HEART));
        Assertions.assertEquals(HandRanking.TRIPS, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.QUEEN, Suit.HEART), new Card(Value.QUEEN, Suit.SPADE),
                new Card(Value.QUEEN, Suit.CLUB), new Card(Value.KING, Suit.SPADE),
                new Card(Value.JACK, Suit.DIAMOND)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testTripsBoardTrips() {
        testPlayer.setHand(new Hand(new Card(Value.FIVE, Suit.HEART), new Card(Value.KING, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.QUEEN, Suit.DIAMOND), new Card(Value.QUEEN, Suit.SPADE), new Card(Value.QUEEN, Suit.CLUB));
        testBoard.setTurn(new Card(Value.THREE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.TWO, Suit.HEART));
        Assertions.assertEquals(HandRanking.TRIPS, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.QUEEN, Suit.DIAMOND), new Card(Value.QUEEN, Suit.SPADE),
                new Card(Value.QUEEN, Suit.CLUB), new Card(Value.KING, Suit.SPADE),
                new Card(Value.FIVE, Suit.HEART)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testQuadsPocketPair() {
        testPlayer.setHand(new Hand(new Card(Value.FIVE, Suit.HEART), new Card(Value.FIVE, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.FIVE, Suit.DIAMOND), new Card(Value.QUEEN, Suit.SPADE), new Card(Value.FIVE, Suit.CLUB));
        testBoard.setTurn(new Card(Value.THREE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.TWO, Suit.HEART));
        Assertions.assertEquals(HandRanking.QUADS, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.FIVE, Suit.HEART), new Card(Value.FIVE, Suit.SPADE),
                new Card(Value.FIVE, Suit.DIAMOND), new Card(Value.FIVE, Suit.CLUB),
                new Card(Value.QUEEN, Suit.SPADE)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testQuadsBoardTrips() {
        testPlayer.setHand(new Hand(new Card(Value.FIVE, Suit.HEART), new Card(Value.FOUR, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.FIVE, Suit.DIAMOND), new Card(Value.FIVE, Suit.SPADE), new Card(Value.FIVE, Suit.CLUB));
        testBoard.setTurn(new Card(Value.THREE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.TWO, Suit.HEART));
        Assertions.assertEquals(HandRanking.QUADS, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.FIVE, Suit.HEART), new Card(Value.FIVE, Suit.DIAMOND),
                new Card(Value.FIVE, Suit.SPADE), new Card(Value.FIVE, Suit.CLUB),
                new Card(Value.FOUR, Suit.SPADE)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testQuadsBoardQuads() {
        testPlayer.setHand(new Hand(new Card(Value.SIX, Suit.HEART), new Card(Value.FOUR, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.FIVE, Suit.DIAMOND), new Card(Value.FIVE, Suit.SPADE), new Card(Value.FIVE, Suit.CLUB));
        testBoard.setTurn(new Card(Value.THREE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.FIVE, Suit.HEART));
        Assertions.assertEquals(HandRanking.QUADS, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.FIVE, Suit.DIAMOND), new Card(Value.FIVE, Suit.SPADE),
                new Card(Value.FIVE, Suit.CLUB), new Card(Value.FIVE, Suit.HEART),
                new Card(Value.SIX, Suit.HEART)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testFullHouseBoard() {
        testPlayer.setHand(new Hand(new Card(Value.SIX, Suit.HEART), new Card(Value.FOUR, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.FIVE, Suit.DIAMOND), new Card(Value.FIVE, Suit.SPADE), new Card(Value.FIVE, Suit.CLUB));
        testBoard.setTurn(new Card(Value.THREE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.THREE, Suit.HEART));
        Assertions.assertEquals(HandRanking.FULL_HOUSE, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.FIVE, Suit.DIAMOND), new Card(Value.FIVE, Suit.SPADE),
                new Card(Value.FIVE, Suit.CLUB), new Card(Value.THREE, Suit.CLUB),
                new Card(Value.THREE, Suit.HEART)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testFullHousePocketPairBoardPair() {
        testPlayer.setHand(new Hand(new Card(Value.SIX, Suit.HEART), new Card(Value.SIX, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.SIX, Suit.DIAMOND), new Card(Value.FIVE, Suit.SPADE), new Card(Value.FIVE, Suit.CLUB));
        testBoard.setTurn(new Card(Value.FOUR, Suit.CLUB));
        testBoard.setRiver(new Card(Value.THREE, Suit.HEART));
        Assertions.assertEquals(HandRanking.FULL_HOUSE, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.SIX, Suit.HEART), new Card(Value.SIX, Suit.SPADE),
                new Card(Value.SIX, Suit.DIAMOND), new Card(Value.FIVE, Suit.SPADE),
                new Card(Value.FIVE, Suit.CLUB)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testFullHousePocketPairBoardTrips() {
        testPlayer.setHand(new Hand(new Card(Value.SIX, Suit.HEART), new Card(Value.SIX, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.FIVE, Suit.DIAMOND), new Card(Value.FIVE, Suit.SPADE), new Card(Value.FIVE, Suit.CLUB));
        testBoard.setTurn(new Card(Value.FOUR, Suit.CLUB));
        testBoard.setRiver(new Card(Value.THREE, Suit.HEART));
        Assertions.assertEquals(HandRanking.FULL_HOUSE, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.FIVE, Suit.DIAMOND), new Card(Value.FIVE, Suit.SPADE),
                new Card(Value.FIVE, Suit.CLUB), new Card(Value.SIX, Suit.HEART),
                new Card(Value.SIX, Suit.SPADE)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testFullHouse() {
        testPlayer.setHand(new Hand(new Card(Value.SIX, Suit.HEART), new Card(Value.FIVE, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.FIVE, Suit.DIAMOND), new Card(Value.SIX, Suit.SPADE), new Card(Value.FIVE, Suit.CLUB));
        testBoard.setTurn(new Card(Value.FOUR, Suit.CLUB));
        testBoard.setRiver(new Card(Value.THREE, Suit.HEART));
        Assertions.assertEquals(HandRanking.FULL_HOUSE, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.FIVE, Suit.SPADE), new Card(Value.FIVE, Suit.DIAMOND),
                new Card(Value.FIVE, Suit.CLUB), new Card(Value.SIX, Suit.HEART),
                new Card(Value.SIX, Suit.SPADE)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testFullHouseTwoPairs() {
        testPlayer.setHand(new Hand(new Card(Value.SIX, Suit.HEART), new Card(Value.FIVE, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.FIVE, Suit.DIAMOND), new Card(Value.SIX, Suit.SPADE), new Card(Value.FIVE, Suit.CLUB));
        testBoard.setTurn(new Card(Value.FOUR, Suit.CLUB));
        testBoard.setRiver(new Card(Value.FOUR, Suit.HEART));
        Assertions.assertEquals(HandRanking.FULL_HOUSE, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.FIVE, Suit.SPADE), new Card(Value.FIVE, Suit.DIAMOND),
                new Card(Value.FIVE, Suit.CLUB), new Card(Value.SIX, Suit.HEART),
                new Card(Value.SIX, Suit.SPADE)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testFullHouseTripsTrips() {
        testPlayer.setHand(new Hand(new Card(Value.FOUR, Suit.HEART), new Card(Value.FIVE, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.FIVE, Suit.DIAMOND), new Card(Value.SIX, Suit.SPADE), new Card(Value.FIVE, Suit.CLUB));
        testBoard.setTurn(new Card(Value.FOUR, Suit.CLUB));
        testBoard.setRiver(new Card(Value.FOUR, Suit.DIAMOND));
        Assertions.assertEquals(HandRanking.FULL_HOUSE, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.FIVE, Suit.SPADE), new Card(Value.FIVE, Suit.DIAMOND),
                new Card(Value.FIVE, Suit.CLUB), new Card(Value.FOUR, Suit.HEART),
                new Card(Value.FOUR, Suit.CLUB)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testStraightOneHoleCard() {
        testPlayer.setHand(new Hand(new Card(Value.SIX, Suit.HEART), new Card(Value.KING, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.FIVE, Suit.DIAMOND), new Card(Value.THREE, Suit.SPADE), new Card(Value.JACK, Suit.CLUB));
        testBoard.setTurn(new Card(Value.FOUR, Suit.CLUB));
        testBoard.setRiver(new Card(Value.SEVEN, Suit.HEART));
        Assertions.assertEquals(HandRanking.STRAIGHT, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.SEVEN, Suit.HEART), new Card(Value.SIX, Suit.HEART),
                new Card(Value.FIVE, Suit.DIAMOND), new Card(Value.FOUR, Suit.CLUB),
                new Card(Value.THREE, Suit.SPADE)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testStraightHoleCards() {
        testPlayer.setHand(new Hand(new Card(Value.SIX, Suit.HEART), new Card(Value.FIVE, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.SEVEN, Suit.DIAMOND), new Card(Value.KING, Suit.SPADE), new Card(Value.KING, Suit.CLUB));
        testBoard.setTurn(new Card(Value.FOUR, Suit.CLUB));
        testBoard.setRiver(new Card(Value.THREE, Suit.HEART));
        Assertions.assertEquals(HandRanking.STRAIGHT, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.SEVEN, Suit.DIAMOND), new Card(Value.SIX, Suit.HEART),
                new Card(Value.FIVE, Suit.SPADE), new Card(Value.FOUR, Suit.CLUB),
                new Card(Value.THREE, Suit.HEART)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testStraightBoard() {
        testPlayer.setHand(new Hand(new Card(Value.JACK, Suit.HEART), new Card(Value.KING, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.FIVE, Suit.DIAMOND), new Card(Value.SIX, Suit.SPADE), new Card(Value.SEVEN, Suit.CLUB));
        testBoard.setTurn(new Card(Value.FOUR, Suit.CLUB));
        testBoard.setRiver(new Card(Value.THREE, Suit.HEART));
        Assertions.assertEquals(HandRanking.STRAIGHT, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.SEVEN, Suit.CLUB), new Card(Value.SIX, Suit.SPADE),
                new Card(Value.FIVE, Suit.DIAMOND), new Card(Value.FOUR, Suit.CLUB),
                new Card(Value.THREE, Suit.HEART)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testStraightWheel() {
        testPlayer.setHand(new Hand(new Card(Value.ACE, Suit.HEART), new Card(Value.KING, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.TWO, Suit.DIAMOND), new Card(Value.JACK, Suit.SPADE), new Card(Value.FIVE, Suit.CLUB));
        testBoard.setTurn(new Card(Value.FOUR, Suit.CLUB));
        testBoard.setRiver(new Card(Value.THREE, Suit.HEART));
        Assertions.assertEquals(HandRanking.STRAIGHT, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.ACE, Suit.HEART), new Card(Value.FIVE, Suit.CLUB),
                new Card(Value.FOUR, Suit.CLUB), new Card(Value.THREE, Suit.HEART),
                new Card(Value.TWO, Suit.DIAMOND)
                ));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testStraightRepeatedCard() {
        testPlayer.setHand(new Hand(new Card(Value.QUEEN, Suit.HEART), new Card(Value.FIVE, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.SIX, Suit.DIAMOND), new Card(Value.SEVEN, Suit.SPADE), new Card(Value.EIGHT, Suit.CLUB));
        testBoard.setTurn(new Card(Value.FIVE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.FOUR, Suit.HEART));
        Assertions.assertEquals(HandRanking.STRAIGHT, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.EIGHT, Suit.CLUB), new Card(Value.SEVEN, Suit.SPADE),
                new Card(Value.SIX, Suit.DIAMOND), new Card(Value.FIVE, Suit.SPADE),
                new Card(Value.FOUR, Suit.HEART)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testStraightTwoDifferentRepeatedCards() {
        testPlayer.setHand(new Hand(new Card(Value.SEVEN, Suit.HEART), new Card(Value.FIVE, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.SIX, Suit.DIAMOND), new Card(Value.SEVEN, Suit.SPADE), new Card(Value.EIGHT, Suit.CLUB));
        testBoard.setTurn(new Card(Value.FIVE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.FOUR, Suit.HEART));
        Assertions.assertEquals(HandRanking.STRAIGHT, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.EIGHT, Suit.CLUB), new Card(Value.SEVEN, Suit.HEART),
                new Card(Value.SIX, Suit.DIAMOND), new Card(Value.FIVE, Suit.SPADE),
                new Card(Value.FOUR, Suit.HEART)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testStraightThreeRepeatedCards() {
        testPlayer.setHand(new Hand(new Card(Value.FIVE, Suit.HEART), new Card(Value.FIVE, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.SIX, Suit.DIAMOND), new Card(Value.SEVEN, Suit.SPADE), new Card(Value.EIGHT, Suit.CLUB));
        testBoard.setTurn(new Card(Value.FIVE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.FOUR, Suit.HEART));
        Assertions.assertEquals(HandRanking.STRAIGHT, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.EIGHT, Suit.CLUB), new Card(Value.SEVEN, Suit.SPADE),
                new Card(Value.SIX, Suit.DIAMOND), new Card(Value.FIVE, Suit.HEART),
                new Card(Value.FOUR, Suit.HEART)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testStraightFlushOneHoleCard() {
        testPlayer.setHand(new Hand(new Card(Value.SIX, Suit.HEART), new Card(Value.KING, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.FIVE, Suit.HEART), new Card(Value.THREE, Suit.SPADE), new Card(Value.FOUR, Suit.HEART));
        testBoard.setTurn(new Card(Value.EIGHT, Suit.HEART));
        testBoard.setRiver(new Card(Value.SEVEN, Suit.HEART));
        Assertions.assertEquals(HandRanking.STRAIGHT_FLUSH, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.EIGHT, Suit.HEART), new Card(Value.SEVEN, Suit.HEART),
                new Card(Value.SIX, Suit.HEART), new Card(Value.FIVE, Suit.HEART),
                new Card(Value.FOUR, Suit.HEART)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testStraightFlushHoleCards() {
        testPlayer.setHand(new Hand(new Card(Value.SIX, Suit.HEART), new Card(Value.FIVE, Suit.HEART)));

        testBoard.setFlop(new Card(Value.SEVEN, Suit.HEART), new Card(Value.KING, Suit.SPADE), new Card(Value.KING, Suit.CLUB));
        testBoard.setTurn(new Card(Value.FOUR, Suit.HEART));
        testBoard.setRiver(new Card(Value.THREE, Suit.HEART));
        Assertions.assertEquals(HandRanking.STRAIGHT_FLUSH, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.SEVEN, Suit.HEART), new Card(Value.SIX, Suit.HEART),
                new Card(Value.FIVE, Suit.HEART), new Card(Value.FOUR, Suit.HEART),
                new Card(Value.THREE, Suit.HEART)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testStraightFlushBoard() {
        testPlayer.setHand(new Hand(new Card(Value.JACK, Suit.HEART), new Card(Value.KING, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.FIVE, Suit.HEART), new Card(Value.SIX, Suit.HEART), new Card(Value.SEVEN, Suit.HEART));
        testBoard.setTurn(new Card(Value.FOUR, Suit.HEART));
        testBoard.setRiver(new Card(Value.THREE, Suit.HEART));
        Assertions.assertEquals(HandRanking.STRAIGHT_FLUSH, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.SEVEN, Suit.HEART), new Card(Value.SIX, Suit.HEART),
                new Card(Value.FIVE, Suit.HEART), new Card(Value.FOUR, Suit.HEART),
                new Card(Value.THREE, Suit.HEART)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testStraightFlushWheel() {
        testPlayer.setHand(new Hand(new Card(Value.ACE, Suit.HEART), new Card(Value.KING, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.TWO, Suit.HEART), new Card(Value.JACK, Suit.SPADE), new Card(Value.FIVE, Suit.HEART));
        testBoard.setTurn(new Card(Value.FOUR, Suit.HEART));
        testBoard.setRiver(new Card(Value.THREE, Suit.HEART));
        Assertions.assertEquals(HandRanking.STRAIGHT_FLUSH, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.ACE, Suit.HEART), new Card(Value.FIVE, Suit.HEART),
                new Card(Value.FOUR, Suit.HEART), new Card(Value.THREE, Suit.HEART),
                new Card(Value.TWO, Suit.HEART)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testStraightFlushRepeatedNonFlushCard() {
        testPlayer.setHand(new Hand(new Card(Value.SIX, Suit.HEART), new Card(Value.KING, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.FIVE, Suit.HEART), new Card(Value.SIX, Suit.DIAMOND), new Card(Value.SEVEN, Suit.HEART));
        testBoard.setTurn(new Card(Value.FOUR, Suit.HEART));
        testBoard.setRiver(new Card(Value.THREE, Suit.HEART));
        Assertions.assertEquals(HandRanking.STRAIGHT_FLUSH, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.SEVEN, Suit.HEART), new Card(Value.SIX, Suit.HEART),
                new Card(Value.FIVE, Suit.HEART), new Card(Value.FOUR, Suit.HEART),
                new Card(Value.THREE, Suit.HEART)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testRoyalFlushHoleCards() {
        testPlayer.setHand(new Hand(new Card(Value.TEN, Suit.CLUB), new Card(Value.KING, Suit.CLUB)));

        testBoard.setFlop(new Card(Value.ACE, Suit.CLUB), new Card(Value.KING, Suit.HEART), new Card(Value.QUEEN, Suit.CLUB));
        testBoard.setTurn(new Card(Value.JACK, Suit.CLUB));
        testBoard.setRiver(new Card(Value.SIX, Suit.SPADE));
        Assertions.assertEquals(HandRanking.ROYAL_FLUSH, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.ACE, Suit.CLUB), new Card(Value.KING, Suit.CLUB),
                new Card(Value.QUEEN, Suit.CLUB), new Card(Value.JACK, Suit.CLUB),
                new Card(Value.TEN, Suit.CLUB)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testRoyalFlushOneHoleCard() {
        testPlayer.setHand(new Hand(new Card(Value.TEN, Suit.CLUB), new Card(Value.FIVE, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.ACE, Suit.CLUB), new Card(Value.KING, Suit.CLUB), new Card(Value.QUEEN, Suit.CLUB));
        testBoard.setTurn(new Card(Value.JACK, Suit.CLUB));
        testBoard.setRiver(new Card(Value.SIX, Suit.SPADE));
        Assertions.assertEquals(HandRanking.ROYAL_FLUSH, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.ACE, Suit.CLUB), new Card(Value.KING, Suit.CLUB),
                new Card(Value.QUEEN, Suit.CLUB), new Card(Value.JACK, Suit.CLUB),
                new Card(Value.TEN, Suit.CLUB)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));
    }

    @Test
    public void testRoyalFlushBoard() {
        testPlayer.setHand(new Hand(new Card(Value.FIVE, Suit.HEART), new Card(Value.FIVE, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.ACE, Suit.CLUB), new Card(Value.KING, Suit.CLUB), new Card(Value.QUEEN, Suit.CLUB));
        testBoard.setTurn(new Card(Value.JACK, Suit.CLUB));
        testBoard.setRiver(new Card(Value.TEN, Suit.CLUB));
        Assertions.assertEquals(HandRanking.ROYAL_FLUSH, CuT.determineBestPossibleHand(testPlayer));

        ArrayList<Card> bestHand = new ArrayList<Card>(Arrays.asList(
                new Card(Value.ACE, Suit.CLUB), new Card(Value.KING, Suit.CLUB),
                new Card(Value.QUEEN, Suit.CLUB), new Card(Value.JACK, Suit.CLUB),
                new Card(Value.TEN, Suit.CLUB)));

        Assertions.assertEquals(bestHand, CuT.getBestHand(testBoard, testPlayer));

    }
}
