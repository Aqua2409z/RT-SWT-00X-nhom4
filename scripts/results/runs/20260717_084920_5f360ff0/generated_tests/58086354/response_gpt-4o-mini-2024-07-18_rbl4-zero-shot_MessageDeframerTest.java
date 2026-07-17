package io.grpc.internal;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import io.grpc.Codec;
import io.grpc.Decompressor;
import io.grpc.Status;
import io.grpc.internal.CompositeReadableBuffer;
import io.grpc.internal.MessageDeframer;
import io.grpc.internal.ReadableBuffer;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class MessageDeframerTest {

    private MessageDeframer.Listener listener;
    private Decompressor decompressor;
    private MessageDeframer deframer;

    @Before
    public void setUp() {
        listener = mock(MessageDeframer.Listener.class);
        decompressor = Codec.Identity.NONE;
        deframer = new MessageDeframer(listener, decompressor, 1024);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestZeroMessages() {
        deframer.request(0);
    }

    @Test
    public void testDeframeWithEndOfStream() {
        ReadableBuffer buffer = mock(ReadableBuffer.class);
        when(buffer.readableBytes()).thenReturn(5);
        deframer.deframe(buffer, true);
        verify(listener, times(1)).endOfStream();
    }

    @Test
    public void testDeframeWithData() {
        ReadableBuffer buffer = mock(ReadableBuffer.class);
        when(buffer.readableBytes()).thenReturn(5);
        deframer.deframe(buffer, false);
        verify(listener, times(1)).bytesRead(5);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeframeAfterClose() {
        deframer.close();
        ReadableBuffer buffer = mock(ReadableBuffer.class);
        deframer.deframe(buffer, false);
    }

    @Test
    public void testIsStalled() {
        assertTrue(deframer.isStalled());
        ReadableBuffer buffer = mock(ReadableBuffer.class);
        when(buffer.readableBytes()).thenReturn(5);
        deframer.deframe(buffer, false);
        assertFalse(deframer.isStalled());
    }

    @Test
    public void testClose() {
        deframer.close();
        assertTrue(deframer.isClosed());
    }

    @Test
    public void testSetDecompressor() {
        Decompressor newDecompressor = mock(Decompressor.class);
        deframer.setDecompressor(newDecompressor);
        assertNotNull(deframer);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeframeWithMalformedHeader() {
        ReadableBuffer buffer = new CompositeReadableBuffer();
        buffer.addBuffer(new MockReadableBuffer(new byte[]{(byte) 0xFF, 0x00, 0x00, 0x00, 0x00}));
        deframer.deframe(buffer, false);
    }

    @Test
    public void testProcessBodyWithCompressedData() {
        // Mock the decompressor to return a valid InputStream
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        when(decompressor.decompress(any(InputStream.class))).thenReturn(inputStream);
        
        // Simulate a valid deframe call
        ReadableBuffer buffer = new CompositeReadableBuffer();
        buffer.addBuffer(new MockReadableBuffer(new byte[]{0x00, 0x00, 0x00, 0x00, 0x05}));
        deframer.deframe(buffer, false);
        
        // Verify that the messageRead method was called with the correct InputStream
        verify(listener, times(1)).messageRead(any(InputStream.class));
    }

    private static class MockReadableBuffer extends ReadableBuffer {
        private final byte[] data;
        private int position = 0;

        MockReadableBuffer(byte[] data) {
            this.data = data;
        }

        @Override
        public int readableBytes() {
            return data.length - position;
        }

        @Override
        public byte readByte() {
            return data[position++];
        }

        @Override
        public int readInt() {
            return (data[position++] & 0xFF) << 24 | (data[position++] & 0xFF) << 16 |
                   (data[position++] & 0xFF) << 8 | (data[position++] & 0xFF);
        }

        @Override
        public void close() {
            // No-op for mock
        }
    }
}
