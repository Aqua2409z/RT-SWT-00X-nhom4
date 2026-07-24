
package de.voidnode.trading4j.functionality.timeframeconversion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import de.voidnode.trading4j.api.Failed;
import de.voidnode.trading4j.api.OrderFilter;
import de.voidnode.trading4j.domain.marketdata.impl.DatedCandleStick;
import de.voidnode.trading4j.domain.orders.BasicPendingOrder;
import de.voidnode.trading4j.domain.timeframe.TimeFrame;

public class TimeFrameConvertingTradeGuard_RBL4_5d305145Test {

    private TimeFrameConvertingTradeGuard<DatedCandleStick<TimeFrame>, DatedCandleStick<TimeFrame>, TimeFrame, TimeFrame> tradeGuard;
    private OrderFilter<DatedCandleStick<TimeFrame>> mockOrderFilter;
    private TimeFrameConverter<DatedCandleStick<TimeFrame>, DatedCandleStick<TimeFrame>, TimeFrame, TimeFrame> mockTimeFrameConverter;

    @Before
    public void setUp() {
        mockOrderFilter = mock(OrderFilter.class);
        mockTimeFrameConverter = mock(TimeFrameConverter.class);
        tradeGuard = new TimeFrameConvertingTradeGuard<>(mockOrderFilter, mockTimeFrameConverter);
    }

    @Test
    public void testUpdateMarketData_WithAggregatedCandle() {
        DatedCandleStick<TimeFrame> marketData = mock(DatedCandleStick.class);
        DatedCandleStick<TimeFrame> aggregatedCandle = mock(DatedCandleStick.class);
        
        when(mockTimeFrameConverter.aggregate(marketData)).thenReturn(Optional.of(aggregatedCandle));

        tradeGuard.updateMarketData(marketData);

        verify(mockOrderFilter).updateMarketData(aggregatedCandle);
        assertTrue(tradeGuard.firstCandleCompleted);
    }

    @Test
    public void testUpdateMarketData_WithoutAggregatedCandle() {
        DatedCandleStick<TimeFrame> marketData = mock(DatedCandleStick.class);
        
        when(mockTimeFrameConverter.aggregate(marketData)).thenReturn(Optional.empty());

        tradeGuard.updateMarketData(marketData);

        verify(mockOrderFilter, never()).updateMarketData(any());
        assertTrue(!tradeGuard.firstCandleCompleted);
    }

    @Test
    public void testFilterOrder_BeforeFirstCandleCompleted() {
        BasicPendingOrder order = mock(BasicPendingOrder.class);
        
        Optional<Failed> result = tradeGuard.filterOrder(order);

        assertTrue(result.isPresent());
        assertEquals("Trade was blocked because no complete candle stick was aggregated yet.", result.get().getMessage());
    }

    @Test
    public void testFilterOrder_AfterFirstCandleCompleted() {
        BasicPendingOrder order = mock(BasicPendingOrder.class);
        DatedCandleStick<TimeFrame> aggregatedCandle = mock(DatedCandleStick.class);
        
        when(mockTimeFrameConverter.aggregate(any())).thenReturn(Optional.of(aggregatedCandle));
        tradeGuard.updateMarketData(mock(DatedCandleStick.class));

        when(mockOrderFilter.filterOrder(order)).thenReturn(Optional.empty());

        Optional<Failed> result = tradeGuard.filterOrder(order);

        assertTrue(result.isEmpty());
        verify(mockOrderFilter).filterOrder(order);
    }
}
