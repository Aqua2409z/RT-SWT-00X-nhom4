
package com.riversoft.weixin.mp.menu;

import com.riversoft.weixin.common.WxClient;
import com.riversoft.weixin.common.exception.WxRuntimeException;
import com.riversoft.weixin.common.menu.Menu;
import com.riversoft.weixin.common.menu.RuleMenu;
import com.riversoft.weixin.mp.base.AppSetting;
import com.riversoft.weixin.mp.base.WxEndpoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class Menus_RBL4_2abcc2c1Test {

    private Menus menus;
    private WxClient wxClient;

    @Before
    public void setUp() {
        wxClient = mock(WxClient.class);
        menus = new Menus();
        menus.setWxClient(wxClient);
    }

    @Test
    public void testCreate() {
        Menu menu = new Menu();
        menus.create(menu);
        String url = WxEndpoint.get("url.menu.create");
        String json = "{\"key\":\"value\"}"; // Assuming this is the expected JSON representation
        when(wxClient.post(url, json)).thenReturn(null);
        verify(wxClient).post(url, json);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateRuleMenuWithoutRule() {
        RuleMenu ruleMenu = new RuleMenu();
        menus.createRuleMenu(ruleMenu);
    }

    @Test
    public void testCreateRuleMenu() {
        RuleMenu ruleMenu = new RuleMenu();
        ruleMenu.setRule(new Object()); // Assuming a valid rule object
        String expectedMenuId = "12345";
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"menuid\":\"" + expectedMenuId + "\"}");

        String menuId = menus.createRuleMenu(ruleMenu);
        assertEquals(expectedMenuId, menuId);
    }

    @Test(expected = WxRuntimeException.class)
    public void testCreateRuleMenuFailed() {
        RuleMenu ruleMenu = new RuleMenu();
        ruleMenu.setRule(new Object());
        when(wxClient.post(anyString(), anyString())).thenReturn("{}");

        menus.createRuleMenu(ruleMenu);
    }

    @Test
    public void testDelete() {
        menus.delete();
        String url = WxEndpoint.get("url.menu.delete");
        verify(wxClient).get(url);
    }

    @Test
    public void testDeleteWithMenuId() {
        String menuId = "12345";
        menus.delete(menuId);
        String url = WxEndpoint.get("url.menu.delete.condition");
        String json = String.format("{\"menuid\":\"%s\"}", menuId);
        verify(wxClient).post(url, json);
    }

    @Test
    public void testGet() {
        String response = "{\"menu\":{}}"; // Assuming a valid response
        when(wxClient.get(anyString())).thenReturn(response);
        Menu menu = menus.get();
        assertNotNull(menu);
    }

    @Test
    public void testGetRuleMenus() {
        String response = "{\"conditionalmenu\":[]}";
        when(wxClient.get(anyString())).thenReturn(response);
        List<RuleMenu> ruleMenus = menus.getRuleMenus();
        assertNotNull(ruleMenus);
        assertTrue(ruleMenus.isEmpty());
    }

    @Test
    public void testGetMenuConfig() {
        String response = "{}"; // Assuming a valid response
        when(wxClient.get(anyString())).thenReturn(response);
        MenuConfig menuConfig = menus.getMenuConfig();
        assertNotNull(menuConfig);
    }

    @Test
    public void testMatch() {
        String userId = "user123";
        String response = "{\"menu\":{}}"; // Assuming a valid response
        when(wxClient.post(anyString(), anyString())).thenReturn(response);
        Menu menu = menus.match(userId);
        assertNotNull(menu);
    }

    @Test
    public void testList() {
        String response = "{\"menu\":{},\"conditionalmenu\":[]}";
        when(wxClient.get(anyString())).thenReturn(response);
        List<Menu> menuList = menus.list();
        assertNotNull(menuList);
        assertEquals(1, menuList.size());
    }
}
