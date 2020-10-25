package model;

import java.util.ArrayList;

public class Table {

    private Deck deck;
    private Board board;

    private ArrayList<Player> players;
    private int numPlayers;

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
        for (Player p : this.players) {
            Card card1 = this.deck.getRandomCard();
            Card card2 = this.deck.getRandomCard();
            p.setHand(new Hand(card1, card2));
            System.out.println("Player " + p.getName() + " hand: " + p.getHand().toString());
        }
    }

    public void dealFlop() {
        Card flop1 = this.deck.getRandomCard();
        Card flop2 = this.deck.getRandomCard();
        Card flop3 = this.deck.getRandomCard();
        this.board.setFlop(flop1, flop2, flop3);
        System.out.println("Board (flop): " + this.board.toString());
    }

    public void dealTurn() {
        Card turn = this.deck.getRandomCard();
        this.board.setTurn(turn);
        System.out.println("Board (turn): " + this.board.toString());

    }

    public void dealRiver() {
        Card river = this.deck.getRandomCard();
        this.board.setRiver(river);
        System.out.println("Board (river): " + this.board.toString());
    }

    public void newHand() {
        this.deck.resetDeck();
        this.board.resetBoard();
        for (Player p : this.players) {
            p.resetHand();
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