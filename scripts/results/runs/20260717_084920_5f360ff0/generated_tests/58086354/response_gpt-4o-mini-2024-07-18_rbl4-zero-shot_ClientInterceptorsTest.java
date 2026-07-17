package io.grpc;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import io.grpc.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClientInterceptorsTest {

    private Channel mockChannel;
    private ClientInterceptor mockInterceptor1;
    private ClientInterceptor mockInterceptor2;

    @Before
    public void setUp() {
        mockChannel = mock(Channel.class);
        mockInterceptor1 = mock(ClientInterceptor.class);
        mockInterceptor2 = mock(ClientInterceptor.class);
    }

    @Test(expected = NullPointerException.class)
    public void testInterceptForward_NullChannel() {
        ClientInterceptors.interceptForward(null, mockInterceptor1);
    }

    @Test
    public void testInterceptForward_WithInterceptors() {
        Channel resultChannel = ClientInterceptors.interceptForward(mockChannel, mockInterceptor1, mockInterceptor2);
        assertNotNull(resultChannel);
    }

    @Test
    public void testInterceptForward_EmptyInterceptors() {
        Channel resultChannel = ClientInterceptors.interceptForward(mockChannel);
        assertNotNull(resultChannel);
    }

    @Test
    public void testIntercept_WithInterceptors() {
        Channel resultChannel = ClientInterceptors.intercept(mockChannel, mockInterceptor1, mockInterceptor2);
        assertNotNull(resultChannel);
    }

    @Test
    public void testIntercept_EmptyInterceptors() {
        Channel resultChannel = ClientInterceptors.intercept(mockChannel);
        assertNotNull(resultChannel);
    }

    @Test(expected = NullPointerException.class)
    public void testIntercept_NullChannel() {
        ClientInterceptors.intercept(null, mockInterceptor1);
    }

    @Test
    public void testIntercept_NullInterceptor() {
        List<ClientInterceptor> interceptors = Arrays.asList(mockInterceptor1, null);
        Channel resultChannel = ClientInterceptors.intercept(mockChannel, interceptors);
        assertNotNull(resultChannel);
    }

    @Test
    public void testInterceptorChannel_NewCall() {
        ClientCall<Object, Object> mockCall = mock(ClientCall.class);
        when(mockChannel.newCall(any(MethodDescriptor.class), any(CallOptions.class))).thenReturn(mockCall);

        InterceptorChannel interceptorChannel = new ClientInterceptors.InterceptorChannel(mockChannel, mockInterceptor1);
        ClientCall<Object, Object> resultCall = interceptorChannel.newCall(mock(MethodDescriptor.class), mock(CallOptions.class));

        assertNotNull(resultCall);
        verify(mockInterceptor1).interceptCall(any(MethodDescriptor.class), any(CallOptions.class), eq(mockChannel));
    }

    @Test
    public void testInterceptorChannel_Authority() {
        when(mockChannel.authority()).thenReturn("test-authority");

        InterceptorChannel interceptorChannel = new ClientInterceptors.InterceptorChannel(mockChannel, mockInterceptor1);
        String authority = interceptorChannel.authority();

        assertEquals("test-authority", authority);
    }
}
