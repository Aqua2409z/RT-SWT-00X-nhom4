
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
        checker.queueTimeout = 5; // seconds
        checker.queueWaitInterval = 1; // seconds
        version = mock(Version.class);
        when(version.getNumber()).thenReturn(VERSION_NUMBER);
        when(version.getPermittedCount()).thenReturn(PERMITTED_COUNT);
    }

    @Test
    public void testEnsureFreeBrowsersAvailable_WhenSessionsAvailable_ShouldFinishWithoutTimeout() {
        when(statsCounter.getSessionsCountForUserAndBrowser(USER, BROWSER, VERSION_NUMBER)).thenReturn(1);

        checker.ensureFreeBrowsersAvailable(USER, "remoteHost", BROWSER, version);

        verify(statsCounter, times(1)).getSessionsCountForUserAndBrowser(USER, BROWSER, VERSION_NUMBER);
    }

    @Test(expected = WaitAvailableBrowserTimeoutException.class)
    public void testEnsureFreeBrowsersAvailable_WhenTimeout_ShouldThrowException() {
        when(statsCounter.getSessionsCountForUserAndBrowser(USER, BROWSER, VERSION_NUMBER)).thenReturn(PERMITTED_COUNT);

        checker.ensureFreeBrowsersAvailable(USER, "remoteHost", BROWSER, version);
    }

    @Test
    public void testOnWaitTimeout() {
        try {
            checker.onWaitTimeout(USER, BROWSER, version, "requestId", 1);
        } catch (WaitAvailableBrowserTimeoutException e) {
            // Expected exception
        }
    }

    @Test
    public void testOnWaitFinished() {
        checker.onWaitFinished(USER, BROWSER, version, "requestId", 1);
        // Verify log output if necessary
    }

    @Test
    public void testOnWait() {
        checker.onWait(USER, BROWSER, version, "requestId", 1);
        // Verify log output if necessary
    }
}
