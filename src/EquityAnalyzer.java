import java.util.ArrayList;

public class EquityAnalyzer {

    private Table table;

    public EquityAnalyzer(Table table) {
        this.table = table;
    }

    public void determineEquity() {
        Board board = table.getBoard();
        switch (board.getSize()) {
            case 0:
                break;

            case 3:
                break;

            case 4:
                break;

            case 5:
                determineShowdownWinner();
                break;

            default:
                // ERROR (invalid size)
                break;
        }
    }

    private void determineShowdownWinner() {
        for (Player p : this.table.getPlayers()) {
            
        }
    }
}
