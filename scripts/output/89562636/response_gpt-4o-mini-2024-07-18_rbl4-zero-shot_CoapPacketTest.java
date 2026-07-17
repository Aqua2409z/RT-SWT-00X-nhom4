package com.mbed.coap.packet;

import com.mbed.coap.exception.CoapException;
import com.mbed.coap.packet.CoapPacket;
import com.mbed.coap.packet.Code;
import com.mbed.coap.packet.MessageType;
import com.mbed.coap.packet.Method;
import com.mbed.coap.packet.HeaderOptions;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;

import static org.junit.Assert.*;

public class CoapPacketTest {

    private InetSocketAddress remoteAddress;
    private CoapPacket coapPacket;

    @Before
    public void setUp() {
        remoteAddress = new InetSocketAddress("127.0.0.1", 5683);
        coapPacket = new CoapPacket(remoteAddress);
    }

    @Test
    public void testConstructorWithRemoteAddress() {
        assertNotNull(coapPacket);
        assertEquals(remoteAddress, coapPacket.getRemoteAddress());
    }

    @Test
    public void testConstructorWithCodeAndMessageType() {
        CoapPacket packet = new CoapPacket(Code.C205_CONTENT, MessageType.Confirmable, remoteAddress);
        assertEquals(Code.C205_CONTENT, packet.getCode());
        assertEquals(MessageType.Confirmable, packet.getMessageType());
        assertEquals(remoteAddress, packet.getRemoteAddress());
    }

    @Test
    public void testConstructorWithMethod() {
        CoapPacket packet = new CoapPacket(Method.GET, MessageType.Confirmable, "test/path", remoteAddress);
        assertEquals(Method.GET, packet.getMethod());
        assertEquals("test/path", packet.headers().getUriPath());
        assertEquals(remoteAddress, packet.getRemoteAddress());
    }

    @Test
    public void testSetMessageId() {
        coapPacket.setMessageId(12345);
        assertEquals(12345, coapPacket.getMessageId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidMessageId() {
        coapPacket.setMessageId(70000); // out of range
    }

    @Test
    public void testSetPayload() {
        String payloadString = "Hello, CoAP!";
        coapPacket.setPayload(payloadString);
        assertEquals(payloadString, coapPacket.getPayloadString());
    }

    @Test
    public void testCreateResponse() {
        coapPacket.setCode(Code.C205_CONTENT);
        CoapPacket response = coapPacket.createResponse();
        assertNotNull(response);
        assertEquals(MessageType.Acknowledgement, response.getMessageType());
        assertEquals(Code.C205_CONTENT, response.getCode());
    }

    @Test
    public void testSerializeAndDeserialize() throws CoapException {
        coapPacket.setMessageId(123);
        coapPacket.setCode(Code.C205_CONTENT);
        coapPacket.setMethod(Method.GET);
        coapPacket.setPayload("Test Payload");

        byte[] serializedData = CoapPacket.serialize(coapPacket);
        CoapPacket deserializedPacket = CoapPacket.deserialize(remoteAddress, new ByteArrayInputStream(serializedData));

        assertEquals(coapPacket.getMessageId(), deserializedPacket.getMessageId());
        assertEquals(coapPacket.getCode(), deserializedPacket.getCode());
        assertEquals(coapPacket.getMethod(), deserializedPacket.getMethod());
        assertArrayEquals(coapPacket.getPayload(), deserializedPacket.getPayload());
    }

    @Test
    public void testToString() {
        coapPacket.setMessageId(123);
        coapPacket.setCode(Code.C205_CONTENT);
        coapPacket.setMethod(Method.GET);
        String result = coapPacket.toString();
        assertTrue(result.contains("MID:123"));
        assertTrue(result.contains("C205"));
    }

    @Test
    public void testEqualsAndHashCode() {
        CoapPacket packet1 = new CoapPacket(remoteAddress);
        CoapPacket packet2 = new CoapPacket(remoteAddress);
        assertEquals(packet1, packet2);
        assertEquals(packet1.hashCode(), packet2.hashCode());

        packet1.setMessageId(1);
        assertNotEquals(packet1, packet2);
    }

    @Test
    public void testGetRemoteAddrString() {
        assertEquals("127.0.0.1:5683", coapPacket.getRemoteAddrString());
    }
}
