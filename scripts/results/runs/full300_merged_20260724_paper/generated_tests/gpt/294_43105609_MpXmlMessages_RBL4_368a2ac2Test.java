
package com.riversoft.weixin.mp.message;

import com.riversoft.weixin.common.exception.WxRuntimeException;
import com.riversoft.weixin.common.message.XmlMessageHeader;
import com.riversoft.weixin.mp.request.LinkRequest;
import org.junit.Test;
import static org.junit.Assert.*;

public class MpXmlMessages_RBL4_368a2ac2Test {

    @Test
    public void testFromXmlTextRequest() {
        String xml = "<xml><MsgType>text</MsgType><Content>Hello</Content></xml>";
        XmlMessageHeader result = MpXmlMessages.fromXml(xml);
        assertTrue(result instanceof TextRequest);
    }

    @Test
    public void testFromXmlImageRequest() {
        String xml = "<xml><MsgType>image</MsgType><MediaId>12345</MediaId></xml>";
        XmlMessageHeader result = MpXmlMessages.fromXml(xml);
        assertTrue(result instanceof ImageRequest);
    }

    @Test
    public void testFromXmlVoiceRequest() {
        String xml = "<xml><MsgType>voice</MsgType><MediaId>12345</MediaId></xml>";
        XmlMessageHeader result = MpXmlMessages.fromXml(xml);
        assertTrue(result instanceof VoiceRequest);
    }

    @Test
    public void testFromXmlVideoRequest() {
        String xml = "<xml><MsgType>video</MsgType><MediaId>12345</MediaId></xml>";
        XmlMessageHeader result = MpXmlMessages.fromXml(xml);
        assertTrue(result instanceof VideoRequest);
    }

    @Test
    public void testFromXmlShortVideoRequest() {
        String xml = "<xml><MsgType>shortvideo</MsgType><MediaId>12345</MediaId></xml>";
        XmlMessageHeader result = MpXmlMessages.fromXml(xml);
        assertTrue(result instanceof ShortVideoRequest);
    }

    @Test
    public void testFromXmlLocationRequest() {
        String xml = "<xml><MsgType>location</MsgType><Location_X>23.134521</Location_X><Location_Y>113.358803</Location_Y></xml>";
        XmlMessageHeader result = MpXmlMessages.fromXml(xml);
        assertTrue(result instanceof LocationRequest);
    }

    @Test
    public void testFromXmlLinkRequest() {
        String xml = "<xml><MsgType>link</MsgType><Title>Link Title</Title><Url>http://example.com</Url></xml>";
        XmlMessageHeader result = MpXmlMessages.fromXml(xml);
        assertTrue(result instanceof LinkRequest);
    }

    @Test(expected = WxRuntimeException.class)
    public void testFromXmlUnknownMessageType() {
        String xml = "<xml><MsgType>unknown</MsgType></xml>";
        MpXmlMessages.fromXml(xml);
    }

    @Test
    public void testToXml() {
        XmlMessageHeader xmlMessage = new TextRequest(); // Assuming TextRequest is a valid subclass
        String xml = MpXmlMessages.toXml(xmlMessage);
        assertNotNull(xml);
    }

    @Test(expected = WxRuntimeException.class)
    public void testToXmlException() {
        XmlMessageHeader xmlMessage = null; // Assuming null will cause an exception
        MpXmlMessages.toXml(xmlMessage);
    }
}
