package com.ebayopensource.webrex.resource;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.ebayopensource.webrex.resource.api.IAggregatedResource;
import com.ebayopensource.webrex.resource.api.IExternalResource;
import com.ebayopensource.webrex.resource.api.IInlineResource;
import com.ebayopensource.webrex.resource.api.IResource;
import com.ebayopensource.webrex.resource.api.IResourceLibrary;
import com.ebayopensource.webrex.resource.api.IResourceLocale;
import com.ebayopensource.webrex.resource.api.IResourceUrn;

public class ResourceFactory_RBL4_d6d7f2bdTest {

    private ResourceManager resourceManagerMock;
    private ResourceRuntimeContext resourceRuntimeContextMock;

    @Before
    public void setUp() {
        resourceManagerMock = mock(ResourceManager.class);
        resourceRuntimeContextMock = mock(ResourceRuntimeContext.class);
        ResourceManager.INSTANCE = resourceManagerMock;
        ResourceRuntimeContext.setInstance(resourceRuntimeContextMock);
    }

    @Test
    public void testCreateAggregatedResource() {
        List<IResource> resources = mock(List.class);
        IAggregatedResource aggregatedResource = mock(IAggregatedResource.class);
        when(resourceManagerMock.resolveAggregatedResource(resources)).thenReturn(aggregatedResource);

        IAggregatedResource result = ResourceFactory.createAggregatedResource(resources);
        assertEquals(aggregatedResource, result);
    }

    @Test
    public void testCreateInlineResource() {
        String type = "text/css";
        String content = "body { background-color: #fff; }";
        IInlineResource inlineResource = mock(IInlineResource.class);
        when(resourceManagerMock.resolveInline(type, content)).thenReturn(inlineResource);

        IInlineResource result = ResourceFactory.createInlineResource(type, content);
        assertEquals(inlineResource, result);
    }

    @Test
    public void testCreateExternalResource() {
        String type = "text/javascript";
        String url = "http://example.com/script.js";
        IExternalResource externalResource = mock(IExternalResource.class);
        when(resourceManagerMock.resolveExternal(type, url)).thenReturn(externalResource);

        IExternalResource result = ResourceFactory.createExternalResource(type, url);
        assertEquals(externalResource, result);
    }

    @Test
    public void testCreateLibraryResource() {
        String resourcePath = "/path/to/resource";
        IResource resource = mock(IResource.class);
        when(resourceRuntimeContextMock.getConfig().getRegistry().getResourceType("resource")).thenReturn("type");
        when(resourceManagerMock.resolve(any(IResourceUrn.class))).thenReturn(resource);

        IResource result = ResourceFactory.createLibraryResource(resourcePath);
        assertEquals(resource, result);
    }

    @Test
    public void testCreateResourceWithUrn() throws Exception {
        IResourceUrn urn = mock(IResourceUrn.class);
        URL url = new URL("http://example.com/resource");
        IResourceLocale locale = mock(IResourceLocale.class);
        IResourceLibrary library = mock(IResourceLibrary.class);
        IResource resource = mock(IResource.class);
        when(resourceManagerMock.create(urn, url, locale, library)).thenReturn(resource);

        IResource result = ResourceFactory.createResource(urn, url, locale, library);
        assertEquals(resource, result);
    }

    @Test
    public void testCreateResourceByEl() {
        String el = "${resource.path}";
        IResource resource = mock(IResource.class);
        ResourceExpression expressionMock = mock(ResourceExpression.class);
        when(ELHelper.getExpressionFromEL(el)).thenReturn(expressionMock);
        when(expressionMock.evaluate()).thenReturn(resource);

        IResource result = ResourceFactory.createResourceByEl(el);
        assertEquals(resource, result);
    }

    @Test
    public void testCreateResourceWithTypeNamespace() {
        String resourceType = "type";
        String namespace = "namespace";
        String resourcePath = "/path/to/resource";
        IResource resource = mock(IResource.class);
        IResourceUrn urn = new ResourceUrn(resourceType, namespace, resourcePath);
        when(resourceManagerMock.resolve(urn)).thenReturn(resource);

        IResource result = ResourceFactory.createResource(resourceType, namespace, resourcePath);
        assertEquals(resource, result);
    }

    @Test
    public void testCreateResourceWithArguments() {
        String resourceType = "type";
        String namespace = "namespace";
        String resourcePath = "/path/to/resource";
        Map<String, Object> arguments = new HashMap<>();
        IResource resource = mock(IResource.class);
        IResourceUrn urn = new ResourceArgumentsUrn(resourceType, namespace, resourcePath, arguments);
        when(resourceManagerMock.resolve(urn)).thenReturn(resource);

        IResource result = ResourceFactory.createResource(resourceType, namespace, resourcePath, arguments);
        assertEquals(resource, result);
    }

    @Test
    public void testCreateResourceWithUrl() throws Exception {
        URL url = new URL("http://example.com/resource");
        String locale = "en_US";
        IResourceLibrary libraryInfo = mock(IResourceLibrary.class);
        IResource resource = mock(IResource.class);
        when(resourceManagerMock.create(url, locale, libraryInfo)).thenReturn(resource);

        IResource result = ResourceFactory.createResource(url, locale, libraryInfo);
        assertEquals(resource, result);
    }

    @Test
    public void testCreateWarResource() {
        String resourcePath = "/path/to/resource";
        IResource resource = mock(IResource.class);
        when(resourceRuntimeContextMock.getConfig().getRegistry().getResourceType("resource")).thenReturn("type");
        IResourceUrn urn = new ResourceUrn("type", ResourceManager.LOCAL, resourcePath);
        when(resourceManagerMock.resolve(urn)).thenReturn(resource);

        IResource result = ResourceFactory.createWarResource(resourcePath);
        assertEquals(resource, result);
    }
}
