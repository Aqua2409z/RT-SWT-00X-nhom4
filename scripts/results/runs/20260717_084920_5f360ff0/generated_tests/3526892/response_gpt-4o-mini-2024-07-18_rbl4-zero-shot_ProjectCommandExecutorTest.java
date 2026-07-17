
package com.datascience.executor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
        executor.executePossibleCommands();
        verify(command, never()).cleanup();
    }

    @Test
    public void testStopExecutor() throws InterruptedException {
        executor.stop();
        assertFalse(executor.isAlive);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddCommandAfterStop() throws InterruptedException {
        executor.stop();
        executor.add(command);
    }

    @Test
    public void testCommandCleanerOnSuccess() {
        when(command.canStart()).thenReturn(true);
        executor.add(command);
        executor.executePossibleCommands();
        verify(command, times(1)).cleanup();
    }

    @Test
    public void testCommandCleanerOnFailure() {
        when(command.canStart()).thenReturn(true);
        executor.add(command);
        executor.executePossibleCommands();
        doThrow(new RuntimeException()).when(command).cleanup();
        executor.executePossibleCommands();
        verify(command, times(1)).cleanup();
    }

    @Test
    public void testMultipleCommandsExecution() throws InterruptedException {
        IExecutorCommand command1 = mock(IExecutorCommand.class);
        IExecutorCommand command2 = mock(IExecutorCommand.class);
        
        when(command1.canStart()).thenReturn(true);
        when(command2.canStart()).thenReturn(true);
        
        executor.add(command1);
        executor.add(command2);
        
        TimeUnit.SECONDS.sleep(1); // Wait for commands to execute
        
        verify(command1, times(1)).cleanup();
        verify(command2, times(1)).cleanup();
    }
}
