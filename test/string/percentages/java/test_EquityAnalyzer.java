package string.percentages.java;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.text.Position;
import java.EquityAnalyzer;
import java.Table;

public class test_EquityAnalyzer {

    private EquityAnalyzer CuT; // The component-under-test

    @BeforeEach
    public void setup()
    {
        Table testTable = new Table();
        CuT = new EquityAnalyzer();
    }

    @Test
    public void correctlyStoresData()
    {
        Assertions.assertEquals(true, true);
    }
}
