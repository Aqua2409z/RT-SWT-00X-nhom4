
package ru.qatools.gridrouter.sessions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import ru.qatools.gridrouter.config.Version;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WaitAvailableBrowsersCheckerTest {

    @Mock
    private StatsCounter statsCounter;

    @InjectMocks
    private WaitAvailableBrowsersChecker checker;

    private static final String USER = "testUser";
    private static final String BROWSER = "testBrowser";
    private static final String VERSION_NUMBER = "1.0";
    private static final int PERMITTED_COUNT = 2;
    private Version version;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        checker = new WaitAvailableBrowsersChecker(5, 1, statsCounter);
        version = mock(Version.class);
        when(version.getNumber()).thenReturn(VERSION_NUMBER);
        when(version.getPermittedCount()).thenReturn(PERMITTED_COUNT);
    }

    @Test
    public void testEnsureFreeBrowsersAvailable_WhenSessionsCountIsLessThanPermittedCount_ShouldFinishWithoutTimeout() {
        when(statsCounter.getSessionsCountForUserAndBrowser(USER, BROWSER, VERSION_NUMBER)).thenReturn(1);

        checker.ensureFreeBrowsersAvailable(USER, "remoteHost", BROWSER, version);

        verify(statsCounter, times(1)).getSessionsCountForUserAndBrowser(USER, BROWSER, VERSION_NUMBER);
    }

    @Test(expected = WaitAvailableBrowserTimeoutException.class)
    public void testEnsureFreeBrowsersAvailable_WhenTimeoutOccurs_ShouldThrowException() {
        when(statsCounter.getSessionsCountForUserAndBrowser(USER, BROWSER, VERSION_NUMBER)).thenReturn(PERMITTED_COUNT);

        // Simulate a long wait by making the method sleep longer than the timeout
        checker.ensureFreeBrowsersAvailable(USER, "remoteHost", BROWSER, version);
    }

    @Test
    public void testCountSessions() {
        when(statsCounter.getSessionsCountForUserAndBrowser(USER, BROWSER, VERSION_NUMBER)).thenReturn(1);
        int count = checker.countSessions(USER, BROWSER, version);
        assertEquals(1, count);
    }
}
