package ch.entwine.weblounge.common.impl.content.page;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ch.entwine.weblounge.common.content.ResourceURI;
import ch.entwine.weblounge.common.impl.content.page.PageletURIImpl;
import ch.entwine.weblounge.common.site.Site;

public class PageletURIImpl_RBL4_a30ec522Test {

    private ResourceURI mockResourceURI;
    private PageletURIImpl pageletURI;

    @Before
    public void setUp() {
        mockResourceURI = new ResourceURI() {
            @Override
            public Site getSite() {
                return new Site() {
                    @Override
                    public String getName() {
                        return "TestSite";
                    }
                };
            }

            @Override
            public int hashCode() {
                return 12345; // Mock hash code
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof ResourceURI;
            }
        };
        pageletURI = new PageletURIImpl(mockResourceURI, "main", 7);
    }

    @Test
    public void testGetSite() {
        assertEquals("TestSite", pageletURI.getSite().getName());
    }

    @Test
    public void testGetPageURI() {
        assertEquals(mockResourceURI, pageletURI.getPageURI());
    }

    @Test
    public void testGetComposer() {
        assertEquals("main", pageletURI.getComposer());
    }

    @Test
    public void testGetPosition() {
        assertEquals(7, pageletURI.getPosition());
    }

    @Test
    public void testSetURI() {
        ResourceURI newResourceURI = new ResourceURI() {
            @Override
            public Site getSite() {
                return new Site() {
                    @Override
                    public String getName() {
                        return "NewSite";
                    }
                };
            }
        };
        pageletURI.setURI(newResourceURI);
        assertEquals(newResourceURI, pageletURI.getPageURI());
    }

    @Test
    public void testSetComposer() {
        pageletURI.setComposer("newComposer");
        assertEquals("newComposer", pageletURI.getComposer());
    }

    @Test
    public void testSetPosition() {
        pageletURI.setPosition(10);
        assertEquals(10, pageletURI.getPosition());
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = (mockResourceURI.hashCode() >> 24) | (pageletURI.getComposer().hashCode() << 16) | (pageletURI.getPosition() << 24);
        assertEquals(expectedHashCode, pageletURI.hashCode());
    }

    @Test
    public void testEquals() {
        PageletURIImpl anotherPageletURI = new PageletURIImpl(mockResourceURI, "main", 7);
        assertTrue(pageletURI.equals(anotherPageletURI));
        assertFalse(pageletURI.equals(null));
        assertFalse(pageletURI.equals(new Object()));
    }

    @Test
    public void testCompareTo() {
        PageletURIImpl lowerPosition = new PageletURIImpl(mockResourceURI, "main", 5);
        PageletURIImpl higherPosition = new PageletURIImpl(mockResourceURI, "main", 10);
        assertTrue(pageletURI.compareTo(lowerPosition) > 0);
        assertTrue(pageletURI.compareTo(higherPosition) < 0);
        assertTrue(pageletURI.compareTo(pageletURI) == 0);
    }

    @Test
    public void testToString() {
        String expectedString = mockResourceURI + " [composer=main,position=7]";
        assertEquals(expectedString, pageletURI.toString());
    }
}
