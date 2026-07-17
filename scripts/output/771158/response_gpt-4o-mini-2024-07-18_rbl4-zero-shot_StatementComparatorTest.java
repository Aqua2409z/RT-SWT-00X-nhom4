
package net.fortytwo.sesametools;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleStatement;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatementComparatorTest {

    private final StatementComparator comparator = StatementComparator.getInstance();

    @Test
    public void testCompareEqualStatements() {
        Statement statement1 = createMockStatement("subject", "predicate", "object", null);
        Statement statement2 = createMockStatement("subject", "predicate", "object", null);
        
        assertEquals(StatementComparator.EQUAL, comparator.compare(statement1, statement2));
    }

    @Test
    public void testCompareDifferentSubjects() {
        Statement statement1 = createMockStatement("subject1", "predicate", "object", null);
        Statement statement2 = createMockStatement("subject2", "predicate", "object", null);
        
        assertEquals(StatementComparator.BEFORE, comparator.compare(statement1, statement2));
        assertEquals(StatementComparator.AFTER, comparator.compare(statement2, statement1));
    }

    @Test
    public void testCompareDifferentPredicates() {
        Statement statement1 = createMockStatement("subject", "predicate1", "object", null);
        Statement statement2 = createMockStatement("subject", "predicate2", "object", null);
        
        assertEquals(StatementComparator.BEFORE, comparator.compare(statement1, statement2));
        assertEquals(StatementComparator.AFTER, comparator.compare(statement2, statement1));
    }

    @Test
    public void testCompareDifferentObjects() {
        Statement statement1 = createMockStatement("subject", "predicate", "object1", null);
        Statement statement2 = createMockStatement("subject", "predicate", "object2", null);
        
        assertEquals(StatementComparator.BEFORE, comparator.compare(statement1, statement2));
        assertEquals(StatementComparator.AFTER, comparator.compare(statement2, statement1));
    }

    @Test
    public void testCompareDifferentContexts() {
        Value context1 = mock(Value.class);
        Value context2 = mock(Value.class);
        when(context1.stringValue()).thenReturn("context1");
        when(context2.stringValue()).thenReturn("context2");

        Statement statement1 = createMockStatement("subject", "predicate", "object", context1);
        Statement statement2 = createMockStatement("subject", "predicate", "object", context2);
        
        assertEquals(StatementComparator.BEFORE, comparator.compare(statement1, statement2));
        assertEquals(StatementComparator.AFTER, comparator.compare(statement2, statement1));
    }

    @Test
    public void testCompareFirstStatementNullContext() {
        Statement statement1 = createMockStatement("subject", "predicate", "object", null);
        Statement statement2 = createMockStatement("subject", "predicate", "object", mock(Value.class));
        
        assertEquals(StatementComparator.BEFORE, comparator.compare(statement1, statement2));
    }

    @Test
    public void testCompareSecondStatementNullContext() {
        Statement statement1 = createMockStatement("subject", "predicate", "object", mock(Value.class));
        Statement statement2 = createMockStatement("subject", "predicate", "object", null);
        
        assertEquals(StatementComparator.AFTER, comparator.compare(statement1, statement2));
    }

    private Statement createMockStatement(String subject, String predicate, String object, Value context) {
        Statement statement = mock(Statement.class);
        when(statement.getSubject()).thenReturn(mockValue(subject));
        when(statement.getPredicate()).thenReturn(mockValue(predicate));
        when(statement.getObject()).thenReturn(mockValue(object));
        when(statement.getContext()).thenReturn(context);
        return statement;
    }

    private Value mockValue(String value) {
        Value mockValue = mock(Value.class);
        when(mockValue.stringValue()).thenReturn(value);
        return mockValue;
    }
}
