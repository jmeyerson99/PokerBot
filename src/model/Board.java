package model;
import java.util.ArrayList;

public class Board {

    private ArrayList<Card> flop;
    private Card turn;
    private Card river;

    private ArrayList<Card> board;

    public Board() {
        this.flop = new ArrayList<Card>();
        this.board = new ArrayList<Card>();
    }

    public Card getRiver() {
        return river;
    }

    public ArrayList<Card> getFlop() {
        return flop;
    }

    public Card getTurn() {
        return turn;
    }

    public ArrayList<Card> getBoard() {
        return board;
    }

    public void setFlop(Card flop1, Card flop2, Card flop3) {
        this.flop.add(flop1);
        this.flop.add(flop2);
        this.flop.add(flop3);
        this.board.add(flop1);
        this.board.add(flop2);
        this.board.add(flop3);
    }

    public void setTurn(Card turn) {
        this.turn = turn;
        this.board.add(turn);
    }

    public void setRiver(Card river) {
        this.river = river;
        this.board.add(river);
    }

    public void resetBoard() {
        for (Card c : this.board) { this.board.remove(c); }
        for (Card c : this.flop) { this.board.remove(c); } // TODO - this.flop.clear() doesn't work
        this.turn = null;
        this.river = null;
    }

    public void removeFlop() {
        for (Card c : this.flop) {
            this.board.remove(c);
        }
        for (Card c : this.flop) { this.board.remove(c); } // TODO - this.flop.clear() doesn't work
    }

    public void removeTurn() {
        this.board.remove(this.turn);
        this.turn = null;
    }

    public void removeRiver() {
        this.board.remove(this.river);
        this.river = null;
    }

    @Override
    public String toString() {
        String out = "";
        for (Card c : this.board) {
            out +=  c.toString() + " ";
        }
        return out;
    }

    public int getSize() {
        return this.board.size();
    }
}