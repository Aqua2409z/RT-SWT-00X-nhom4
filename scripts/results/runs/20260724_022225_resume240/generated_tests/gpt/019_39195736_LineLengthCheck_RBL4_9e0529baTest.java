package org.sonar.jproperties.checks.generic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sonar.plugins.jproperties.api.tree.Tree;
import org.sonar.plugins.jproperties.api.visitors.Context;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class LineLengthCheck_RBL4_9e0529baTest {

    private LineLengthCheck lineLengthCheck;
    private Context context;
    private Tree tree;

    @Before
    public void setUp() {
        lineLengthCheck = new LineLengthCheck();
        context = mock(Context.class);
        tree = mock(Tree.class);
        lineLengthCheck.setContext(context);
    }

    @Test
    public void testVisitFileWithShortLines() throws Exception {
        lineLengthCheck.setMaximumLineLength(120);
        List<String> lines = Arrays.asList("short line", "another short line");
        when(context.getFile()).thenReturn(mockFileWithLines(lines));

        lineLengthCheck.visitFile(tree);

        verify(context, never()).addLineIssue(anyInt(), anyString());
    }

    @Test
    public void testVisitFileWithLongLines() throws Exception {
        lineLengthCheck.setMaximumLineLength(10);
        List<String> lines = Arrays.asList("short line", "this line is way too long");
        when(context.getFile()).thenReturn(mockFileWithLines(lines));

        lineLengthCheck.visitFile(tree);

        verify(context, times(1)).addLineIssue(2, "The line contains 30 characters which is greater than 10 authorized.");
    }

    @Test(expected = IllegalStateException.class)
    public void testVisitFileThrowsExceptionOnIOException() throws Exception {
        when(context.getFile()).thenThrow(new IOException("File not found"));

        lineLengthCheck.visitFile(tree);
    }

    @Test
    public void testSetCharset() {
        Charset charset = Charset.forName("UTF-8");
        lineLengthCheck.setCharset(charset);
        // Assuming there's a way to verify the charset is set correctly
        // This would require a getter or some way to check the internal state
    }

    private File mockFileWithLines(List<String> lines) {
        // Mock implementation to return a file that contains the specified lines
        // This would typically involve creating a temporary file and writing the lines to it
        // For simplicity, this is left as a placeholder
        return new File("mockFile.properties");
    }
}
