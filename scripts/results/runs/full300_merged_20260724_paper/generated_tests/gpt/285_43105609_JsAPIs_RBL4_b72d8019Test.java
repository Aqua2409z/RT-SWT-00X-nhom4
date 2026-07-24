
package com.riversoft.weixin.mp.jsapi;

import com.riversoft.weixin.common.WxClient;
import com.riversoft.weixin.common.exception.WxRuntimeException;
import com.riversoft.weixin.common.jsapi.JsAPISignature;
import com.riversoft.weixin.common.jsapi.WxCardAPISignature;
import com.riversoft.weixin.mp.base.AppSetting;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class JsAPIs_RBL4_b72d8019Test {

    private JsAPIs jsAPIs;
    private WxClient wxClient;

    @Before
    public void setUp() {
        AppSetting appSetting = AppSetting.defaultSettings();
        wxClient = Mockito.mock(WxClient.class);
        jsAPIs = JsAPIs.with(appSetting);
        jsAPIs.setWxClient(wxClient);
    }

    @Test
    public void testCreateJsAPISignature() {
        String url = "http://example.com";
        String mockTicket = "mock_ticket";
        Mockito.when(wxClient.get(Mockito.anyString())).thenReturn("{\"ticket\":\"" + mockTicket + "\",\"expires_in\":7200}");

        JsAPISignature signature = jsAPIs.createJsAPISignature(url);

        assertNotNull(signature);
        assertEquals(mockTicket, signature.getSignature());
        assertEquals(url, signature.getUrl());
    }

    @Test(expected = WxRuntimeException.class)
    public void testCreateJsAPISignatureThrowsException() {
        String url = "http://example.com";
        Mockito.when(wxClient.get(Mockito.anyString())).thenThrow(new RuntimeException("Error"));

        jsAPIs.createJsAPISignature(url);
    }

    @Test
    public void testCreateWxCardJsAPISignature() {
        WxCardAPISignature wxCardAPISignature = new WxCardAPISignature();
        wxCardAPISignature.setCardId("card_id");
        String mockTicket = "mock_card_ticket";
        Mockito.when(wxClient.get(Mockito.anyString())).thenReturn("{\"ticket\":\"" + mockTicket + "\",\"expires_in\":7200}");

        WxCardAPISignature signature = jsAPIs.createWxCardJsAPISignature(wxCardAPISignature);

        assertNotNull(signature);
        assertEquals(mockTicket, signature.getSignature());
        assertEquals("card_id", signature.getCardId());
    }

    @Test(expected = WxRuntimeException.class)
    public void testCreateWxCardJsAPISignatureThrowsException() {
        WxCardAPISignature wxCardAPISignature = new WxCardAPISignature();
        wxCardAPISignature.setCardId("card_id");
        Mockito.when(wxClient.get(Mockito.anyString())).thenThrow(new RuntimeException("Error"));

        jsAPIs.createWxCardJsAPISignature(wxCardAPISignature);
    }

    @Test
    public void testGetJsAPITicket() {
        String mockTicket = "mock_ticket";
        Mockito.when(wxClient.get(Mockito.anyString())).thenReturn("{\"ticket\":\"" + mockTicket + "\",\"expires_in\":7200}");

        jsAPIs.createJsAPISignature("http://example.com");
        assertNotNull(jsAPIs.jsAPITicket);
        assertEquals(mockTicket, jsAPIs.jsAPITicket.getTicket());
    }

    @Test
    public void testGetWxCardAPITicket() {
        String mockTicket = "mock_card_ticket";
        Mockito.when(wxClient.get(Mockito.anyString())).thenReturn("{\"ticket\":\"" + mockTicket + "\",\"expires_in\":7200}");

        WxCardAPISignature wxCardAPISignature = new WxCardAPISignature();
        wxCardAPISignature.setCardId("card_id");
        jsAPIs.createWxCardJsAPISignature(wxCardAPISignature);
        assertNotNull(jsAPIs.wxCardAPITicket);
        assertEquals(mockTicket, jsAPIs.wxCardAPITicket.getTicket());
    }
}
