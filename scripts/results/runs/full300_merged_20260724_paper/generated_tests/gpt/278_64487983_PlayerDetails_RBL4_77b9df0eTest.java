
package com.iluwatar.hexagonal.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerDetails_RBL4_77b9df0eTest {

    @Test
    public void testCreate() {
        PlayerDetails player = PlayerDetails.create("test@example.com", "123456789", "123-456-7890");
        assertNotNull(player);
        assertEquals("test@example.com", player.getEmail());
        assertEquals("123456789", player.getBankAccount());
        assertEquals("123-456-7890", player.getPhoneNumber());
    }

    @Test
    public void testEqualsAndHashCode() {
        PlayerDetails player1 = PlayerDetails.create("test@example.com", "123456789", "123-456-7890");
        PlayerDetails player2 = PlayerDetails.create("test@example.com", "123456789", "123-456-7890");
        PlayerDetails player3 = PlayerDetails.create("other@example.com", "987654321", "098-765-4321");

        assertEquals(player1, player2);
        assertNotEquals(player1, player3);
        assertEquals(player1.hashCode(), player2.hashCode());
        assertNotEquals(player1.hashCode(), player3.hashCode());
    }

    @Test
    public void testEqualsWithNull() {
        PlayerDetails player = PlayerDetails.create("test@example.com", "123456789", "123-456-7890");
        assertNotEquals(player, null);
    }

    @Test
    public void testEqualsWithDifferentClass() {
        PlayerDetails player = PlayerDetails.create("test@example.com", "123456789", "123-456-7890");
        assertNotEquals(player, new Object());
    }

    @Test
    public void testEqualsWithSameInstance() {
        PlayerDetails player = PlayerDetails.create("test@example.com", "123456789", "123-456-7890");
        assertEquals(player, player);
    }
}
