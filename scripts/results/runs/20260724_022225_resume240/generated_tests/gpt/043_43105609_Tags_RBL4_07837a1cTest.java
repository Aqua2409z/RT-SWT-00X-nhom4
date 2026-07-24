
package com.riversoft.weixin.mp.user;

import com.riversoft.weixin.common.WxClient;
import com.riversoft.weixin.mp.base.AppSetting;
import com.riversoft.weixin.mp.user.bean.Tag;
import com.riversoft.weixin.mp.user.bean.UserPagination;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class Tags_RBL4_07837a1cTest {

    private Tags tags;
    private WxClient wxClient;

    @Before
    public void setUp() {
        wxClient = Mockito.mock(WxClient.class);
        tags = new Tags();
        tags.setWxClient(wxClient);
    }

    @Test
    public void testCreate() {
        String tagName = "TestTag";
        Tag expectedTag = new Tag();
        expectedTag.setId(1);
        expectedTag.setName(tagName);
        
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"tag\":{\"id\":1,\"name\":\"TestTag\"}}");

        Tag createdTag = tags.create(tagName);
        
        assertNotNull(createdTag);
        assertEquals(expectedTag.getId(), createdTag.getId());
        assertEquals(expectedTag.getName(), createdTag.getName());
    }

    @Test
    public void testUpdate() {
        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("UpdatedTag");

        tags.update(tag);
        
        verify(wxClient).post(anyString(), anyString());
    }

    @Test
    public void testDelete() {
        int tagId = 1;

        tags.delete(tagId);
        
        verify(wxClient).post(anyString(), anyString());
    }

    @Test
    public void testList() {
        Tag tag1 = new Tag();
        tag1.setId(1);
        tag1.setName("Tag1");
        Tag tag2 = new Tag();
        tag2.setId(2);
        tag2.setName("Tag2");
        
        when(wxClient.get(anyString())).thenReturn("{\"tags\":[{\"id\":1,\"name\":\"Tag1\"},{\"id\":2,\"name\":\"Tag2\"}]}");

        List<Tag> tagsList = tags.list();
        
        assertNotNull(tagsList);
        assertEquals(2, tagsList.size());
        assertEquals("Tag1", tagsList.get(0).getName());
        assertEquals("Tag2", tagsList.get(1).getName());
    }

    @Test
    public void testListUsers() {
        int tagId = 1;
        UserPagination userPagination = new UserPagination();
        userPagination.setCount(2);
        userPagination.setOpenIds(Arrays.asList("user1", "user2"));
        
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"count\":2,\"data\":{\"openid\":[\"user1\",\"user2\"]}}");

        UserPagination result = tags.listUsers(tagId);
        
        assertNotNull(result);
        assertEquals(2, result.getCount());
        assertEquals(Arrays.asList("user1", "user2"), result.getOpenIds());
    }

    @Test
    public void testTagUsers() {
        int tagId = 1;
        List<String> openIds = Arrays.asList("user1", "user2");

        tags.tagUsers(tagId, openIds);
        
        verify(wxClient).post(anyString(), anyString());
    }

    @Test
    public void testUnTagUsers() {
        int tagId = 1;
        List<String> openIds = Arrays.asList("user1", "user2");

        tags.unTagUsers(tagId, openIds);
        
        verify(wxClient).post(anyString(), anyString());
    }

    @Test
    public void testGetUserTags() {
        String openId = "user1";
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"tagid_list\":[1,2]}");

        List<Integer> tagIds = tags.getUserTags(openId);
        
        assertNotNull(tagIds);
        assertEquals(2, tagIds.size());
        assertEquals(Arrays.asList(1, 2), tagIds);
    }
}
