
package random;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReservoirSamplingMultipleItems_RBL4Test_01a0a377 {

    @Test
    public void testReservoirSamplingWithLessElementsThanReservoirSize() {
        List<Integer> input = Arrays.asList(1, 2, 3);
        Iterator<Integer> iterator = input.iterator();
        List<Integer> result = ReservoirSamplingMultipleItems.reservoirSampling(iterator, 5);
        
        assertEquals(3, result.size());
        assertTrue(result.containsAll(input));
    }

    @Test
    public void testReservoirSamplingWithEqualElementsAndReservoirSize() {
        List<Integer> input = Arrays.asList(1, 1, 1, 1, 1);
        Iterator<Integer> iterator = input.iterator();
        List<Integer> result = ReservoirSamplingMultipleItems.reservoirSampling(iterator, 5);
        
        assertEquals(5, result.size());
        assertTrue(result.stream().allMatch(e -> e.equals(1)));
    }

    @Test
    public void testReservoirSamplingWithMoreElementsThanReservoirSize() {
        List<Integer> input = Stream.iterate(1, n -> n + 1).limit(100).collect(Collectors.toList());
        Iterator<Integer> iterator = input.iterator();
        List<Integer> result = ReservoirSamplingMultipleItems.reservoirSampling(iterator, 10);
        
        assertEquals(10, result.size());
        assertTrue(input.containsAll(result));
    }

    @Test
    public void testReservoirSamplingWithZeroReservoirSize() {
        List<Integer> input = Arrays.asList(1, 2, 3, 4, 5);
        Iterator<Integer> iterator = input.iterator();
        List<Integer> result = ReservoirSamplingMultipleItems.reservoirSampling(iterator, 0);
        
        assertEquals(0, result.size());
    }

    @Test
    public void testReservoirSamplingWithEmptyStream() {
        List<Integer> input = Arrays.asList();
        Iterator<Integer> iterator = input.iterator();
        List<Integer> result = ReservoirSamplingMultipleItems.reservoirSampling(iterator, 5);
        
        assertEquals(0, result.size());
    }
}
