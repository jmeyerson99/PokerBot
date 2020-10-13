import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Table {

    private Deck deck;
    private Board board;

    private ArrayList<Player> players;
    private int numPlayers;

    //Manual mode variables
    private boolean manualMode = true;
    private ArrayList<Card> presetCards = new ArrayList<>(Arrays.asList(
            new Card(Value.KING, Suit.SPADE), new Card(Value.KING, Suit.CLUB), // Player 1
            new Card(Value.QUEEN, Suit.SPADE), new Card(Value.QUEEN, Suit.CLUB), // Player 2
            new Card(Value.JACK, Suit.SPADE), new Card(Value.TEN, Suit.CLUB), // Player 3
            new Card(Value.TWO, Suit.SPADE), new Card(Value.KING, Suit.DIAMOND), new Card(Value.TWO, Suit.HEART), // Flop
            new Card(Value.TEN, Suit.SPADE), // Turn
            new Card(Value.TWO, Suit.DIAMOND) // River
    ));

    public Table(int numPlayers) {
        this.numPlayers = numPlayers;
        this.players = new ArrayList<>();

        for (int i = 0; i< numPlayers; i++) {
            this.players.add(new Player(Integer.toString(i)));
        }

        this.board = new Board();
        this.deck = new Deck();
    }

    public void dealHands() {
        if (!manualMode) {
            for (Player p : this.players) {
                Card card1 = this.deck.getRandomCard();
                Card card2 = this.deck.getRandomCard();
                p.setHand(new Hand(card1, card2));
                System.out.println("Player " + p.getName() + " hand: " + p.getHand().toString());
            }
        } else {
            for (int i = 0; i < players.size(); i++) {
                Card card1 = this.deck.getCard(presetCards.get(2*i));
                Card card2 = this.deck.getCard(presetCards.get((2*i) + 1));
                players.get(i).setHand(new Hand(card1, card2));
                System.out.println("Player " + players.get(i).getName() + " hand: " + players.get(i).getHand().toString());
            }
        }
    }

    public void dealFlop() {
        if (!manualMode) {
            Card flop1 = this.deck.getRandomCard();
            Card flop2 = this.deck.getRandomCard();
            Card flop3 = this.deck.getRandomCard();
            this.board.setFlop(flop1, flop2, flop3);
            System.out.println("Board (flop): " + this.board.toString());
        } else {
            int i = (2 * players.size());
            Card flop1 = this.deck.getCard(presetCards.get(i));
            Card flop2 = this.deck.getCard(presetCards.get(i+1));
            Card flop3 = this.deck.getCard(presetCards.get(i+2));
            this.board.setFlop(flop1, flop2, flop3);
            System.out.println("Board (flop): " + this.board.toString());

        }
    }

    public void dealTurn() {
        if (!manualMode) {
            Card turn = this.deck.getRandomCard();
            this.board.setTurn(turn);
            System.out.println("Board (turn): " + this.board.toString());
        } else {
            Card turn = this.deck.getCard(presetCards.get(2 * players.size() + 3));
            this.board.setTurn(turn);
            System.out.println("Board (turn): " + this.board.toString());
        }
    }

    public void dealRiver() {
        if (!manualMode) {
            Card river = this.deck.getRandomCard();
            this.board.setRiver(river);
            System.out.println("Board (river): " + this.board.toString());
        } else {
            Card river = this.deck.getCard(presetCards.get(2 * players.size() + 4));
            this.board.setRiver(river);
            System.out.println("Board (river): " + this.board.toString());
        }
    }

    public Board getBoard() {
        return this.board;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    // TODO - add determineWinningHand function
}