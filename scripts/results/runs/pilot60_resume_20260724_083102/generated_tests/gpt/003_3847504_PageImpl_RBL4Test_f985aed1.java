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
    public void testGetLayout() {
        assertNull(page.getLayout());
        page.setLayout("newLayout");
        assertEquals("newLayout", page.getLayout());
    }

    @Test
    public void testGetTemplate() {
        assertNull(page.getTemplate());
        page.setTemplate("newTemplate");
        assertEquals("newTemplate", page.getTemplate());
    }

    @Test
    public void testSetStationary() {
        assertFalse(page.isStationary());
        page.setStationary(true);
        assertTrue(page.isStationary());
    }

    @Test
    public void testAddPagelet() {
        Pagelet pagelet = new MockPagelet("testModule", "testId");
        page.addPagelet(pagelet, "composer1");
        assertEquals(1, page.getPagelets("composer1").length);
        assertEquals(pagelet, page.getPagelets("composer1")[0]);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddPageletAtInvalidPosition() {
        Pagelet pagelet = new MockPagelet("testModule", "testId");
        page.addPagelet(pagelet, "composer1", 1); // Invalid position
    }

    @Test
    public void testRemovePagelet() {
        Pagelet pagelet = new MockPagelet("testModule", "testId");
        page.addPagelet(pagelet, "composer1");
        assertEquals(1, page.getPagelets("composer1").length);
        page.removePagelet("composer1", 0);
        assertEquals(0, page.getPagelets("composer1").length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemovePageletAtInvalidPosition() {
        page.removePagelet("composer1", 0); // No pagelet to remove
    }

    @Test
    public void testGetComposers() {
        assertEquals(0, page.getComposers().length);
        page.addPagelet(new MockPagelet("testModule", "testId"), "composer1");
        Composer[] composers = page.getComposers();
        assertEquals(1, composers.length);
        assertEquals("composer1", composers[0].getId());
    }

    @Test
    public void testGetPreview() {
        Pagelet pagelet = new MockPagelet("testModule", "testId");
        page.addPagelet(pagelet, "composer1");
        assertEquals(0, page.getPreview().length); // No preview yet
        page.setTemplate("templateWithStage");
        // Assuming templateWithStage has a valid stage
        // Add logic to mock the behavior of getting a preview
    }

    @Test
    public void testAddPageContentListener() {
        MockPageContentListener listener = new MockPageContentListener();
        page.addPageContentListener(listener);
        // Verify listener is added
    }

    @Test
    public void testRemovePageContentListener() {
        MockPageContentListener listener = new MockPageContentListener();
        page.addPageContentListener(listener);
        page.removePageContentListener(listener);
        // Verify listener is removed
    }

    @Test
    public void testSupportsContentLanguage() {
        Language language = new MockLanguage("en");
        assertTrue(page.supportsContentLanguage(language));
    }

    // Mock classes for testing
    private class PageImpl_RBL4Test_f985aed1 implements Pagelet {
        private String module;
        private String identifier;

        public MockPagelet(String module, String identifier) {
            this.module = module;
            this.identifier = identifier;
        }

        @Override
        public String getModule() {
            return module;
        }

        @Override
        public String getIdentifier() {
            return identifier;
        }

        @Override
        public String toXml() {
            return "<pagelet module=\"" + module + "\" id=\"" + identifier + "\" />";
        }
    }

    private class PageImpl_RBL4Test_f985aed1 implements PageContentListener {
        // Implement necessary methods for testing
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
