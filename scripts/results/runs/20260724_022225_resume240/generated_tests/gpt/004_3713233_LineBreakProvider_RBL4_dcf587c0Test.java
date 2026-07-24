package org.junithelper.core.generator;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junithelper.core.config.Configuration;
import org.junithelper.core.config.LineBreakPolicy;
import org.junithelper.core.constant.StringValue;
import org.junithelper.core.meta.CurrentLineBreak;
import org.junithelper.core.generator.LineBreakProvider;

public class LineBreakProvider_RBL4_dcf587c0Test {

    @Test
    public void testGetLineBreakForceCRLF() {
        Configuration config = new Configuration();
        config.lineBreakPolicy = LineBreakPolicy.forceCRLF;
        LineBreakProvider provider = new LineBreakProvider(config, CurrentLineBreak.CRLF);
        assertEquals(StringValue.CarriageReturn + StringValue.LineFeed, provider.getLineBreak());
    }

    @Test
    public void testGetLineBreakForceLF() {
        Configuration config = new Configuration();
        config.lineBreakPolicy = LineBreakPolicy.forceLF;
        LineBreakProvider provider = new LineBreakProvider(config, CurrentLineBreak.CRLF);
        assertEquals(StringValue.LineFeed, provider.getLineBreak());
    }

    @Test
    public void testGetLineBreakForceNewFileCRLFWithCurrentLineBreak() {
        Configuration config = new Configuration();
        config.lineBreakPolicy = LineBreakPolicy.forceNewFileCRLF;
        LineBreakProvider provider = new LineBreakProvider(config, CurrentLineBreak.CRLF);
        assertEquals(StringValue.CarriageReturn + StringValue.LineFeed, provider.getLineBreak());
    }

    @Test
    public void testGetLineBreakForceNewFileCRLFWithoutCurrentLineBreak() {
        Configuration config = new Configuration();
        config.lineBreakPolicy = LineBreakPolicy.forceNewFileCRLF;
        LineBreakProvider provider = new LineBreakProvider(config, null);
        assertEquals(StringValue.CarriageReturn + StringValue.LineFeed, provider.getLineBreak());
    }

    @Test
    public void testGetLineBreakForceNewFileLFWithCurrentLineBreak() {
        Configuration config = new Configuration();
        config.lineBreakPolicy = LineBreakPolicy.forceNewFileLF;
        LineBreakProvider provider = new LineBreakProvider(config, CurrentLineBreak.CRLF);
        assertEquals(StringValue.LineFeed, provider.getLineBreak());
    }

    @Test
    public void testGetLineBreakForceNewFileLFWithoutCurrentLineBreak() {
        Configuration config = new Configuration();
        config.lineBreakPolicy = LineBreakPolicy.forceNewFileLF;
        LineBreakProvider provider = new LineBreakProvider(config, null);
        assertEquals(StringValue.LineFeed, provider.getLineBreak());
    }

    @Test
    public void testGetLineBreakDefault() {
        Configuration config = new Configuration();
        config.lineBreakPolicy = null; // Default case
        LineBreakProvider provider = new LineBreakProvider(config, CurrentLineBreak.CRLF);
        assertEquals(StringValue.CarriageReturn + StringValue.LineFeed, provider.getLineBreak());
    }
}
