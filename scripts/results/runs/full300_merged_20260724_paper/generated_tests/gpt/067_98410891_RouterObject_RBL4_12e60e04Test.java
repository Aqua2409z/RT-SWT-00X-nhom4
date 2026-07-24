
package com.softavail.commsrouter.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RouterObject_RBL4_12e60e04Test {

    private RouterObject routerObject1;
    private RouterObject routerObject2;
    private RouterObject routerObject3;
    private Router router1;
    private Router router2;

    @Before
    public void setUp() {
        router1 = new Router("router1");
        router2 = new Router("router2");
        routerObject1 = new RouterObject("1");
        routerObject2 = new RouterObject("2");
        routerObject3 = new RouterObject(routerObject1);
        routerObject1.setRouter(router1);
        routerObject2.setRouter(router2);
    }

    @Test
    public void testGetRouter() {
        assertEquals(router1, routerObject1.getRouter());
        assertEquals(router2, routerObject2.getRouter());
    }

    @Test
    public void testSetRouter() {
        routerObject1.setRouter(router2);
        assertEquals(router2, routerObject1.getRouter());
    }

    @Test
    public void testToString() {
        assertEquals("router1:1", routerObject1.toString());
        assertEquals("router2:2", routerObject2.toString());
    }

    @Test
    public void testEquals() {
        assertTrue(routerObject1.equals(routerObject1));
        assertFalse(routerObject1.equals(routerObject2));
        routerObject1.setRouter(router2);
        assertFalse(routerObject1.equals(routerObject2));
        routerObject2.setRouter(router1);
        assertFalse(routerObject1.equals(routerObject2));
        routerObject3.setRouter(router1);
        assertTrue(routerObject1.equals(routerObject3));
    }

    @Test
    public void testHashCode() {
        int hash1 = routerObject1.hashCode();
        int hash2 = routerObject2.hashCode();
        assertNotEquals(hash1, hash2);
        routerObject2.setRouter(router1);
        assertEquals(hash1, routerObject2.hashCode());
    }
}
