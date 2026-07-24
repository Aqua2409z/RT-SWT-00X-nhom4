package com.iluwatar.hexagonal.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LotteryTicket_RBL4Test_81d1374a {

    private PlayerDetails playerDetails;
    private LotteryNumbers lotteryNumbers;
    private LotteryTicket lotteryTicket;

    @Before
    public void setUp() {
        playerDetails = new PlayerDetails("John Doe", "john.doe@example.com");
        lotteryNumbers = new LotteryNumbers(new int[]{1, 2, 3, 4, 5});
        lotteryTicket = LotteryTicket.create(playerDetails, lotteryNumbers);
    }

    @Test
    public void testCreateLotteryTicket() {
        assertNotNull(lotteryTicket);
        assertEquals(playerDetails, lotteryTicket.getPlayerDetails());
        assertEquals(lotteryNumbers, lotteryTicket.getNumbers());
    }

    @Test
    public void testGetPlayerDetails() {
        assertEquals(playerDetails, lotteryTicket.getPlayerDetails());
    }

    @Test
    public void testGetLotteryNumbers() {
        assertEquals(lotteryNumbers, lotteryTicket.getNumbers());
    }

    @Test
    public void testEquals() {
        LotteryTicket anotherTicket = LotteryTicket.create(playerDetails, lotteryNumbers);
        assertTrue(lotteryTicket.equals(anotherTicket));
    }

    @Test
    public void testHashCode() {
        LotteryTicket anotherTicket = LotteryTicket.create(playerDetails, lotteryNumbers);
        assertEquals(lotteryTicket.hashCode(), anotherTicket.hashCode());
    }

    @Test
    public void testNotEqualDifferentPlayerDetails() {
        PlayerDetails differentPlayerDetails = new PlayerDetails("Jane Doe", "jane.doe@example.com");
        LotteryTicket differentTicket = LotteryTicket.create(differentPlayerDetails, lotteryNumbers);
        assertTrue(!lotteryTicket.equals(differentTicket));
    }

    @Test
    public void testNotEqualDifferentLotteryNumbers() {
        LotteryNumbers differentLotteryNumbers = new LotteryNumbers(new int[]{6, 7, 8, 9, 10});
        LotteryTicket differentTicket = LotteryTicket.create(playerDetails, differentLotteryNumbers);
        assertTrue(!lotteryTicket.equals(differentTicket));
    }
}
