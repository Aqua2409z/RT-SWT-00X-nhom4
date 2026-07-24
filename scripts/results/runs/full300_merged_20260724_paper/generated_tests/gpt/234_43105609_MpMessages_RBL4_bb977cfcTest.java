
package com.riversoft.weixin.mp.message;

import com.riversoft.weixin.common.WxClient;
import com.riversoft.weixin.common.exception.WxRuntimeException;
import com.riversoft.weixin.mp.base.AppSetting;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class MpMessages_RBL4_bb977cfcTest {

    private MpMessages mpMessages;
    private WxClient wxClient;

    @Before
    public void setUp() {
        wxClient = Mockito.mock(WxClient.class);
        AppSetting appSetting = AppSetting.defaultSettings();
        mpMessages = MpMessages.with(appSetting);
        mpMessages.setWxClient(wxClient);
    }

    @Test
    public void testMpNewsToAll() {
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"msg_id\":12345}");
        long msgId = mpMessages.mpNews("testMpNews");
        assertEquals(12345, msgId);
        verify(wxClient).post(anyString(), anyString());
    }

    @Test
    public void testMpNewsToGroup() {
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"msg_id\":12345}");
        long msgId = mpMessages.mpNews(1, "testMpNews");
        assertEquals(12345, msgId);
        verify(wxClient).post(anyString(), anyString());
    }

    @Test
    public void testMpNewsToOpenIds() {
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"msg_id\":12345}");
        long msgId = mpMessages.mpNews(Arrays.asList("openId1", "openId2"), "testMpNews");
        assertEquals(12345, msgId);
        verify(wxClient).post(anyString(), anyString());
    }

    @Test
    public void testTextToAll() {
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"msg_id\":12345}");
        long msgId = mpMessages.text("testText");
        assertEquals(12345, msgId);
        verify(wxClient).post(anyString(), anyString());
    }

    @Test
    public void testTextToGroup() {
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"msg_id\":12345}");
        long msgId = mpMessages.text(1, "testText");
        assertEquals(12345, msgId);
        verify(wxClient).post(anyString(), anyString());
    }

    @Test
    public void testTextToOpenIds() {
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"msg_id\":12345}");
        long msgId = mpMessages.text(Arrays.asList("openId1", "openId2"), "testText");
        assertEquals(12345, msgId);
        verify(wxClient).post(anyString(), anyString());
    }

    @Test
    public void testDeleteMessage() {
        doNothing().when(wxClient).post(anyString(), anyString());
        mpMessages.delete(12345);
        verify(wxClient).post(anyString(), anyString());
    }

    @Test
    public void testSuccessMessage() {
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"msg_status\":\"SEND_SUCCESS\"}");
        boolean isSuccess = mpMessages.success(12345);
        assertTrue(isSuccess);
        verify(wxClient).post(anyString(), anyString());
    }

    @Test(expected = WxRuntimeException.class)
    public void testVideoPreviewFailure() {
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"error\":\"some error\"}");
        mpMessages.videoPreview("wxName", "openId", "mediaId", "title", "desc");
    }

    @Test
    public void testCardToAll() {
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"msg_id\":12345}");
        long msgId = mpMessages.card("testCard");
        assertEquals(12345, msgId);
        verify(wxClient).post(anyString(), anyString());
    }

    @Test
    public void testCardToGroup() {
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"msg_id\":12345}");
        long msgId = mpMessages.card(1, "testCard");
        assertEquals(12345, msgId);
        verify(wxClient).post(anyString(), anyString());
    }

    @Test
    public void testCardToOpenIds() {
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"msg_id\":12345}");
        long msgId = mpMessages.card(Arrays.asList("openId1", "openId2"), "testCard");
        assertEquals(12345, msgId);
        verify(wxClient).post(anyString(), anyString());
    }
}
