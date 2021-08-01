package string.equity;

import model.Casino;
// TODO - consider adding a logger when testing

// TODO - to determine equity against range, need to rank all starting hands and then determine how wide someone is opening. Then compare against that percentage of hands. Make a fixed list of size x, and take the first ___ indices that are the percentage of x.
public class test_Run {
    // This class will test running the simulator,which deals cards to all
    // players, and determines equity for each player throughout the hand.

    public static void main(String[] args) {
        Casino casino = new Casino(3);
        casino.shuffleUpAndDeal();
    }
}
