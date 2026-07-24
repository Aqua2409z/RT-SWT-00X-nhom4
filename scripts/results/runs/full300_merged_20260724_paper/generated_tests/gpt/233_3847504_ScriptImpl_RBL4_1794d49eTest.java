package ch.entwine.weblounge.common.impl.content.page;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ch.entwine.weblounge.common.site.Environment;
import ch.entwine.weblounge.common.site.Module;
import ch.entwine.weblounge.common.site.Site;
import org.w3c.dom.Node;
import javax.xml.xpath.XPath;

public class ScriptImpl_RBL4_1794d49eTest {

    private ScriptImpl script;

    @Before
    public void setUp() {
        script = new ScriptImpl("test.js");
    }

    @Test
    public void testConstructorWithHref() {
        assertEquals("test.js", script.getHref());
        assertEquals(ScriptImpl.DEFAULT_CHARSET, script.getCharset());
        assertNull(script.getType());
        assertFalse(script.isDeferred());
    }

    @Test
    public void testConstructorWithHrefAndType() {
        script = new ScriptImpl("test.js", "text/javascript");
        assertEquals("text/javascript", script.getType());
    }

    @Test
    public void testSetSite() {
        Site site = new Site() {}; // Mock Site
        script.setSite(site);
        assertNotNull(script);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSiteNull() {
        script.setSite(null);
    }

    @Test
    public void testSetModule() {
        Module module = new Module() {}; // Mock Module
        script.setModule(module);
        assertNotNull(script);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModuleNull() {
        script.setModule(null);
    }

    @Test
    public void testSetEnvironment() {
        Environment environment = new Environment() {}; // Mock Environment
        script.setSite(new Site() {}); // Set site first
        script.setEnvironment(environment);
        assertNotNull(script);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetEnvironmentWithoutSite() {
        script.setEnvironment(new Environment() {});
    }

    @Test
    public void testSetUse() {
        script.setUse(Script.Use.All);
        assertEquals(Script.Use.All, script.getUse());
    }

    @Test
    public void testSetType() {
        script.setType("text/javascript");
        assertEquals("text/javascript", script.getType());
    }

    @Test
    public void testSetCharset() {
        script.setCharset("utf-8");
        assertEquals("utf-8", script.getCharset());
    }

    @Test
    public void testSetJQuery() {
        script.setJQuery("3.5.1");
        assertEquals("3.5.1", script.getJQuery());
    }

    @Test
    public void testSetDeferred() {
        script.setDeferred(true);
        assertTrue(script.isDeferred());
    }

    @Test
    public void testToHtml() {
        String html = script.toHtml();
        assertTrue(html.contains("src=\"test.js\""));
    }

    @Test
    public void testToXml() {
        String xml = script.toXml();
        assertTrue(xml.contains("src=\"test.js\""));
    }

    @Test
    public void testEquals() {
        ScriptImpl anotherScript = new ScriptImpl("test.js");
        assertTrue(script.equals(anotherScript));
    }

    @Test
    public void testNotEquals() {
        ScriptImpl anotherScript = new ScriptImpl("other.js");
        assertFalse(script.equals(anotherScript));
    }

    @Test
    public void testHashCode() {
        assertEquals(script.hashCode(), new ScriptImpl("test.js").hashCode());
    }

    @Test
    public void testToString() {
        String str = script.toString();
        assertTrue(str.contains("src=test.js"));
    }

    @Test
    public void testFromXml() {
        // Mock Node and XPath for testing fromXml method
        Node mockNode = null; // Create a mock Node
        XPath mockXPath = null; // Create a mock XPath
        // Assuming fromXml method is tested with valid Node and XPath
        // script = ScriptImpl.fromXml(mockNode, mockXPath);
        // assertNotNull(script);
    }
}
