
package org.rf.ide.core.execution.debug.contexts;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rf.ide.core.execution.debug.RobotBreakpoint;
import org.rf.ide.core.execution.debug.RobotBreakpointSupplier;
import org.rf.ide.core.execution.debug.RunningKeyword;
import org.rf.ide.core.execution.debug.StackFrameContext;
import org.rf.ide.core.testdata.model.FilePosition;
import org.rf.ide.core.testdata.model.FileRegion;
import org.rf.ide.core.testdata.model.ModelType;
import org.rf.ide.core.testdata.model.RobotFile;
import org.rf.ide.core.testdata.model.table.RobotExecutableRow;
import org.rf.ide.core.testdata.model.table.keywords.UserKeyword;
import org.rf.ide.core.testdata.model.table.keywords.names.QualifiedKeywordName;
import org.rf.ide.core.testdata.model.table.testcases.TestCase;

import java.util.ArrayList;

class ExecutableCallContext_RBL4_4fbd413bTest {

    private List<RobotFile> models;
    private List<ExecutableWithDescriptor> elements;
    private RobotBreakpointSupplier breakpointSupplier;
    private ExecutableCallContext context;

    @BeforeEach
    void setUp() {
        models = new ArrayList<>();
        elements = new ArrayList<>();
        breakpointSupplier = mock(RobotBreakpointSupplier.class);
        context = new ExecutableCallContext(models, elements, 0, URI.create("file://test.robot"), 1, breakpointSupplier);
    }

    @Test
    void testIsErroneousWhenErrorMessageIsNull() {
        assertFalse(context.isErroneous());
    }

    @Test
    void testIsErroneousWhenErrorMessageIsNotNull() {
        context = new ExecutableCallContext(models, elements, 0, URI.create("file://test.robot"), 1, "Error", breakpointSupplier);
        assertTrue(context.isErroneous());
    }

    @Test
    void testGetErrorMessageWhenErrorMessageIsNull() {
        assertEquals(Optional.empty(), context.getErrorMessage());
    }

    @Test
    void testGetErrorMessageWhenErrorMessageIsNotNull() {
        context = new ExecutableCallContext(models, elements, 0, URI.create("file://test.robot"), 1, "Error", breakpointSupplier);
        assertEquals(Optional.of("Error"), context.getErrorMessage());
    }

    @Test
    void testGetAssociatedPath() {
        assertEquals(Optional.of(URI.create("file://test.robot")), context.getAssociatedPath());
    }

    @Test
    void testGetFileRegion() {
        FileRegion fileRegion = context.getFileRegion().orElse(null);
        assertNotNull(fileRegion);
        assertEquals(new FilePosition(1, -1, -1), fileRegion.getStart());
        assertEquals(new FilePosition(1, -1, -1), fileRegion.getEnd());
    }

    @Test
    void testCurrentElement() {
        ExecutableWithDescriptor element = mock(ExecutableWithDescriptor.class);
        elements.add(element);
        when(element.getExecutable()).thenReturn(mock(RobotExecutableRow.class));
        assertEquals(element, context.currentElement());
    }

    @Test
    void testIsOnLastExecutable() {
        ExecutableWithDescriptor element = mock(ExecutableWithDescriptor.class);
        elements.add(element);
        when(element.isLastExecutable()).thenReturn(true);
        assertTrue(context.isOnLastExecutable());
    }

    @Test
    void testMoveTo() {
        RunningKeyword runningKeyword = mock(RunningKeyword.class);
        RobotExecutableRow<?> executableRow = mock(RobotExecutableRow.class);
        ExecutableWithDescriptor element = mock(ExecutableWithDescriptor.class);
        elements.add(element);
        when(element.getExecutable()).thenReturn(executableRow);
        when(runningKeyword.isTeardown()).thenReturn(false);
        
        StackFrameContext result = context.moveTo(runningKeyword, breakpointSupplier);
        assertNotNull(result);
    }

    @Test
    void testGetLineBreakpoint() {
        RobotBreakpoint breakpoint = mock(RobotBreakpoint.class);
        when(breakpointSupplier.lineBreakpointFor(any(), anyInt())).thenReturn(Optional.of(breakpoint));
        assertEquals(Optional.of(breakpoint), context.getLineBreakpoint());
    }

    @Test
    void testGetKeywordFailBreakpoint() {
        QualifiedKeywordName currentlyFailedKeyword = mock(QualifiedKeywordName.class);
        ExecutableWithDescriptor element = mock(ExecutableWithDescriptor.class);
        elements.add(element);
        when(element.getCalledKeywordName()).thenReturn("keywordName");
        when(currentlyFailedKeyword.getKeywordName()).thenReturn("keywordName");
        
        Optional<RobotBreakpoint> result = context.getKeywordFailBreakpoint(currentlyFailedKeyword);
        assertTrue(result.isPresent());
    }
}
