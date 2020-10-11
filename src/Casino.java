public class Casino {

    private Table table;

    private EquityAnalyzer analyzer;

    public Casino() {
        this.table = new Table(3);
        this.analyzer = new EquityAnalyzer(table);
    }

    public void shuffleUpAndDeal() {
        this.table.dealHands();
        this.table.dealFlop();
        this.table.dealTurn();
        this.table.dealRiver();
    }

    public static void main(String[] args) {
        Casino casino = new Casino();
        casino.shuffleUpAndDeal();
    }
}
