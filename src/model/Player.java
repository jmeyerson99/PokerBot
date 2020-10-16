package model;

import java.util.ArrayList;

public class Player {

    private Hand hand;
    private HandRanking handRanking;
    private ArrayList<Card> bestPossibleHand;
    private final String name;

    public Player(String name) {
        this.name = name;
        this.bestPossibleHand = new ArrayList<>();
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public String getName() {
        return name;
    }

    public HandRanking getHandRanking() {
        return handRanking;
    }

    public void setHandRanking(HandRanking handRanking) {
        this.handRanking = handRanking;
    }

    public ArrayList<Card> getBestPossibleHand() { return bestPossibleHand; }

    public void setBestPossibleHand(ArrayList<Card> bestPossibleHand) { this.bestPossibleHand = bestPossibleHand; }

    public void resetHand() {
        for (Card c : bestPossibleHand) { bestPossibleHand.remove(c); }
        hand = null;
        handRanking = null;
    }
}
