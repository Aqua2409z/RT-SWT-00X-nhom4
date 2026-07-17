package com.salesforce.pyplyn.util;

import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

public class FormatUtilsTest {

    @Test
    public void testParseUTCTimeWithValidMillis() {
        String millis = "1633072800000"; // Corresponds to 2021-10-01T00:00:00Z
        ZonedDateTime result = FormatUtils.parseUTCTime(millis);
        assertEquals(ZonedDateTime.parse("2021-10-01T00:00:00Z"), result);
    }

    @Test
    public void testParseUTCTimeWithValidDate() {
        String date = "2021-10-01T00:00:00Z";
        ZonedDateTime result = FormatUtils.parseUTCTime(date);
        assertEquals(ZonedDateTime.parse(date), result);
    }

    @Test(expected = DateTimeParseException.class)
    public void testParseUTCTimeWithInvalidDate() {
        FormatUtils.parseUTCTime("invalid-date");
    }

    @Test
    public void testParseNumberWithValidInput() throws ParseException {
        String numberStr = "12345.67";
        Number result = FormatUtils.parseNumber(numberStr);
        assertEquals(12345.67, result);
    }

    @Test(expected = ParseException.class)
    public void testParseNumberWithInvalidInput() throws ParseException {
        FormatUtils.parseNumber("invalid-number");
    }

    @Test
    public void testFormatNumber() {
        Number value = 12345.6789;
        String result = FormatUtils.formatNumber(value);
        assertEquals("12,345.68", result);
    }

    @Test
    public void testFormatNumberFiveCharLimit() {
        assertEquals("0", FormatUtils.formatNumberFiveCharLimit(0));
        assertEquals("1", FormatUtils.formatNumberFiveCharLimit(1));
        assertEquals("1.2", FormatUtils.formatNumberFiveCharLimit(1.2));
        assertEquals("1.5K", FormatUtils.formatNumberFiveCharLimit(1500));
        assertEquals("12.3K", FormatUtils.formatNumberFiveCharLimit(12300));
        assertEquals("123K", FormatUtils.formatNumberFiveCharLimit(123000));
        assertEquals("1.2M", FormatUtils.formatNumberFiveCharLimit(1200000));
        assertEquals("12.3M", FormatUtils.formatNumberFiveCharLimit(12300000));
        assertEquals("123M", FormatUtils.formatNumberFiveCharLimit(123000000));
    }

    @Test
    public void testFormatMillisOrSeconds() {
        assertEquals("500ms", FormatUtils.formatMillisOrSeconds(500));
        assertEquals("1.5s", FormatUtils.formatMillisOrSeconds(1500));
        assertEquals("1.0s", FormatUtils.formatMillisOrSeconds(1000));
    }

    @Test
    public void testGenerateDefaultValueMessage() {
        String metric = "testMetric";
        Number value = 123.456;
        String result = FormatUtils.generateDefaultValueMessage(metric, value);
        assertEquals("Default value testMetric=123.46", result);
    }
}
