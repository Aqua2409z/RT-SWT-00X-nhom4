package com.iluwatar.hexagonal;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.iluwatar.hexagonal.App;
import com.iluwatar.hexagonal.administration.LotteryAdministration;
import com.iluwatar.hexagonal.administration.LotteryAdministrationImpl;
import com.iluwatar.hexagonal.service.LotteryService;
import com.iluwatar.hexagonal.service.LotteryServiceImpl;
import com.iluwatar.hexagonal.domain.LotteryTicket;
import com.iluwatar.hexagonal.domain.PlayerDetails;
import com.iluwatar.hexagonal.domain.LotteryNumbers;

public class App_RBL4_735c5bfeTest {

    private LotteryAdministration administration;
    private LotteryService lotteryService;

    @Before
    public void setUp() {
        administration = new LotteryAdministrationImpl();
        lotteryService = new LotteryServiceImpl();
    }

    @Test
    public void testMain() {
        // Test if the main method runs without exceptions
        try {
            App.main(new String[]{});
        } catch (Exception e) {
            fail("Main method threw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testSubmitTickets() {
        // Test ticket submission
        int initialTicketCount = lotteryService.getSubmittedTickets().size();
        App.submitTickets(lotteryService, 5);
        int newTicketCount = lotteryService.getSubmittedTickets().size();
        assertEquals(initialTicketCount + 5, newTicketCount);
    }

    @Test
    public void testGetRandomPlayerDetails() {
        // Test if random player details are fetched correctly
        PlayerDetails playerDetails = App.getRandomPlayerDetails();
        assertNotNull(playerDetails);
        assertTrue(App.allPlayerDetails.contains(playerDetails));
    }

    @Test
    public void testLotteryAdministrationReset() {
        // Test if the lottery administration resets correctly
        administration.resetLottery();
        assertTrue(administration.isLotteryReset());
    }

    @Test
    public void testLotteryServiceSubmitTicket() {
        // Test if a lottery ticket can be submitted
        LotteryTicket ticket = LotteryTicket.create(PlayerDetails.create("test@google.com", "123-456", "+123456789"), LotteryNumbers.createRandom());
        lotteryService.submitTicket(ticket);
        assertTrue(lotteryService.getSubmittedTickets().contains(ticket));
    }
}
