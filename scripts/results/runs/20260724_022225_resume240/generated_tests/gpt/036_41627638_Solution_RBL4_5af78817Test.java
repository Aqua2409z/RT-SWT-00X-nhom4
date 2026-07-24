
package com.github.WordLadder;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Solution_RBL4_5af78817Test {

    @Test
    public void testFindLadders_Success() {
        Solution solution = new Solution();
        Set<String> wordList = new HashSet<>(Arrays.asList("hot", "dot", "dog", "lot", "log"));
        List<List<String>> result = solution.findLadders("hit", "cog", wordList);
        
        List<List<String>> expected = Arrays.asList(
                Arrays.asList("hit", "hot", "dot", "dog", "cog"),
                Arrays.asList("hit", "hot", "lot", "log", "cog")
        );

        assertTrue(result.size() > 0);
        assertTrue(result.containsAll(expected));
    }

    @Test
    public void testFindLadders_NoPath() {
        Solution solution = new Solution();
        Set<String> wordList = new HashSet<>(Arrays.asList("hot", "dot", "dog"));
        List<List<String>> result = solution.findLadders("hit", "cog", wordList);
        
        assertEquals(0, result.size());
    }

    @Test
    public void testFindLadders_SingleStep() {
        Solution solution = new Solution();
        Set<String> wordList = new HashSet<>(Arrays.asList("b"));
        List<List<String>> result = solution.findLadders("a", "b", wordList);
        
        List<List<String>> expected = Arrays.asList(
                Arrays.asList("a", "b")
        );

        assertEquals(1, result.size());
        assertEquals(expected, result);
    }

    @Test
    public void testIsOneLetterDiff() {
        assertTrue(Solution.isOneLetterDiff("hit", "hot"));
        assertTrue(Solution.isOneLetterDiff("hot", "dot"));
        assertTrue(Solution.isOneLetterDiff("dot", "dog"));
        assertTrue(Solution.isOneLetterDiff("lot", "log"));
        assertTrue(Solution.isOneLetterDiff("a", "b"));
        assertTrue(Solution.isOneLetterDiff("abc", "bbc"));
        
        assertTrue(!Solution.isOneLetterDiff("hit", "dot"));
        assertTrue(!Solution.isOneLetterDiff("hot", "dog"));
        assertTrue(!Solution.isOneLetterDiff("dot", "log"));
        assertTrue(!Solution.isOneLetterDiff("abc", "def"));
    }

    @Test
    public void testOneLetterDiffsOf() {
        List<String> diffs = Solution.oneLetterDiffsOf("hit");
        Set<String> expectedDiffs = new HashSet<>(Arrays.asList("ait", "bit", "cit", "dit", "eit", "fit", "git", "hit", "kit", "lit", "mit", "nit", "oit", "pit", "qit", "rit", "sit", "tit", "uit", "vit", "wit", "xit", "yit", "zit", "hat", "hbt", "hct", "hdt", "het", "hft", "hgt", "hht", "hit", "hjt", "hkt", "hlt", "hmt", "hnt", "hot", "hpt", "hqt", "hrt", "hst", "htt", "hut", "hvt", "hwt", "hxt", "hyt", "hzt", "hia", "hib", "hic", "hid", "hie", "hif", "hig", "hih", "hij", "hik", "hil", "him", "hin", "hio", "hip", "hiq", "hir", "his", "hit", "hiu", "hiv", "hiw", "hix", "hiy", "hiz"));
        
        assertTrue(diffs.containsAll(expectedDiffs));
    }
}
