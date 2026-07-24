
package com.iluwatar.hexagonal.domain;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class LotteryNumbers_RBL4_eef37d9aTest {

    @Test
    public void testCreateRandom() {
        LotteryNumbers lotteryNumbers = LotteryNumbers.createRandom();
        Set<Integer> numbers = lotteryNumbers.getNumbers();
        assertEquals(4, numbers.size());
        for (Integer number : numbers) {
            assertTrue(number >= LotteryNumbers.MIN_NUMBER && number <= LotteryNumbers.MAX_NUMBER);
        }
    }

    @Test
    public void testCreateWithGivenNumbers() {
        Set<Integer> givenNumbers = new HashSet<>();
        givenNumbers.add(1);
        givenNumbers.add(5);
        givenNumbers.add(10);
        givenNumbers.add(15);
        
        LotteryNumbers lotteryNumbers = LotteryNumbers.create(givenNumbers);
        Set<Integer> numbers = lotteryNumbers.getNumbers();
        
        assertEquals(4, numbers.size());
        assertTrue(numbers.containsAll(givenNumbers));
    }

    @Test
    public void testGetNumbersImmutable() {
        LotteryNumbers lotteryNumbers = LotteryNumbers.createRandom();
        Set<Integer> numbers = lotteryNumbers.getNumbers();
        
        try {
            numbers.add(21);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testEqualsAndHashCode() {
        LotteryNumbers lotteryNumbers1 = LotteryNumbers.createRandom();
        LotteryNumbers lotteryNumbers2 = LotteryNumbers.create(lotteryNumbers1.getNumbers());

        assertEquals(lotteryNumbers1, lotteryNumbers2);
        assertEquals(lotteryNumbers1.hashCode(), lotteryNumbers2.hashCode());

        LotteryNumbers lotteryNumbers3 = LotteryNumbers.createRandom();
        assertNotEquals(lotteryNumbers1, lotteryNumbers3);
    }

    @Test
    public void testRandomNumberGeneration() {
        LotteryNumbers lotteryNumbers = LotteryNumbers.createRandom();
        Set<Integer> numbers = lotteryNumbers.getNumbers();
        
        assertEquals(4, numbers.size());
        for (Integer number : numbers) {
            assertTrue(number >= LotteryNumbers.MIN_NUMBER && number <= LotteryNumbers.MAX_NUMBER);
        }
    }
}
