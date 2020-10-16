package model;

public enum HandRanking {
    ROYAL_FLUSH,
    STRAIGHT_FLUSH,
    QUADS,
    FULL_HOUSE,
    FLUSH,
    STRAIGHT,
    TRIPS,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD;

    public static int getRankValue(HandRanking rank) {
        switch (rank) {
            case HIGH_CARD:
                return 0;
            case ONE_PAIR:
                return 1;
            case TWO_PAIR:
                return 2;
            case TRIPS:
                return 3;
            case STRAIGHT:
                return 4;
            case FLUSH:
                return 5;
            case FULL_HOUSE:
                return 6;
            case QUADS:
                return 7;
            case STRAIGHT_FLUSH:
                return 8;
            case ROYAL_FLUSH:
                return 9;
            default:
                //throw error
                return -1;
        }
    }
}
