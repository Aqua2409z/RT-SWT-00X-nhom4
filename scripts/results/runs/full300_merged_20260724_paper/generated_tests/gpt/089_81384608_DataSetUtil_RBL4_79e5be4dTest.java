
package com.thinkbiganalytics.kylo.catalog.spark.sources;

import com.thinkbiganalytics.kylo.catalog.api.MissingOptionException;
import com.thinkbiganalytics.kylo.catalog.spi.DataSetOptions;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DataSetUtil_RBL4_79e5be4dTest {

    @Test
    public void testGetOptionOrThrow_OptionDefined() {
        // Arrange
        DataSetOptions options = Mockito.mock(DataSetOptions.class);
        String key = "testKey";
        String expectedValue = "testValue";
        Mockito.when(options.getOption(key)).thenReturn(Option.apply(expectedValue));

        // Act
        String result = DataSetUtil.getOptionOrThrow(options, key, null);

        // Assert
        assertEquals(expectedValue, result);
    }

    @Test
    public void testGetOptionOrThrow_OptionNotDefined() {
        // Arrange
        DataSetOptions options = Mockito.mock(DataSetOptions.class);
        String key = "testKey";
        Mockito.when(options.getOption(key)).thenReturn(Option.empty());

        // Act & Assert
        try {
            DataSetUtil.getOptionOrThrow(options, key, null);
            fail("Expected MissingOptionException to be thrown");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: " + key, e.getMessage());
        }
    }

    @Test
    public void testGetOptionOrThrow_OptionNotDefined_WithCustomErrorMessage() {
        // Arrange
        DataSetOptions options = Mockito.mock(DataSetOptions.class);
        String key = "testKey";
        String customErrorMessage = "Custom error message";
        Mockito.when(options.getOption(key)).thenReturn(Option.empty());

        // Act & Assert
        try {
            DataSetUtil.getOptionOrThrow(options, key, customErrorMessage);
            fail("Expected MissingOptionException to be thrown");
        } catch (MissingOptionException e) {
            assertEquals(customErrorMessage, e.getMessage());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        // Act
        new DataSetUtil();
    }
}
