
package org.minnal.instrument.entity.metadata.handler;

import org.minnal.instrument.entity.Secure;
import org.minnal.instrument.entity.metadata.PermissionMetaData;
import org.minnal.instrument.entity.metadata.SecurableMetaData;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;

public class SecureAnnotationHandler_RBL4Test_cedbf5e7 {

    private SecureAnnotationHandler handler;
    private SecurableMetaData metaData;

    @BeforeMethod
    public void setUp() {
        handler = new SecureAnnotationHandler();
        metaData = Mockito.mock(SecurableMetaData.class);
    }

    @Test
    public void testHandle() {
        Secure secureAnnotation = Mockito.mock(Secure.class);
        Mockito.when(secureAnnotation.method()).thenReturn(Secure.Method.GET);
        Mockito.when(secureAnnotation.permissions()).thenReturn(new String[]{"READ", "WRITE"});

        handler.handle(metaData, secureAnnotation);

        Mockito.verify(metaData).addPermissionMetaData(Mockito.any(PermissionMetaData.class));
    }

    @Test
    public void testConstructPermissionMetaData() {
        Secure secureAnnotation = Mockito.mock(Secure.class);
        Mockito.when(secureAnnotation.method()).thenReturn(Secure.Method.POST);
        Mockito.when(secureAnnotation.permissions()).thenReturn(new String[]{"CREATE"});

        PermissionMetaData permissionMetaData = handler.constructPermissionMetaData(secureAnnotation);

        Assert.assertEquals(permissionMetaData.getMethod(), Secure.Method.POST.getMethod());
        Assert.assertTrue(permissionMetaData.getPermissions().contains("CREATE"));
    }
}
