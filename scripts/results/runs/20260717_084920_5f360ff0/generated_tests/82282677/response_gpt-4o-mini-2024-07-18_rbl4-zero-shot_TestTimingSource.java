
package org.jdesktop.core.animation.timing;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicBoolean;

public class TestTimingSource {

    private TimingSource timingSource;

    @Before
    public void setUp() {
        timingSource = new TimingSource() {
            private boolean disposed = false;

            @Override
            public void init() {
                disposed = false;
            }

            @Override
            public void dispose() {
                disposed = true;
            }

            @Override
            public boolean isDisposed() {
                return disposed;
            }
        };
    }

    @Test
    public void testInit() {
        timingSource.init();
        assertFalse(timingSource.isDisposed());
    }

    @Test
    public void testDispose() {
        timingSource.init();
        timingSource.dispose();
        assertTrue(timingSource.isDisposed());
    }

    @Test
    public void testAddTickListener() {
        final AtomicBoolean tickReceived = new AtomicBoolean(false);
        TimingSource.TickListener listener = new TimingSource.TickListener() {
            @Override
            public void timingSourceTick(TimingSource source, long nanoTime) {
                tickReceived.set(true);
            }
        };

        timingSource.addTickListener(listener);
        timingSource.init();
        timingSource.runPerTick();
        assertTrue(tickReceived.get());
    }

    @Test
    public void testRemoveTickListener() {
        final AtomicBoolean tickReceived = new AtomicBoolean(false);
        TimingSource.TickListener listener = new TimingSource.TickListener() {
            @Override
            public void timingSourceTick(TimingSource source, long nanoTime) {
                tickReceived.set(true);
            }
        };

        timingSource.addTickListener(listener);
        timingSource.removeTickListener(listener);
        timingSource.init();
        timingSource.runPerTick();
        assertFalse(tickReceived.get());
    }

    @Test
    public void testAddPostTickListener() {
        final AtomicBoolean postTickReceived = new AtomicBoolean(false);
        TimingSource.PostTickListener postListener = new TimingSource.PostTickListener() {
            @Override
            public void timingSourcePostTick(TimingSource source, long nanoTime) {
                postTickReceived.set(true);
            }
        };

        timingSource.addPostTickListener(postListener);
        timingSource.init();
        timingSource.runPerTick();
        assertTrue(postTickReceived.get());
    }

    @Test
    public void testRemovePostTickListener() {
        final AtomicBoolean postTickReceived = new AtomicBoolean(false);
        TimingSource.PostTickListener postListener = new TimingSource.PostTickListener() {
            @Override
            public void timingSourcePostTick(TimingSource source, long nanoTime) {
                postTickReceived.set(true);
            }
        };

        timingSource.addPostTickListener(postListener);
        timingSource.removePostTickListener(postListener);
        timingSource.init();
        timingSource.runPerTick();
        assertFalse(postTickReceived.get());
    }

    @Test
    public void testSubmitRunnable() {
        final AtomicBoolean taskExecuted = new AtomicBoolean(false);
        Runnable task = () -> taskExecuted.set(true);

        timingSource.submit(task);
        timingSource.init();
        timingSource.runPerTick();
        assertTrue(taskExecuted.get());
    }

    @Test
    public void testSubmitNullRunnable() {
        timingSource.submit(null);
        timingSource.init();
        timingSource.runPerTick();
        // No exception should be thrown
    }
}
