package string.equity;

import model.Casino;

public class test_Run {
    // This class will test running the simulator,which deals cards to all
    // players, and determines equity for each player throughout the hand.

    public static void main(String[] args) {
        Casino casino = new Casino(3);
        casino.shuffleUpAndDeal();
    }
}
