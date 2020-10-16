package model;

public class Card {
    private final Suit suit;
    private final Value value;

    public Card(Value value, Suit suit) {
        this.suit = suit;
        this.value = value;
    }

    public Suit getSuit() {
        return suit;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString() + "," + suit.toString();
    }
}
