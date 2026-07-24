
package com.riversoft.weixin.mp.card;

import com.riversoft.weixin.common.WxClient;
import com.riversoft.weixin.common.exception.WxRuntimeException;
import com.riversoft.weixin.mp.base.AppSetting;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class Cards_RBL4_1f16a0efTest {

    private Cards cards;
    private WxClient wxClient;

    @Before
    public void setUp() {
        wxClient = Mockito.mock(WxClient.class);
        cards = new Cards();
        cards.setWxClient(wxClient);
    }

    @Test
    public void testGroupon() {
        Groupon groupon = new Groupon();
        // Set properties for groupon as needed
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"card_id\":\"12345\"}");

        String cardId = cards.groupon(groupon);
        assertEquals("12345", cardId);
    }

    @Test
    public void testCash() {
        Cash cash = new Cash();
        // Set properties for cash as needed
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"card_id\":\"12345\"}");

        String cardId = cards.cash(cash);
        assertEquals("12345", cardId);
    }

    @Test
    public void testGift() {
        Gift gift = new Gift();
        // Set properties for gift as needed
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"card_id\":\"12345\"}");

        String cardId = cards.gift(gift);
        assertEquals("12345", cardId);
    }

    @Test
    public void testDiscount() {
        Discount discount = new Discount();
        // Set properties for discount as needed
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"card_id\":\"12345\"}");

        String cardId = cards.discount(discount);
        assertEquals("12345", cardId);
    }

    @Test
    public void testCoupon() {
        Coupon coupon = new Coupon();
        // Set properties for coupon as needed
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"card_id\":\"12345\"}");

        String cardId = cards.coupon(coupon);
        assertEquals("12345", cardId);
    }

    @Test
    public void testMember() {
        Member member = new Member();
        // Set properties for member as needed
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"card_id\":\"12345\"}");

        String cardId = cards.member(member);
        assertEquals("12345", cardId);
    }

    @Test
    public void testCount() {
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"total_num\":5}");

        int count = cards.count(Collections.emptyList());
        assertEquals(5, count);
    }

    @Test
    public void testList() {
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"card_id_list\":[\"12345\", \"67890\"]}");

        List<String> cardIds = cards.list(0, 2, Collections.emptyList());
        assertEquals(2, cardIds.size());
        assertTrue(cardIds.contains("12345"));
        assertTrue(cardIds.contains("67890"));
    }

    @Test
    public void testGet() {
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"card\":{\"card_id\":\"12345\"}}");

        Card card = cards.get("12345");
        assertNotNull(card);
        assertEquals("12345", card.getCardId());
    }

    @Test
    public void testSetWhiteList() {
        List<String> openIds = Arrays.asList("openId1", "openId2");
        List<String> userNames = Arrays.asList("userName1", "userName2");

        cards.setWhiteList(openIds, userNames);
        verify(wxClient).post(anyString(), anyString());
    }

    @Test
    public void testGetContentByCardId() {
        when(wxClient.post(anyString(), anyString())).thenReturn("{\"content\":\"some content\"}");

        String content = cards.getContentByCardId("12345");
        assertEquals("some content", content);
    }

    @Test
    public void testListColors() {
        when(wxClient.get(anyString())).thenReturn("{\"colors\":[]}");

        List<Color> colors = cards.listColors();
        assertNotNull(colors);
        assertTrue(colors.isEmpty());
    }

    @Test(expected = WxRuntimeException.class)
    public void testGrouponFailure() {
        Groupon groupon = new Groupon();
        when(wxClient.post(anyString(), anyString())).thenReturn("{}");

        cards.groupon(groupon);
    }

    @Test(expected = WxRuntimeException.class)
    public void testCountFailure() {
        when(wxClient.post(anyString(), anyString())).thenReturn("{}");

        cards.count(Collections.emptyList());
    }

    @Test(expected = WxRuntimeException.class)
    public void testListFailure() {
        when(wxClient.post(anyString(), anyString())).thenReturn("{}");

        cards.list(0, 2, Collections.emptyList());
    }

    @Test(expected = WxRuntimeException.class)
    public void testGetFailure() {
        when(wxClient.post(anyString(), anyString())).thenReturn("{}");

        cards.get("12345");
    }

    @Test(expected = WxRuntimeException.class)
    public void testGetContentByCardIdFailure() {
        when(wxClient.post(anyString(), anyString())).thenReturn("{}");

        cards.getContentByCardId("12345");
    }
}
