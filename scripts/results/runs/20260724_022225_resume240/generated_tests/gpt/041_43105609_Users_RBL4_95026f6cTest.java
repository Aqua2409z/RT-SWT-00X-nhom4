
package com.riversoft.weixin.mp.user;

import com.riversoft.weixin.common.WxClient;
import com.riversoft.weixin.common.exception.WxRuntimeException;
import com.riversoft.weixin.mp.base.AppSetting;
import com.riversoft.weixin.mp.user.bean.User;
import com.riversoft.weixin.mp.user.bean.UserPagination;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class Users_RBL4_95026f6cTest {

    private Users users;
    private WxClient wxClient;

    @Before
    public void setUp() {
        wxClient = Mockito.mock(WxClient.class);
        users = new Users();
        users.setWxClient(wxClient);
    }

    @Test
    public void testGetUserSuccess() {
        String openId = "testOpenId";
        String jsonResponse = "{\"openid\":\"testOpenId\",\"nickname\":\"testUser\"}";
        when(wxClient.get(anyString())).thenReturn(jsonResponse);

        User user = users.get(openId);
        assertNotNull(user);
        assertEquals("testOpenId", user.getOpenid());
        assertEquals("testUser", user.getNickname());
    }

    @Test
    public void testGetUserNotFound() {
        String openId = "notFoundOpenId";
        when(wxClient.get(anyString())).thenThrow(new WxRuntimeException(40003, "User not found"));

        User user = users.get(openId);
        assertNull(user);
    }

    @Test(expected = WxRuntimeException.class)
    public void testGetUserThrowsException() {
        String openId = "errorOpenId";
        when(wxClient.get(anyString())).thenThrow(new WxRuntimeException(999, "Some error"));

        users.get(openId);
    }

    @Test
    public void testBatchGetUsersSuccess() {
        List<Map<String, String>> openIds = Arrays.asList(
                createOpenIdMap("openId1"),
                createOpenIdMap("openId2")
        );
        String jsonResponse = "{\"user_list\":[{\"openid\":\"openId1\",\"nickname\":\"user1\"},{\"openid\":\"openId2\",\"nickname\":\"user2\"}]}";
        when(wxClient.post(anyString(), anyString())).thenReturn(jsonResponse);

        List<User> usersList = users.batchGet(openIds);
        assertNotNull(usersList);
        assertEquals(2, usersList.size());
        assertEquals("openId1", usersList.get(0).getOpenid());
        assertEquals("user1", usersList.get(0).getNickname());
        assertEquals("openId2", usersList.get(1).getOpenid());
        assertEquals("user2", usersList.get(1).getNickname());
    }

    @Test(expected = WxRuntimeException.class)
    public void testBatchGetUsersTooMany() {
        String[] openIds = new String[101];
        Arrays.fill(openIds, "openId");
        users.batchGet(openIds);
    }

    @Test
    public void testListUsersSuccess() {
        String jsonResponse = "{\"total\":2,\"count\":2,\"data\":{\"openid\":[\"openId1\",\"openId2\"]},\"next_openid\":\"\"}";
        when(wxClient.get(anyString())).thenReturn(jsonResponse);

        UserPagination pagination = users.list();
        assertNotNull(pagination);
        assertEquals(2, pagination.getCount());
        assertEquals(2, pagination.getTotal());
    }

    @Test
    public void testRemarkUser() {
        String openId = "testOpenId";
        String remark = "testRemark";
        users.remark(openId, remark);
        verify(wxClient).post(anyString(), anyString());
    }

    private Map<String, String> createOpenIdMap(String openId) {
        Map<String, String> map = new HashMap<>();
        map.put("openid", openId);
        map.put("lang", "zh_CN");
        return map;
    }
}
