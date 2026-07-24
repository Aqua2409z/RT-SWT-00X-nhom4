
package ch.jalu.injector.handlers.dependency;

import ch.jalu.injector.Injector;
import ch.jalu.injector.context.ResolutionContext;
import ch.jalu.injector.exceptions.InjectorException;
import ch.jalu.injector.factory.SingletonStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SingletonStoreDependencyHandler_RBL4_b36dd89fTest {

    private SingletonStoreDependencyHandler handler;
    private Injector injector;
    private ResolutionContext context;

    @BeforeEach
    void setUp() {
        handler = new SingletonStoreDependencyHandler();
        injector = Mockito.mock(Injector.class);
        context = Mockito.mock(ResolutionContext.class);
    }

    @Test
    void testResolveWithValidGenericType() {
        when(context.getIdentifier().getTypeAsClass()).thenReturn(SingletonStore.class);
        when(context.getIdentifier().getType()).thenReturn(SingletonStore.class);
        when(context.getInjector()).thenReturn(injector);
        when(context.getIdentifier().getType()).thenReturn(SingletonStore.class);

        Resolution<?> resolution = handler.resolve(context);
        assertNotNull(resolution);
        assertTrue(resolution instanceof SimpleResolution);
    }

    @Test
    void testResolveWithNullGenericType() {
        when(context.getIdentifier().getTypeAsClass()).thenReturn(SingletonStore.class);
        when(context.getIdentifier().getType()).thenReturn(SingletonStore.class);
        when(context.getInjector()).thenReturn(injector);
        when(context.getIdentifier().getType()).thenReturn(SingletonStore.class);
        when(ReflectionUtils.getGenericType(SingletonStore.class)).thenReturn(null);

        InjectorException exception = assertThrows(InjectorException.class, () -> handler.resolve(context));
        assertEquals("Singleton store fields must have concrete generic type. Cannot get generic type for field in 'class SingletonStoreDependencyHandler_RBL4_b36dd89fTest.jalu.injector.factory.SingletonStore'", exception.getMessage());
    }

    @Test
    void testGetSingletonWithValidType() {
        SingletonStore<Object> store = (SingletonStore<Object>) handler.resolve(context).get();
        when(injector.getSingleton(String.class)).thenReturn("test");

        String result = store.getSingleton(String.class);
        assertEquals("test", result);
    }

    @Test
    void testGetSingletonWithInvalidType() {
        SingletonStore<Object> store = (SingletonStore<Object>) handler.resolve(context).get();
        when(injector.getSingleton(Integer.class)).thenThrow(new InjectorException("Not a valid type"));

        InjectorException exception = assertThrows(InjectorException.class, () -> store.getSingleton(Integer.class));
        assertEquals("class SingletonStoreDependencyHandler_RBL4_b36dd89fTest.lang.Integer not child of class SingletonStoreDependencyHandler_RBL4_b36dd89fTest.lang.Object", exception.getMessage());
    }

    @Test
    void testRetrieveAllOfType() {
        SingletonStore<Object> store = (SingletonStore<Object>) handler.resolve(context).get();
        when(injector.retrieveAllOfType(String.class)).thenReturn(Collections.singletonList("test"));

        Collection<Object> result = store.retrieveAllOfType(String.class);
        assertEquals(1, result.size());
        assertTrue(result.contains("test"));
    }

    @Test
    void testRetrieveAllOfTypeWithInvalidType() {
        SingletonStore<Object> store = (SingletonStore<Object>) handler.resolve(context).get();
        when(injector.retrieveAllOfType(Integer.class)).thenThrow(new InjectorException("Not a valid type"));

        InjectorException exception = assertThrows(InjectorException.class, () -> store.retrieveAllOfType(Integer.class));
        assertEquals("class SingletonStoreDependencyHandler_RBL4_b36dd89fTest.lang.Integer not child of class SingletonStoreDependencyHandler_RBL4_b36dd89fTest.lang.Object", exception.getMessage());
    }
}
