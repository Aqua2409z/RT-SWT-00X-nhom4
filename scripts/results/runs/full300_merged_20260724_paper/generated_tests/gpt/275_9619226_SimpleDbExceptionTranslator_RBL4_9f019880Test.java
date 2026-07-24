
package org.springframework.data.simpledb.exception;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.springframework.dao.*;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.cloudwatch.model.ResourceNotFoundException;
import com.amazonaws.services.simpledb.model.*;

public class SimpleDbExceptionTranslator_RBL4_9f019880Test {

    private final SimpleDbExceptionTranslator translator = SimpleDbExceptionTranslator.getTranslatorInstance();

    @Test
    public void testTranslateAmazonClientException_NullTranslation() {
        AmazonClientException exception = mock(AmazonClientException.class);
        RuntimeException result = translator.translateAmazonClientException(exception);
        assertEquals(exception, result);
    }

    @Test
    public void testTranslateExceptionIfPossible_DuplicateItemNameException() {
        DuplicateItemNameException exception = new DuplicateItemNameException("Duplicate item");
        DataAccessException result = translator.translateExceptionIfPossible(exception);
        assertTrue(result instanceof DuplicateKeyException);
        assertEquals("Duplicate item", result.getMessage());
    }

    @Test
    public void testTranslateExceptionIfPossible_AttributeDoesNotExistException() {
        AttributeDoesNotExistException exception = new AttributeDoesNotExistException("Attribute does not exist");
        DataAccessException result = translator.translateExceptionIfPossible(exception);
        assertTrue(result instanceof EmptyResultDataAccessException);
        assertEquals("Attribute does not exist", result.getMessage());
    }

    @Test
    public void testTranslateExceptionIfPossible_ResourceNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");
        DataAccessException result = translator.translateExceptionIfPossible(exception);
        assertTrue(result instanceof DataRetrievalFailureException);
        assertEquals("Resource not found", result.getMessage());
    }

    @Test
    public void testTranslateExceptionIfPossible_InvalidParameterValueException() {
        InvalidParameterValueException exception = new InvalidParameterValueException("Invalid parameter");
        DataAccessException result = translator.translateExceptionIfPossible(exception);
        assertTrue(result instanceof InvalidDataAccessResourceUsageException);
        assertEquals("Invalid parameter", result.getMessage());
    }

    @Test
    public void testTranslateExceptionIfPossible_NoSuchDomainException() {
        NoSuchDomainException exception = new NoSuchDomainException("No such domain");
        DataAccessException result = translator.translateExceptionIfPossible(exception);
        assertTrue(result instanceof EmptyResultDataAccessException);
        assertEquals("No such domain", result.getMessage());
    }

    @Test
    public void testTranslateExceptionIfPossible_NumberDomainAttributesExceededException() {
        NumberDomainAttributesExceededException exception = new NumberDomainAttributesExceededException("Exceeded attributes");
        DataAccessException result = translator.translateExceptionIfPossible(exception);
        assertTrue(result instanceof DataIntegrityViolationException);
        assertEquals("Exceeded attributes", result.getMessage());
    }

    @Test
    public void testTranslateExceptionIfPossible_InvalidNextTokenException() {
        InvalidNextTokenException exception = new InvalidNextTokenException("Invalid next token");
        DataAccessException result = translator.translateExceptionIfPossible(exception);
        assertTrue(result instanceof InvalidDataAccessApiUsageException);
        assertEquals("Invalid next token", result.getMessage());
    }

    @Test
    public void testTranslateExceptionIfPossible_AmazonServiceException() {
        AmazonServiceException exception = new AmazonServiceException("Service exception");
        DataAccessException result = translator.translateExceptionIfPossible(exception);
        assertTrue(result instanceof DataAccessResourceFailureException);
        assertEquals("Service exception", result.getMessage());
    }

    @Test
    public void testTranslateExceptionIfPossible_AmazonClientException() {
        AmazonClientException exception = new AmazonClientException("Client exception");
        DataAccessException result = translator.translateExceptionIfPossible(exception);
        assertNull(result);
    }

    @Test
    public void testGetTranslatorInstance() {
        SimpleDbExceptionTranslator instance1 = SimpleDbExceptionTranslator.getTranslatorInstance();
        SimpleDbExceptionTranslator instance2 = SimpleDbExceptionTranslator.getTranslatorInstance();
        assertSame(instance1, instance2);
    }
}
