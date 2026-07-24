
package com.riversoft.weixin.mp.media;

import com.riversoft.weixin.common.WxClient;
import com.riversoft.weixin.common.exception.WxRuntimeException;
import com.riversoft.weixin.common.media.MpArticle;
import com.riversoft.weixin.common.media.MpNews;
import com.riversoft.weixin.common.media.Video;
import com.riversoft.weixin.mp.base.AppSetting;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class Materials_RBL4Test_a90c8766 {

    private WxClient wxClient;
    private Materials materials;

    @Before
    public void setUp() {
        wxClient = Mockito.mock(WxClient.class);
        materials = new Materials();
        materials.setWxClient(wxClient);
    }

    @Test
    public void testAddMpNewsImage() {
        String fileName = "test.jpg";
        String expectedUrl = "http://example.com/image.jpg";
        InputStream inputStream = new ByteArrayInputStream(new byte[1]);

        when(wxClient.post(anyString(), any(InputStream.class), anyString())).thenReturn("{\"url\":\"" + expectedUrl + "\"}");

        String url = materials.addMpNewsImage(inputStream, fileName);
        assertEquals(expectedUrl, url);
    }

    @Test(expected = WxRuntimeException.class)
    public void testAddMpNewsImageFailure() {
        String fileName = "test.jpg";
        InputStream inputStream = new ByteArrayInputStream(new byte[1]);

        when(wxClient.post(anyString(), any(InputStream.class), anyString())).thenReturn("{}");

        materials.addMpNewsImage(inputStream, fileName);
    }

    @Test
    public void testAddMpNews() {
        MpNews mpNews = new MpNews();
        String expectedMediaId = "media_id_123";
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"media_id\":\"" + expectedMediaId + "\"}");

        String mediaId = materials.addMpNews(mpNews);
        assertEquals(expectedMediaId, mediaId);
    }

    @Test(expected = WxRuntimeException.class)
    public void testAddMpNewsFailure() {
        MpNews mpNews = new MpNews();
        when(wxClient.post(anyString(), anyString())).thenReturn("{}");

        materials.addMpNews(mpNews);
    }

    @Test
    public void testGetMpNews() {
        String mediaId = "media_id_123";
        MpNews expectedMpNews = new MpNews();
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"title\":\"Test Title\"}");
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"media_id\":\"" + mediaId + "\"}");

        MpNews mpNews = materials.getMpNews(mediaId);
        assertNotNull(mpNews);
    }

    @Test
    public void testUpdateMpNews() {
        String mediaId = "media_id_123";
        int index = 0;
        MpArticle article = new MpArticle();
        materials.updateMpNews(mediaId, index, article);
        verify(wxClient, times(1)).post(anyString(), anyString());
    }

    @Test
    public void testAddVoice() {
        String fileName = "test.mp3";
        InputStream inputStream = new ByteArrayInputStream(new byte[1]);
        when(wxClient.post(anyString(), any(InputStream.class), anyString(), anyMap())).thenReturn("{\"media_id\":\"media_id_123\"}");

        Material material = materials.addVoice(inputStream, fileName);
        assertNotNull(material);
    }

    @Test
    public void testGetVoice() {
        String mediaId = "media_id_123";
        InputStream inputStream = materials.getVoice(mediaId);
        assertNotNull(inputStream);
    }

    @Test
    public void testDelete() {
        String mediaId = "media_id_123";
        materials.delete(mediaId);
        verify(wxClient, times(1)).post(anyString(), anyString());
    }

    @Test
    public void testCount() {
        when(wxClient.get(anyString())).thenReturn("{\"count\":10}");
        Counts counts = materials.count();
        assertNotNull(counts);
    }

    @Test
    public void testListMpNews() {
        int offset = 0;
        int count = 10;
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"item_count\":1,\"total_count\":1,\"item\":[{\"media_id\":\"media_id_123\"}]}");

        MpNewsPagination pagination = materials.listMpNews(offset, count);
        assertNotNull(pagination);
        assertEquals(1, pagination.getCurrentCount());
    }
}
