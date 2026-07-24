
package com.iluwatar.hexagonal.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class LotteryTicket_RBL4Test_81d1374a {

    @Test
    public void testCreateLotteryTicket() {
        PlayerDetails playerDetails = new PlayerDetails("John Doe", "john.doe@example.com");
        LotteryNumbers lotteryNumbers = new LotteryNumbers(new int[]{1, 2, 3, 4, 5});
        
        LotteryTicket ticket = LotteryTicket.create(playerDetails, lotteryNumbers);
        
        assertNotNull(ticket);
        assertEquals(playerDetails, ticket.getPlayerDetails());
        assertEquals(lotteryNumbers, ticket.getNumbers());
    }

    @Test
    public void testEqualsAndHashCode() {
        PlayerDetails playerDetails1 = new PlayerDetails("John Doe", "john.doe@example.com");
        LotteryNumbers lotteryNumbers1 = new LotteryNumbers(new int[]{1, 2, 3, 4, 5});
        LotteryTicket ticket1 = LotteryTicket.create(playerDetails1, lotteryNumbers1);
        
        PlayerDetails playerDetails2 = new PlayerDetails("John Doe", "john.doe@example.com");
        LotteryNumbers lotteryNumbers2 = new LotteryNumbers(new int[]{1, 2, 3, 4, 5});
        LotteryTicket ticket2 = LotteryTicket.create(playerDetails2, lotteryNumbers2);
        
        assertEquals(ticket1, ticket2);
        assertEquals(ticket1.hashCode(), ticket2.hashCode());
        
        PlayerDetails playerDetails3 = new PlayerDetails("Jane Doe", "jane.doe@example.com");
        LotteryNumbers lotteryNumbers3 = new LotteryNumbers(new int[]{6, 7, 8, 9, 10});
        LotteryTicket ticket3 = LotteryTicket.create(playerDetails3, lotteryNumbers3);
        
        assertNotEquals(ticket1, ticket3);
        assertNotEquals(ticket1.hashCode(), ticket3.hashCode());
    }
}
