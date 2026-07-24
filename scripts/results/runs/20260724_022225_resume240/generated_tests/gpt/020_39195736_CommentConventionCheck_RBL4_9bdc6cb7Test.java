package org.sonar.jproperties.checks.generic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sonar.plugins.jproperties.api.tree.PropertiesTree;
import org.sonar.plugins.jproperties.api.tree.SyntaxTrivia;

import static org.mockito.Mockito.*;

public class CommentConventionCheck_RBL4_9bdc6cb7Test {

    private CommentConventionCheck check;
    private PropertiesTree propertiesTree;
    private SyntaxTrivia syntaxTrivia;

    @Before
    public void setUp() {
        check = new CommentConventionCheck();
        propertiesTree = mock(PropertiesTree.class);
        syntaxTrivia = mock(SyntaxTrivia.class);
    }

    @Test
    public void testVisitPropertiesWithDefaultToken() {
        check.visitProperties(propertiesTree);
        // Add assertions or verifications if needed
    }

    @Test
    public void testVisitCommentWithDisallowedToken() {
        check.setStartingCommentToken("!");
        when(syntaxTrivia.text()).thenReturn("# This is a comment");
        
        check.visitComment(syntaxTrivia);
        
        // Verify that an issue is added for using the disallowed token
        verify(check, times(1)).addPreciseIssue(syntaxTrivia, "Use starting comment token '!' instead.");
    }

    @Test
    public void testVisitCommentWithNoWhitespaceAfterToken() {
        check.setStartingCommentToken("#");
        when(syntaxTrivia.text()).thenReturn("#No whitespace");
        
        check.visitComment(syntaxTrivia);
        
        // Verify that an issue is added for missing whitespace
        verify(check, times(1)).addPreciseIssue(syntaxTrivia, "Add a whitespace after the starting comment token.");
    }

    @Test
    public void testValidateParametersWithValidToken() {
        check.setStartingCommentToken("#");
        check.validateParameters(); // Should not throw exception
    }

    @Test(expected = IllegalStateException.class)
    public void testValidateParametersWithInvalidToken() {
        check.setStartingCommentToken("@");
        check.validateParameters(); // Should throw exception
    }

    @Test
    public void testVisitCommentWithValidComment() {
        check.setStartingCommentToken("#");
        when(syntaxTrivia.text()).thenReturn("# This is a valid comment");
        
        check.visitComment(syntaxTrivia);
        
        // Verify that no issues are added
        verify(check, never()).addPreciseIssue(any(), anyString());
    }
}
