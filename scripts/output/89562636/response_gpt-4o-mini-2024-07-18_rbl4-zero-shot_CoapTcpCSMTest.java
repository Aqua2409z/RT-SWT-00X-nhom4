package com.mbed.coap.server.internal;

import com.mbed.coap.packet.BlockSize;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoapTcpCSMTest {

    @Test
    public void testBaseValues() {
        assertEquals(1152, CoapTcpCSM.BASE.getMaxMessageSize());
        assertFalse(CoapTcpCSM.BASE.isBlockTransferEnabled());
    }

    @Test
    public void testConstructor() {
        CoapTcpCSM csm = new CoapTcpCSM(2048, true);
        assertEquals(2048, csm.getMaxMessageSize());
        assertTrue(csm.isBlockTransferEnabled());
    }

    @Test
    public void testWithNewOptions() {
        CoapTcpCSM csm = new CoapTcpCSM(2048, true);
        CoapTcpCSM newCsm = csm.withNewOptions(1024L, null);
        assertEquals(1024, newCsm.getMaxMessageSize());
        assertTrue(newCsm.isBlockTransferEnabled());
    }

    @Test
    public void testMin() {
        CoapTcpCSM csm1 = new CoapTcpCSM(2048, true);
        CoapTcpCSM csm2 = new CoapTcpCSM(1024, false);
        CoapTcpCSM minCsm = CoapTcpCSM.min(csm1, csm2);
        assertEquals(1024, minCsm.getMaxMessageSize());
        assertFalse(minCsm.isBlockTransferEnabled());
    }

    @Test
    public void testGetBlockSize() {
        CoapTcpCSM csm = new CoapTcpCSM(512, true);
        assertEquals(BlockSize.S_512, csm.getBlockSize());

        csm = new CoapTcpCSM(2048, true);
        assertEquals(BlockSize.S_1024, csm.getBlockSize());

        csm = new CoapTcpCSM(1152, true);
        assertEquals(BlockSize.S_1024_BERT, csm.getBlockSize());
    }

    @Test
    public void testGetMaxOutboundPayloadSize() {
        CoapTcpCSM csm = new CoapTcpCSM(2048, true);
        assertEquals(1024, csm.getMaxOutboundPayloadSize());

        csm = new CoapTcpCSM(512, true);
        assertEquals(512, csm.getMaxOutboundPayloadSize());

        csm = new CoapTcpCSM(1152, true);
        assertEquals(1024, csm.getMaxOutboundPayloadSize());
    }

    @Test
    public void testEqualsAndHashCode() {
        CoapTcpCSM csm1 = new CoapTcpCSM(2048, true);
        CoapTcpCSM csm2 = new CoapTcpCSM(2048, true);
        CoapTcpCSM csm3 = new CoapTcpCSM(1024, false);

        assertEquals(csm1, csm2);
        assertNotEquals(csm1, csm3);
        assertNotEquals(csm2, csm3);
        assertEquals(csm1.hashCode(), csm2.hashCode());
        assertNotEquals(csm1.hashCode(), csm3.hashCode());
    }

    @Test
    public void testToString() {
        CoapTcpCSM csm = new CoapTcpCSM(2048, true);
        assertEquals("CoapTcpCSM{block=true, size=2048}", csm.toString());
    }
}
