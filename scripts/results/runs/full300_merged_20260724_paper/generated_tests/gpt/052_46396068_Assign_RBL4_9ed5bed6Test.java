package com.kakao.hbase.manager.command;

import com.kakao.hbase.common.Args;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class Assign_RBL4_9ed5bed6Test {
    private HBaseAdmin admin;
    private Args args;

    @Before
    public void setUp() {
        admin = Mockito.mock(HBaseAdmin.class);
        args = Mockito.mock(Args.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInsufficientArguments() {
        when(args.getOptionSet().nonOptionArguments()).thenReturn(new ArrayList<>());
        new Assign(admin, args);
    }

    @Test
    public void testConstructorWithValidArguments() {
        when(args.getOptionSet().nonOptionArguments()).thenReturn(Arrays.asList("arg1", "arg2"));
        Assign assign = new Assign(admin, args);
        assertNotNull(assign);
    }

    @Test
    public void testUsage() {
        String usage = Assign.usage();
        assertNotNull(usage);
        assertTrue(usage.contains("Manage assignment of regions."));
        assertTrue(usage.contains("usage: assign <zookeeper quorum> <action> [options]"));
    }

    @Test
    public void testRunWithValidAction() throws Exception {
        when(args.getOptionSet().nonOptionArguments()).thenReturn(Arrays.asList("arg1", "export"));
        Assign assign = new Assign(admin, args);
        
        AssignAction mockAction = Mockito.mock(AssignAction.class);
        AssignAction.valueOf("EXPORT").run(admin, args);
        
        assign.run();
        verify(mockAction).run(admin, args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRunWithInvalidAction() throws Exception {
        when(args.getOptionSet().nonOptionArguments()).thenReturn(Arrays.asList("arg1", "invalidAction"));
        Assign assign = new Assign(admin, args);
        assign.run();
    }
}
