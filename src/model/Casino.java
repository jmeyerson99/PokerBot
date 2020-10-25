package model;

import equity.EquityAnalyzer;

public class Casino {

    private Table table;

    private EquityAnalyzer analyzer;

    public Casino(int numPlayers) {
        this.table = new Table(numPlayers);
        this.analyzer = new EquityAnalyzer(table);
    }

    public void shuffleUpAndDeal() {
        this.table.dealHands();
        this.table.dealFlop();
        this.table.dealTurn();
        this.table.dealRiver();
        this.analyzer.determineEquity();
    }

    public static void main(String[] args) {
        Casino casino = new Casino(3);
        casino.shuffleUpAndDeal();
    }
}
