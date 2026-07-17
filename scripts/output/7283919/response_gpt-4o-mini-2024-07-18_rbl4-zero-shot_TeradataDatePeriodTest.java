
package dbfit.environment;

import org.junit.Test;
import static org.junit.Assert.*;

public class TeradataDatePeriodTest {

    @Test
    public void testToString() {
        Object[] dates = { "2023-01-01", "2023-12-31" };
        TeradataDatePeriod period = new TeradataDatePeriod(dates);
        String expected = "2023-01-01,2023-12-31";
        assertEquals(expected, period.toString());
    }

    @Test
    public void testEquals_SameObject() {
        Object[] dates = { "2023-01-01", "2023-12-31" };
        TeradataDatePeriod period1 = new TeradataDatePeriod(dates);
        TeradataDatePeriod period2 = period1;
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testEquals_DifferentObjectSameValues() {
        Object[] dates1 = { "2023-01-01", "2023-12-31" };
        Object[] dates2 = { "2023-01-01", "2023-12-31" };
        TeradataDatePeriod period1 = new TeradataDatePeriod(dates1);
        TeradataDatePeriod period2 = new TeradataDatePeriod(dates2);
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testEquals_DifferentValues() {
        Object[] dates1 = { "2023-01-01", "2023-12-31" };
        Object[] dates2 = { "2023-01-02", "2023-12-30" };
        TeradataDatePeriod period1 = new TeradataDatePeriod(dates1);
        TeradataDatePeriod period2 = new TeradataDatePeriod(dates2);
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testEquals_Null() {
        Object[] dates = { "2023-01-01", "2023-12-31" };
        TeradataDatePeriod period = new TeradataDatePeriod(dates);
        assertFalse(period.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        Object[] dates = { "2023-01-01", "2023-12-31" };
        TeradataDatePeriod period = new TeradataDatePeriod(dates);
        assertFalse(period.equals("Not a TeradataDatePeriod"));
    }
}
