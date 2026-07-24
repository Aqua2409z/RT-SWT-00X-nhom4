
package de.voidnode.trading4j.indicators.adx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.voidnode.trading4j.domain.marketdata.MarketData;
import de.voidnode.trading4j.domain.marketdata.WithOhlc;
import de.voidnode.trading4j.domain.monetary.Price;

import java.util.Optional;

public class TrueRange_RBL4_8c7aa6c3Test {

    private TrueRange<MockMarketData> trueRange;

    @Before
    public void setUp() {
        trueRange = new TrueRange<>();
    }

    @Test
    public void testIndicateFirstCallReturnsEmpty() {
        MockMarketData marketData = new MockMarketData(new Price(100), new Price(105), new Price(95), new Price(100));
        Optional<Price> result = trueRange.indicate(marketData);
        assertFalse(result.isPresent());
    }

    @Test
    public void testIndicateReturnsPriceOnSecondCall() {
        MockMarketData firstMarketData = new MockMarketData(new Price(100), new Price(105), new Price(95), new Price(100));
        trueRange.indicate(firstMarketData);

        MockMarketData secondMarketData = new MockMarketData(new Price(102), new Price(108), new Price(98), new Price(102));
        Optional<Price> result = trueRange.indicate(secondMarketData);
        assertTrue(result.isPresent());
        assertEquals(new Price(8), result.get());
    }

    @Test
    public void testIndicateHandlesVolatility() {
        MockMarketData firstMarketData = new MockMarketData(new Price(100), new Price(105), new Price(95), new Price(100));
        trueRange.indicate(firstMarketData);

        MockMarketData secondMarketData = new MockMarketData(new Price(102), new Price(110), new Price(98), new Price(102));
        Optional<Price> result = trueRange.indicate(secondMarketData);
        assertTrue(result.isPresent());
        assertEquals(new Price(10), result.get());
    }

    @Test
    public void testIndicateHandlesNegativeCloseToHigh() {
        MockMarketData firstMarketData = new MockMarketData(new Price(100), new Price(105), new Price(95), new Price(100));
        trueRange.indicate(firstMarketData);

        MockMarketData secondMarketData = new MockMarketData(new Price(95), new Price(100), new Price(90), new Price(95));
        Optional<Price> result = trueRange.indicate(secondMarketData);
        assertTrue(result.isPresent());
        assertEquals(new Price(5), result.get());
    }

    private static class TrueRange_RBL4_8c7aa6c3Test implements MarketData, WithOhlc {
        private final Price close;
        private final Price high;
        private final Price low;
        private final Price open;

        public MockMarketData(Price close, Price high, Price low, Price open) {
            this.close = close;
            this.high = high;
            this.low = low;
            this.open = open;
        }

        @Override
        public Price getClose() {
            return close;
        }

        @Override
        public Price getHigh() {
            return high;
        }

        @Override
        public Price getLow() {
            return low;
        }

        @Override
        public Price getOpen() {
            return open;
        }

        @Override
        public Price getVolatility() {
            return new Price(Math.abs(high.asPipette() - low.asPipette()));
        }
    }
}
