package flapjack.cobol.layout;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import flapjack.cobol.layout.CobolFieldDefinitionFactory;
import flapjack.cobol.layout.CobolFieldInfo;
import flapjack.cobol.layout.CobolFieldType;
import flapjack.layout.PaddingDescriptor;

public class CobolFieldDefinitionFactoryTest {

    private CobolFieldDefinitionFactory factory;
    private PaddingDescriptor paddingDescriptor;

    @Before
    public void setUp() {
        factory = new CobolFieldDefinitionFactory();
        paddingDescriptor = new PaddingDescriptor(); // Assuming a default constructor exists
    }

    @Test
    public void testBuildDecimalFieldDefinition() {
        CobolFieldInfo fieldInfo = new CobolFieldInfo("amount", 1, "999.99", CobolFieldType.DECIMAL);
        assertNotNull(factory.build(fieldInfo, paddingDescriptor));
        assertTrue(factory.build(fieldInfo, paddingDescriptor) instanceof DecimalFieldDefinition);
    }

    @Test
    public void testBuildIntegerFieldDefinition() {
        CobolFieldInfo fieldInfo = new CobolFieldInfo("count", 1, "9999", CobolFieldType.INTEGER);
        assertNotNull(factory.build(fieldInfo, paddingDescriptor));
        assertTrue(factory.build(fieldInfo, paddingDescriptor) instanceof IntegerFieldDefinition);
    }

    @Test
    public void testBuildAlphaNumericFieldDefinition() {
        CobolFieldInfo fieldInfo = new CobolFieldInfo("name", 1, "X(10)", CobolFieldType.ALPHANUMERIC);
        assertNotNull(factory.build(fieldInfo, paddingDescriptor));
        assertTrue(factory.build(fieldInfo, paddingDescriptor) instanceof AlphaNumericFieldDefinition);
    }

    @Test(expected = NullPointerException.class)
    public void testBuildWithNullFieldInfo() {
        factory.build(null, paddingDescriptor);
    }

    @Test(expected = NullPointerException.class)
    public void testBuildWithNullPaddingDescriptor() {
        CobolFieldInfo fieldInfo = new CobolFieldInfo("name", 1, "X(10)", CobolFieldType.ALPHANUMERIC);
        factory.build(fieldInfo, null);
    }
}
