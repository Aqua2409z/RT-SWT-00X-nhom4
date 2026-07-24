package ch.entwine.weblounge.common.impl.content.page;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import ch.entwine.weblounge.common.content.RenderException;
import ch.entwine.weblounge.common.content.page.HTMLHeadElement;
import ch.entwine.weblounge.common.content.page.PagePreviewMode;
import ch.entwine.weblounge.common.content.page.PageletRenderer;
import ch.entwine.weblounge.common.request.WebloungeRequest;
import ch.entwine.weblounge.common.request.WebloungeResponse;
import ch.entwine.weblounge.common.site.Module;
import ch.entwine.weblounge.common.site.Site;

public class PageletRendererImpl_RBL4_a6858dc4Test {

    private PageletRendererImpl renderer;
    private Module module;
    private Site site;
    private WebloungeRequest request;
    private WebloungeResponse response;

    @Before
    public void setUp() throws Exception {
        renderer = new PageletRendererImpl("testRenderer", new URL("http://example.com/test.jsp"));
        module = mock(Module.class);
        site = mock(Site.class);
        request = mock(WebloungeRequest.class);
        response = mock(WebloungeResponse.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModule_NullModule() {
        renderer.setModule(null);
    }

    @Test
    public void testSetModule_ValidModule() {
        when(module.getSite()).thenReturn(site);
        renderer.setModule(module);
        assertEquals(module, renderer.getModule());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEnvironment_NullEnvironment() {
        renderer.setEnvironment(null);
    }

    @Test
    public void testSetEnvironment_ValidEnvironment() {
        when(module.getSite()).thenReturn(site);
        renderer.setModule(module);
        renderer.setEnvironment(ch.entwine.weblounge.common.site.Environment.Any);
        assertNotNull(renderer.getModule());
    }

    @Test
    public void testSetEditor_ValidEditor() throws MalformedURLException {
        URL editorUrl = new URL("http://example.com/editor.jsp");
        renderer.setEditor(editorUrl);
        assertEquals(editorUrl, renderer.getEditor());
    }

    @Test
    public void testRender() throws RenderException {
        when(module.getIdentifier()).thenReturn("testModule");
        when(module.getSite()).thenReturn(site);
        renderer.setModule(module);
        renderer.render(request, response);
        verify(response).setClientRevalidationTime(renderer.getClientRevalidationTime());
        verify(response).setCacheExpirationTime(renderer.getCacheExpirationTime());
        verify(response).addTag(any(), any());
    }

    @Test
    public void testRenderAsEditor() throws RenderException {
        URL editorUrl = new URL("http://example.com/editor.jsp");
        renderer.setEditor(editorUrl);
        renderer.renderAsEditor(request, response);
        verify(response).addTag(any(), any());
    }

    @Test
    public void testAddHTMLHeader() {
        HTMLHeadElement header = mock(HTMLHeadElement.class);
        renderer.setModule(module);
        renderer.addHTMLHeader(header);
        // Assuming headers is a List<HTMLHeadElement> in the parent class
        assertTrue(renderer.getHTMLHeaders().length > 0);
    }

    @Test
    public void testToXml() {
        String xml = renderer.toXml();
        assertTrue(xml.contains("id=\"testRenderer\""));
    }

    @Test
    public void testEquals() {
        PageletRendererImpl anotherRenderer = new PageletRendererImpl("testRenderer");
        assertTrue(renderer.equals(anotherRenderer));
    }

    @Test
    public void testHashCode() {
        int hashCode = renderer.hashCode();
        assertNotNull(hashCode);
    }
}
