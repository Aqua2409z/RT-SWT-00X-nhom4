
package net.redhogs.cronparser.format;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.util.Locale;

import net.redhogs.cronparser.CronExpressionDescriptor;
import net.redhogs.cronparser.Options;

import org.junit.Before;
import org.junit.Test;
import org.quartz.CronExpression;

public class CronExpressionFormatterTest {

    private CronExpressionFormatter formatter;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        formatter = new CronExpressionFormatter(options);
    }

    @Test
    public void testPrintWithOptions() throws Exception {
        String cronExpression = "0 0/5 * * * ?";
        CronExpression cron = new CronExpression(cronExpression);
        String expectedDescription = CronExpressionDescriptor.getDescription(cronExpression, options, Locale.ENGLISH);
        
        String result = formatter.print(cron, Locale.ENGLISH);
        
        assertEquals(expectedDescription, result);
    }

    @Test
    public void testPrintWithoutOptions() throws Exception {
        String cronExpression = "0 0/5 * * * ?";
        CronExpression cron = new CronExpression(cronExpression);
        String expectedDescription = CronExpressionDescriptor.getDescription(cronExpression, Locale.ENGLISH);
        
        formatter = new CronExpressionFormatter(); // No options
        String result = formatter.print(cron, Locale.ENGLISH);
        
        assertEquals(expectedDescription, result);
    }

    @Test
    public void testPrintWithParseException() throws Exception {
        String cronExpression = "invalid cron expression";
        CronExpression cron = new CronExpression(cronExpression);
        
        String result = formatter.print(cron, Locale.ENGLISH);
        
        assertEquals(cronExpression, result);
    }

    @Test
    public void testParseUnsupportedOperation() {
        assertThrows(UnsupportedOperationException.class, () -> {
            formatter.parse("0 0/5 * * * ?", Locale.ENGLISH);
        });
    }
}
