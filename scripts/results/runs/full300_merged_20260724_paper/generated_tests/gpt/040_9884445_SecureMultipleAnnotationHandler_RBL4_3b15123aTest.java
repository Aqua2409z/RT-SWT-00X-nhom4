
package org.minnal.instrument.entity.metadata.handler;

import org.minnal.instrument.entity.Secure;
import org.minnal.instrument.entity.SecureMultiple;
import org.minnal.instrument.entity.metadata.SecurableMetaData;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;

import static org.mockito.Mockito.*;

public class SecureMultipleAnnotationHandler_RBL4_3b15123aTest {

    private SecureMultipleAnnotationHandler handler;
    private SecurableMetaData metaData;

    @BeforeMethod
    public void setUp() {
        handler = new SecureMultipleAnnotationHandler();
        metaData = Mockito.mock(SecurableMetaData.class);
    }

    @Test
    public void testHandleWithSingleSecure() {
        Secure secure = mock(Secure.class);
        SecureMultiple secureMultiple = mock(SecureMultiple.class);
        when(secureMultiple.value()).thenReturn(new Secure[]{secure});

        handler.handle(metaData, secureMultiple);

        verify(metaData, times(1)).addSecure(secure);
    }

    @Test
    public void testHandleWithMultipleSecures() {
        Secure secure1 = mock(Secure.class);
        Secure secure2 = mock(Secure.class);
        SecureMultiple secureMultiple = mock(SecureMultiple.class);
        when(secureMultiple.value()).thenReturn(new Secure[]{secure1, secure2});

        handler.handle(metaData, secureMultiple);

        verify(metaData, times(1)).addSecure(secure1);
        verify(metaData, times(1)).addSecure(secure2);
    }

    @Test(expectedExceptions = ClassCastException.class)
    public void testHandleWithInvalidAnnotation() {
        Annotation invalidAnnotation = mock(Annotation.class);
        handler.handle(metaData, invalidAnnotation);
    }
}
