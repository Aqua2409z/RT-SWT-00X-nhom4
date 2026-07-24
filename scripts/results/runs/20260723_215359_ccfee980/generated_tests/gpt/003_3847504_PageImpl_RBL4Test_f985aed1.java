package ch.entwine.weblounge.common.impl.content.page;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ch.entwine.weblounge.common.content.ResourceURI;
import ch.entwine.weblounge.common.impl.content.page.PageImpl;
import ch.entwine.weblounge.common.impl.content.ResourceURIImpl;
import ch.entwine.weblounge.common.content.page.Pagelet;
import ch.entwine.weblounge.common.content.page.Composer;
import ch.entwine.weblounge.common.language.Language;

import java.util.ArrayList;
import java.util.List;

public class PageImpl_RBL4Test_f985aed1 {

    private PageImpl page;
    private ResourceURI uri;

    @Before
    public void setUp() {
        uri = new ResourceURIImpl("test", "site", "path", "id", 1);
        page = new PageImpl(uri);
    }

    @Test
    public void testSetAndGetLayout() {
        page.setLayout("newLayout");
        assertEquals("newLayout", page.getLayout());
    }

    @Test
    public void testSetAndGetTemplate() {
        page.setTemplate("newTemplate");
        assertEquals("newTemplate", page.getTemplate());
    }

    @Test
    public void testSetStationary() {
        page.setStationary(true);
        assertTrue(page.isStationary());
    }

    @Test
    public void testAddPagelet() {
        Pagelet pagelet = new MockPagelet("testPagelet");
        page.addPagelet(pagelet, "composer1");
        assertEquals(1, page.getPagelets().length);
        assertEquals(pagelet, page.getPagelets()[0]);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddPageletAtInvalidPosition() {
        Pagelet pagelet = new MockPagelet("testPagelet");
        page.addPagelet(pagelet, "composer1", 1); // Invalid position
    }

    @Test
    public void testRemovePagelet() {
        Pagelet pagelet = new MockPagelet("testPagelet");
        page.addPagelet(pagelet, "composer1");
        assertEquals(1, page.getPagelets().length);
        page.removePagelet("composer1", 0);
        assertEquals(0, page.getPagelets().length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemovePageletAtInvalidPosition() {
        page.removePagelet("composer1", 0); // No pagelet to remove
    }

    @Test
    public void testGetComposers() {
        Composer composer = page.getComposer("composer1");
        assertNotNull(composer);
        assertEquals("composer1", composer.getId());
    }

    @Test
    public void testGetPagelets() {
        Pagelet pagelet1 = new MockPagelet("testPagelet1");
        Pagelet pagelet2 = new MockPagelet("testPagelet2");
        page.addPagelet(pagelet1, "composer1");
        page.addPagelet(pagelet2, "composer1");
        Pagelet[] pagelets = page.getPagelets();
        assertEquals(2, pagelets.length);
        assertEquals(pagelet1, pagelets[0]);
        assertEquals(pagelet2, pagelets[1]);
    }

    @Test
    public void testSupportsContentLanguage() {
        Language language = new MockLanguage("en");
        assertTrue(page.supportsContentLanguage(language));
    }

    private class PageImpl_RBL4Test_f985aed1 implements Pagelet {
        private String identifier;

        public MockPagelet(String identifier) {
            this.identifier = identifier;
        }

        @Override
        public String getIdentifier() {
            return identifier;
        }

        @Override
        public String getModule() {
            return "mockModule";
        }

        @Override
        public String toXml() {
            return "<pagelet id=\"" + identifier + "\"/>";
        }

        @Override
        public PageletURI getURI() {
            return null; // Simplified for testing
        }

        @Override
        public void setURI(PageletURI uri) {
            // No implementation needed for testing
        }
    }

    private class PageImpl_RBL4Test_f985aed1 extends Language {
        private String code;

        public MockLanguage(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }
    }
}
