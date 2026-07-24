
package com.riversoft.weixin.mp.user;

import com.riversoft.weixin.common.WxClient;
import com.riversoft.weixin.mp.base.AppSetting;
import com.riversoft.weixin.mp.user.bean.UserPagination;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BlackLists_RBL4_840860a6Test {

    private BlackLists blackLists;
    private WxClient wxClient;

    @Before
    public void setUp() {
        wxClient = Mockito.mock(WxClient.class);
        blackLists = new BlackLists();
        blackLists.setWxClient(wxClient);
    }

    @Test
    public void testBlack() {
        List<String> openids = Arrays.asList("openid1", "openid2");
        blackLists.black(openids);

        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        verify(wxClient, times(1)).post(urlCaptor.capture(), bodyCaptor.capture());

        assertEquals("url.blacklist.black", urlCaptor.getValue());
        assertEquals("{\"opened_list\":[\"openid1\",\"openid2\"]}", bodyCaptor.getValue());
    }

    @Test
    public void testUnblack() {
        List<String> openids = Arrays.asList("openid1", "openid2");
        blackLists.unblack(openids);

        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        verify(wxClient, times(1)).post(urlCaptor.capture(), bodyCaptor.capture());

        assertEquals("url.blacklist.unblack", urlCaptor.getValue());
        assertEquals("{\"opened_list\":[\"openid1\",\"openid2\"]}", bodyCaptor.getValue());
    }

    @Test
    public void testList() {
        String nextOpenId = "nextOpenId";
        String responseJson = "{\"total\":1,\"count\":1,\"data\":{\"openid\":[\"openid1\"]},\"next_openid\":\"\"}";
        when(wxClient.post(anyString(), anyString())).thenReturn(responseJson);

        UserPagination result = blackLists.list(nextOpenId);

        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        verify(wxClient, times(1)).post(urlCaptor.capture(), bodyCaptor.capture());

        assertEquals("url.blacklist.list", urlCaptor.getValue());
        assertEquals("{\"begin_openid\":\"nextOpenId\"}", bodyCaptor.getValue());
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getCount());
        assertEquals(Arrays.asList("openid1"), result.getData().getOpenid());
    }

    @Test
    public void testListWithoutNextOpenId() {
        String responseJson = "{\"total\":0,\"count\":0,\"data\":{\"openid\":[]},\"next_openid\":\"\"}";
        when(wxClient.post(anyString(), anyString())).thenReturn(responseJson);

        UserPagination result = blackLists.list();

        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        verify(wxClient, times(1)).post(urlCaptor.capture(), bodyCaptor.capture());

        assertEquals("url.blacklist.list", urlCaptor.getValue());
        assertEquals("{\"begin_openid\":\"\"}", bodyCaptor.getValue());
        assertEquals(0, result.getTotal());
        assertEquals(0, result.getCount());
        assertEquals(Arrays.asList(), result.getData().getOpenid());
    }
}
