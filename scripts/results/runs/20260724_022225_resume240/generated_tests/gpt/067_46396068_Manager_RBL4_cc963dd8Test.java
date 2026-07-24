package com.kakao.hbase.manager;

import com.kakao.hbase.ManagerArgs;
import com.kakao.hbase.common.Args;
import com.kakao.hbase.common.InvalidTableException;
import com.kakao.hbase.manager.Manager;
import com.kakao.hbase.manager.command.Command;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class Manager_RBL4_cc963dd8Test {
    private Args args;
    private String commandName;
    private Manager manager;

    @Before
    public void setUp() throws Exception {
        args = mock(Args.class);
        commandName = "testCommand";
        manager = new Manager(args, commandName);
    }

    @Test
    public void testCommandSetInitialization() {
        Set<Class<? extends Command>> commandSet = Manager.getCommandSet();
        assertNotNull(commandSet);
        assertFalse(commandSet.isEmpty());
    }

    @Test
    public void testCommandExists() {
        assertTrue(Manager.commandExists("testCommand"));
        assertFalse(Manager.commandExists("nonExistentCommand"));
    }

    @Test
    public void testGetCommandUsage() throws Exception {
        Method usageMethod = Command.class.getDeclaredMethod("usage");
        usageMethod.setAccessible(true);
        String usage = (String) usageMethod.invoke(null);
        
        String commandUsage = Manager.getCommandUsage("testCommand");
        assertNotNull(commandUsage);
        assertEquals(usage, commandUsage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCommandUsageInvalidCommand() throws Exception {
        Manager.getCommandUsage("invalidCommand");
    }

    @Test
    public void testParseArgsValid() throws Exception {
        String[] validArgs = {"testCommand", "zookeeperQuorum"};
        Args parsedArgs = Manager.parseArgs(validArgs);
        assertNotNull(parsedArgs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseArgsInvalidCommand() throws Exception {
        String[] invalidArgs = {"invalidCommand"};
        Manager.parseArgs(invalidArgs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseArgsNoArguments() throws Exception {
        String[] noArgs = {};
        Manager.parseArgs(noArgs);
    }

    @Test
    public void testRun() throws Exception {
        HBaseAdmin admin = mock(HBaseAdmin.class);
        when(HBaseClient.getAdmin(args)).thenReturn(admin);
        
        Command command = mock(Command.class);
        when(command.getClass().getSimpleName()).thenReturn(commandName);
        when(manager.createCommand(commandName, admin, args)).thenReturn(command);
        
        manager.run();
        
        verify(command).run();
        verify(admin).close();
    }

    @Test(expected = InvalidTableException.class)
    public void testRunThrowsInvalidTableException() throws Exception {
        doThrow(new InvalidTableException("Invalid table")).when(manager).createCommand(anyString(), any(HBaseAdmin.class), any(Args.class));
        manager.run();
    }
}
