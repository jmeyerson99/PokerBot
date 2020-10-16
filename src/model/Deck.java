package model;

import java.util.ArrayList;
import java.util.Random;

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

    public Card getRandomCard() {
        Random rand = new Random();
        int upperbound = cards.size();
        int randomNum = rand.nextInt(upperbound);

        Card randomCard = this.cards.get(randomNum);
        this.cards.remove(randomNum);
        return randomCard;
    }

    public Card getCard(Card card) {
        this.cards.remove(card);
        return card;
    }

    public void resetDeck() {
        this.cards = new ArrayList<>();
        for (Suit s : Suit.values()) { //make all suits
            for (Value v : Value.values()) { //make all values
                cards.add(new Card(v, s));
            }
        }
    }
}
