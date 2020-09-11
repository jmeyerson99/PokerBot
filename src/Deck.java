import java.util.ArrayList;

public class Deck {

    private ArrayList<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
        for (Suit s : Suit.values()) { //make all suits
            for (Value v : Value.values()) { //make all values
                cards.add(new Card(v, s));
            }
        }
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
