package unit.equity;

import equity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class test_EquityAnalyzer {

    private EquityAnalyzer CuT; // The component-under-test

    private Player testPlayer;
    private Board testBoard;
    private Table testTable;

    @BeforeEach
    public void setup()
    {
        testTable = new Table(1);
        testPlayer = testTable.getPlayers().get(0);
        testBoard = testTable.getBoard();
        CuT = new EquityAnalyzer(testTable);
    }

    @Test
    public void testHighCardHand()
    {
        testPlayer.setHand(new Hand(new Card(Value.ACE, Suit.HEART), new Card(Value.THREE, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.JACK, Suit.DIAMOND), new Card(Value.KING, Suit.HEART), new Card(Value.FOUR, Suit.CLUB));
        testBoard.setTurn(new Card(Value.NINE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.TWO, Suit.HEART));
        Assertions.assertEquals(CuT.determineBestPossibleHand(testPlayer), HandRanking.HIGH_CARD);
    }

    @Test
    public void testPocketPair()
    {
        testPlayer.setHand(new Hand(new Card(Value.THREE, Suit.HEART), new Card(Value.THREE, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.JACK, Suit.DIAMOND), new Card(Value.KING, Suit.HEART), new Card(Value.FOUR, Suit.CLUB));
        testBoard.setTurn(new Card(Value.NINE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.TWO, Suit.HEART));
        Assertions.assertEquals(CuT.determineBestPossibleHand(testPlayer), HandRanking.ONE_PAIR);
    }

    @Test
    public void testBoardPair()
    {
        testPlayer.setHand(new Hand(new Card(Value.THREE, Suit.HEART), new Card(Value.ACE, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.JACK, Suit.DIAMOND), new Card(Value.JACK, Suit.HEART), new Card(Value.FOUR, Suit.CLUB));
        testBoard.setTurn(new Card(Value.NINE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.TWO, Suit.HEART));
        Assertions.assertEquals(CuT.determineBestPossibleHand(testPlayer), HandRanking.ONE_PAIR);
    }

    @Test
    public void testOnePair()
    {
        testPlayer.setHand(new Hand(new Card(Value.TWO, Suit.HEART), new Card(Value.THREE, Suit.SPADE)));

        testBoard.setFlop(new Card(Value.JACK, Suit.DIAMOND), new Card(Value.KING, Suit.HEART), new Card(Value.FOUR, Suit.CLUB));
        testBoard.setTurn(new Card(Value.NINE, Suit.CLUB));
        testBoard.setRiver(new Card(Value.TWO, Suit.HEART));
        Assertions.assertEquals(CuT.determineBestPossibleHand(testPlayer), HandRanking.ONE_PAIR);
    }
}
