import java.util.ArrayList;

public class Board {

    private ArrayList<Card> flop;
    private Card turn;
    private Card river;

    private ArrayList<Card> board;

    public Board() {
        this.flop = new ArrayList<Card>;
        this.board = new ArrayList<Card>;
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

    public void setFlop(ArrayList<Card> flop) {
        this.flop = flop;
        this.board.add(flop);
    }

    public void setTurn(Card turn) {
        this.turn = turn;
        this.board.add(turn)
    }

    public void setRiver(Card river) {
        this.river = river;
        this.board.add(river)
    }
}