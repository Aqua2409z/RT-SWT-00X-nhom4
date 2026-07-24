
package org.minnal.instrument.entity.metadata.handler;

import org.minnal.instrument.entity.metadata.AssociationMetaData;
import org.minnal.instrument.entity.metadata.EntityMetaData;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.ElementCollection;
import javax.persistence.OneToMany;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.mockito.Mockito.*;

public class ElementCollectionAnnotationHandler_RBL4_7e40a186Test {

    private ElementCollectionAnnotationHandler handler;
    private EntityMetaData metaData;

    @BeforeMethod
    public void setUp() {
        handler = new ElementCollectionAnnotationHandler();
        metaData = mock(EntityMetaData.class);
    }

    @Test
    public void testHandleMethodWithElementCollection() throws Exception {
        Method method = TestEntity.class.getMethod("getElements");
        Annotation annotation = method.getAnnotation(ElementCollection.class);
        
        handler.handle(metaData, annotation, method);
        
        verify(metaData).addAssociation(argThat(association -> 
            "getElements".equals(association.getGetterName()) && 
            List.class.equals(association.getElementType()) && 
            association.isEntity() == false));
    }

    @Test
    public void testHandleMethodWithOneToMany() throws Exception {
        Method method = TestEntity.class.getMethod("getOneToManyElements");
        Annotation annotation = method.getAnnotation(ElementCollection.class);
        
        handler.handle(metaData, annotation, method);
        
        verify(metaData, never()).addAssociation(any(AssociationMetaData.class));
    }

    @Test
    public void testHandleFieldWithElementCollection() throws Exception {
        Field field = TestEntity.class.getDeclaredField("elements");
        Annotation annotation = field.getAnnotation(ElementCollection.class);
        
        handler.handle(metaData, annotation, field);
        
        verify(metaData).addAssociation(argThat(association -> 
            "elements".equals(association.getGetterName()) && 
            List.class.equals(association.getElementType()) && 
            association.isEntity() == false));
    }

    @Test
    public void testHandleFieldWithOneToMany() throws Exception {
        Field field = TestEntity.class.getDeclaredField("oneToManyElements");
        Annotation annotation = field.getAnnotation(ElementCollection.class);
        
        handler.handle(metaData, annotation, field);
        
        verify(metaData, never()).addAssociation(any(AssociationMetaData.class));
    }

    private static class ElementCollectionAnnotationHandler_RBL4_7e40a186Test {
        @ElementCollection
        public List<String> getElements() {
            return null;
        }

        @OneToMany
        @ElementCollection
        public List<String> getOneToManyElements() {
            return null;
        }

        @ElementCollection
        private List<String> elements;

        @OneToMany
        @ElementCollection
        private List<String> oneToManyElements;
    }
}
