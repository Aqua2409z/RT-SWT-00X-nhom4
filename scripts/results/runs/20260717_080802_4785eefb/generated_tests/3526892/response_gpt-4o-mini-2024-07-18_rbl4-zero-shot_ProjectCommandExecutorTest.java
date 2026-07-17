
package com.datascience.executor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;

public class ProjectCommandExecutorTest {

    private ProjectCommandExecutor executor;
    private IExecutorCommand command;

    @Before
    public void setUp() {
        executor = new ProjectCommandExecutor(2);
        command = mock(IExecutorCommand.class);
    }

    @After
    public void tearDown() throws InterruptedException {
        executor.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testAddCommandWhenStopped() throws InterruptedException {
        executor.stop();
        executor.add(command);
    }

    @Test
    public void testAddCommandCanStart() {
        when(command.canStart()).thenReturn(true);
        executor.add(command);
        verify(command, times(1)).cleanup();
    }

    @Test
    public void testAddCommandCannotStart() {
        when(command.canStart()).thenReturn(false);
        executor.add(command);
        verify(command, never()).cleanup();
    }

    @Test
    public void testExecutePossibleCommands() {
        IExecutorCommand command1 = mock(IExecutorCommand.class);
        IExecutorCommand command2 = mock(IExecutorCommand.class);
        
        when(command1.canStart()).thenReturn(false);
        when(command2.canStart()).thenReturn(true);
        
        executor.add(command1);
        executor.add(command2);
        
        executor.add(command1);
        verify(command1, never()).cleanup();
        verify(command2, times(1)).cleanup();
    }

    @Test
    public void testStopExecutor() throws InterruptedException {
        executor.stop();
        // Verify that the executor has been shut down properly
        // This can be done by checking logs or ensuring no tasks are running
    }

    @Test
    public void testCommandCleanerOnSuccess() {
        when(command.canStart()).thenReturn(true);
        executor.add(command);
        verify(command, times(1)).cleanup();
    }

    @Test
    public void testCommandCleanerOnFailure() {
        when(command.canStart()).thenReturn(true);
        executor.add(command);
        CommandCleaner cleaner = executor.new CommandCleaner(command);
        cleaner.onFailure(new RuntimeException("Test Exception"));
        verify(command, times(1)).cleanup();
    }
}
