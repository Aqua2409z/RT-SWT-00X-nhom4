
package com.ebayopensource.webrex.resource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ebayopensource.webrex.resource.api.IResource;
import com.ebayopensource.webrex.resource.api.IResourceContext;
import com.ebayopensource.webrex.resource.api.IResourceLibrary;
import com.ebayopensource.webrex.resource.api.IResourceLocale;
import com.ebayopensource.webrex.resource.api.IResourceUrn;
import com.ebayopensource.webrex.resource.api.ITemplateContext;
import com.ebayopensource.webrex.resource.spi.IResourceHandler;

public class ResourceWrapper_RBL4Test_5b1f1ebf {
    
    private IResource mockResource;
    private ResourceWrapper resourceWrapper;

    @Before
    public void setUp() {
        mockResource = mock(IResource.class);
        resourceWrapper = new ResourceWrapper(mockResource);
    }

    @Test
    public void testGetBinaryContent() {
        IResourceContext context = mock(IResourceContext.class);
        byte[] expectedContent = new byte[]{1, 2, 3};
        when(mockResource.getBinaryContent(context)).thenReturn(expectedContent);
        
        byte[] actualContent = resourceWrapper.getBinaryContent(context);
        assertArrayEquals(expectedContent, actualContent);
    }

    @Test
    public void testGetContent() {
        IResourceContext context = mock(IResourceContext.class);
        String expectedContent = "Hello, World!";
        when(mockResource.getContent(context)).thenReturn(expectedContent);
        
        String actualContent = resourceWrapper.getContent(context);
        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testGetDependencies() {
        List<IResource> expectedDependencies = new ArrayList<>();
        expectedDependencies.add(mock(IResource.class));
        when(mockResource.getDependencies()).thenReturn(expectedDependencies);
        
        List<IResource> actualDependencies = resourceWrapper.getDependencies();
        assertEquals(expectedDependencies, actualDependencies);
    }

    @Test
    public void testGetLastModified() {
        long expectedLastModified = System.currentTimeMillis();
        when(mockResource.getLastModified()).thenReturn(expectedLastModified);
        
        long actualLastModified = resourceWrapper.getLastModified();
        assertEquals(expectedLastModified, actualLastModified);
    }

    @Test
    public void testGetLibrary() {
        IResourceLibrary expectedLibrary = mock(IResourceLibrary.class);
        when(mockResource.getLibrary()).thenReturn(expectedLibrary);
        
        IResourceLibrary actualLibrary = resourceWrapper.getLibrary();
        assertEquals(expectedLibrary, actualLibrary);
    }

    @Test
    public void testGetLocale() {
        IResourceLocale expectedLocale = mock(IResourceLocale.class);
        when(mockResource.getLocale()).thenReturn(expectedLocale);
        
        IResourceLocale actualLocale = resourceWrapper.getLocale();
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testGetOriginalBinaryContent() {
        byte[] expectedContent = new byte[]{4, 5, 6};
        when(mockResource.getOriginalBinaryContent()).thenReturn(expectedContent);
        
        byte[] actualContent = resourceWrapper.getOriginalBinaryContent();
        assertArrayEquals(expectedContent, actualContent);
    }

    @Test
    public void testGetOriginalContent() {
        String expectedContent = "Original Content";
        when(mockResource.getOriginalContent()).thenReturn(expectedContent);
        
        String actualContent = resourceWrapper.getOriginalContent();
        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testGetOriginalUrl() {
        URL expectedUrl = mock(URL.class);
        when(mockResource.getOriginalUrl()).thenReturn(expectedUrl);
        
        URL actualUrl = resourceWrapper.getOriginalUrl();
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    public void testGetUrl() {
        IResourceContext context = mock(IResourceContext.class);
        String expectedUrl = "http://example.com";
        when(mockResource.getUrl(context)).thenReturn(expectedUrl);
        
        String actualUrl = resourceWrapper.getUrl(context);
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    public void testGetUrn() {
        IResourceUrn expectedUrn = mock(IResourceUrn.class);
        when(mockResource.getUrn()).thenReturn(expectedUrn);
        
        IResourceUrn actualUrn = resourceWrapper.getUrn();
        assertEquals(expectedUrn, actualUrn);
    }

    @Test
    public void testSetDependencies() {
        List<IResource> dependencies = new ArrayList<>();
        dependencies.add(mock(IResource.class));
        
        resourceWrapper.setDependencies(dependencies);
        verify(mockResource).setDependencies(dependencies);
    }

    @Test
    public void testSetHandler() {
        IResourceHandler handler = mock(IResourceHandler.class);
        
        resourceWrapper.setHandler(handler);
        verify(mockResource).setHandler(handler);
    }
}
