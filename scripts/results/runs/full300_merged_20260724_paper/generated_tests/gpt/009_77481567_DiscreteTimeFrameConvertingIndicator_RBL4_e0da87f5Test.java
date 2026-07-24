
package de.voidnode.trading4j.functionality.timeframeconversion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import de.voidnode.trading4j.api.Indicator;
import de.voidnode.trading4j.domain.MarketDirection;
import de.voidnode.trading4j.domain.marketdata.impl.DatedCandleStick;
import de.voidnode.trading4j.domain.timeframe.TimeFrame;

public class DiscreteTimeFrameConvertingIndicator_RBL4_e0da87f5Test {

    private Indicator<String, DatedCandleStick<TimeFrame>> originalIndicator;
    private TimeFrameConverter<DatedCandleStick<TimeFrame>, DatedCandleStick<TimeFrame>, TimeFrame, TimeFrame> timeFrameConverter;
    private DiscreteTimeFrameConvertingIndicator<String, DatedCandleStick<TimeFrame>, DatedCandleStick<TimeFrame>, TimeFrame, TimeFrame> convertingIndicator;

    @Before
    public void setUp() {
        originalIndicator = new Indicator<String, DatedCandleStick<TimeFrame>>() {
            @Override
            public Optional<String> indicate(DatedCandleStick<TimeFrame> candle) {
                return Optional.of("MarketDirection");
            }
        };

        timeFrameConverter = new TimeFrameConverter<DatedCandleStick<TimeFrame>, DatedCandleStick<TimeFrame>, TimeFrame, TimeFrame>() {
            @Override
            public Optional<DatedCandleStick<TimeFrame>> aggregate(DatedCandleStick<TimeFrame> candle) {
                return Optional.of(candle);
            }
        };

        convertingIndicator = new DiscreteTimeFrameConvertingIndicator<>(originalIndicator, timeFrameConverter);
    }

    @Test
    public void testIndicateReturnsValue() {
        DatedCandleStick<TimeFrame> candle = new DatedCandleStick<>();
        Optional<String> result = convertingIndicator.indicate(candle);
        assertTrue(result.isPresent());
        assertEquals("MarketDirection", result.get());
    }

    @Test
    public void testIndicateReturnsEmptyOptional() {
        originalIndicator = new Indicator<String, DatedCandleStick<TimeFrame>>() {
            @Override
            public Optional<String> indicate(DatedCandleStick<TimeFrame> candle) {
                return Optional.empty();
            }
        };

        convertingIndicator = new DiscreteTimeFrameConvertingIndicator<>(originalIndicator, timeFrameConverter);
        DatedCandleStick<TimeFrame> candle = new DatedCandleStick<>();
        Optional<String> result = convertingIndicator.indicate(candle);
        assertFalse(result.isPresent());
    }

    @Test
    public void testIndicateWithNullCandle() {
        DatedCandleStick<TimeFrame> candle = null;
        Optional<String> result = convertingIndicator.indicate(candle);
        assertFalse(result.isPresent());
    }
}
