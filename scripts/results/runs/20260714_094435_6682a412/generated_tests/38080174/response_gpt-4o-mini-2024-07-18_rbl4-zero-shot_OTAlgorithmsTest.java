
package io.datakernel.ot;

import io.datakernel.async.function.AsyncPredicate;
import io.datakernel.promise.Promise;
import io.datakernel.promise.SettablePromise;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OTAlgorithmsTest {

    @Test
    public void testReduce() {
        OTRepository<String, String> repository = mock(OTRepository.class);
        OTSystem<String> system = mock(OTSystem.class);
        Set<String> heads = new HashSet<>(Arrays.asList("head1", "head2"));
        GraphReducer<String, String, String> reducer = mock(GraphReducer.class);

        when(repository.loadCommit("head1")).thenReturn(Promise.of(new OTCommit<>("head1", 1, Collections.emptyMap())));
        when(repository.loadCommit("head2")).thenReturn(Promise.of(new OTCommit<>("head2", 1, Collections.emptyMap())));
        when(reducer.onStart(any())).thenReturn(null);

        Promise<String> result = OTAlgorithms.reduce(repository, system, heads, reducer);
        result.whenComplete((res, e) -> {
            assertNotNull(res);
            assertEquals("expectedResult", res);
        });
    }

    @Test
    public void testFindParent() {
        OTRepository<String, String> repository = mock(OTRepository.class);
        OTSystem<String> system = mock(OTSystem.class);
        Set<String> startNodes = new HashSet<>(Collections.singletonList("startNode"));
        DiffsReducer<String, String> diffsReducer = mock(DiffsReducer.class);
        AsyncPredicate<OTCommit<String, String>> matchPredicate = commit -> Promise.of(true);

        when(repository.loadCommit("startNode")).thenReturn(Promise.of(new OTCommit<>("startNode", 1, Collections.emptyMap())));

        Promise<OTAlgorithms.FindResult<String, String>> result = OTAlgorithms.findParent(repository, system, startNodes, diffsReducer, matchPredicate);
        result.whenComplete((res, e) -> {
            assertNotNull(res);
            assertEquals("startNode", res.getCommit());
        });
    }

    @Test
    public void testMergeAndPush() {
        OTRepository<String, String> repository = mock(OTRepository.class);
        OTSystem<String> system = mock(OTSystem.class);
        Set<String> heads = new HashSet<>(Arrays.asList("head1", "head2"));

        when(repository.getHeads()).thenReturn(Promise.of(heads));
        when(repository.push(any())).thenReturn(Promise.of(new OTCommit<>("mergedCommit", 1, Collections.emptyMap())));

        Promise<String> result = OTAlgorithms.mergeAndPush(repository, system);
        result.whenComplete((res, e) -> {
            assertNotNull(res);
            assertEquals("mergedCommit", res);
        });
    }

    @Test
    public void testFindCut() {
        OTRepository<String, String> repository = mock(OTRepository.class);
        OTSystem<String> system = mock(OTSystem.class);
        Set<String> startNodes = new HashSet<>(Arrays.asList("node1", "node2"));
        Predicate<Collection<OTCommit<String, String>>> matchPredicate = commits -> true;

        when(repository.loadCommit("node1")).thenReturn(Promise.of(new OTCommit<>("node1", 1, Collections.emptyMap())));
        when(repository.loadCommit("node2")).thenReturn(Promise.of(new OTCommit<>("node2", 1, Collections.emptyMap())));

        Promise<Set<String>> result = OTAlgorithms.findCut(repository, system, startNodes, matchPredicate);
        result.whenComplete((res, e) -> {
            assertNotNull(res);
            assertTrue(res.contains("node1"));
            assertTrue(res.contains("node2"));
        });
    }

    @Test
    public void testCheckout() {
        OTRepository<String, String> repository = mock(OTRepository.class);
        OTSystem<String> system = mock(OTSystem.class);
        String commitId = "commitId";

        when(repository.getHeads()).thenReturn(Promise.of(new HashSet<>(Collections.singletonList("head"))));
        when(repository.loadSnapshot(commitId)).thenReturn(Promise.of(Optional.of(Arrays.asList("diff1", "diff2"))));

        Promise<List<String>> result = OTAlgorithms.checkout(repository, system, commitId);
        result.whenComplete((res, e) -> {
            assertNotNull(res);
            assertEquals(2, res.size());
            assertTrue(res.contains("diff1"));
            assertTrue(res.contains("diff2"));
        });
    }

    @Test
    public void testSaveSnapshot() {
        OTRepository<String, String> repository = mock(OTRepository.class);
        OTSystem<String> system = mock(OTSystem.class);
        String revisionId = "revisionId";

        when(repository.saveSnapshot(eq(revisionId), any())).thenReturn(Promise.of(null));
        when(repository.getHeads()).thenReturn(Promise.of(new HashSet<>(Collections.singletonList("head"))));
        when(repository.loadSnapshot(any())).thenReturn(Promise.of(Optional.of(Arrays.asList("diff1", "diff2"))));

        Promise<Void> result = OTAlgorithms.saveSnapshot(repository, system, revisionId);
        result.whenComplete((res, e) -> {
            assertNull(res);
        });
    }
}
