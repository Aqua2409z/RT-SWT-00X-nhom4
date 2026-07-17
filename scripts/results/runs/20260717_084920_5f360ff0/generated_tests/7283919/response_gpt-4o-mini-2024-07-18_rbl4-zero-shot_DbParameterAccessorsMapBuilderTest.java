
package dbfit.util;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;

public class DbParameterAccessorsMapBuilderTest {
    private DbParameterAccessorsMapBuilder builder;
    private TypeTransformerFactory mockFactory;

    @Before
    public void setUp() {
        mockFactory = new TypeTransformerFactory(); // Assuming a default constructor exists
        builder = new DbParameterAccessorsMapBuilder(mockFactory);
    }

    @Test
    public void testAddParameterAccessor() {
        String name = "testParam";
        Direction direction = Direction.IN;
        int sqlType = 1; // Example SQL type
        Class<?> javaType = String.class;

        builder.add(name, direction, sqlType, javaType);
        Map<String, DbParameterAccessor> accessors = builder.toMap();

        assertEquals(1, accessors.size());
        DbParameterAccessor accessor = accessors.get("testParam");
        assertNotNull(accessor);
        assertEquals(name, accessor.getName());
        assertEquals(direction, accessor.getDirection());
        assertEquals(sqlType, accessor.getSqlType());
        assertEquals(javaType, accessor.getJavaType());
    }

    @Test
    public void testAddMultipleParameterAccessors() {
        builder.add("param1", Direction.IN, 1, String.class);
        builder.add("param2", Direction.OUT, 2, Integer.class);
        builder.add("param3", Direction.RETURN_VALUE, 3, Double.class);

        Map<String, DbParameterAccessor> accessors = builder.toMap();
        assertEquals(3, accessors.size());
        assertNotNull(accessors.get("param1"));
        assertNotNull(accessors.get("param2"));
        assertNotNull(accessors.get("param3"));
    }

    @Test
    public void testNormaliseName() {
        String name = "Test Name";
        String normalised = NameNormaliser.normaliseName(name);
        assertEquals("TestName", normalised); // Assuming normaliseName removes spaces
    }

    @Test
    public void testPositionIncrement() {
        builder.add("param1", Direction.IN, 1, String.class);
        builder.add("param2", Direction.IN, 2, Integer.class);
        Map<String, DbParameterAccessor> accessors = builder.toMap();

        assertEquals(0, accessors.get("param1").getPosition());
        assertEquals(1, accessors.get("param2").getPosition());
    }

    @Test
    public void testReturnValuePosition() {
        builder.add("returnParam", Direction.RETURN_VALUE, 1, String.class);
        Map<String, DbParameterAccessor> accessors = builder.toMap();

        assertEquals(-1, accessors.get("returnParam").getPosition());
    }
}
