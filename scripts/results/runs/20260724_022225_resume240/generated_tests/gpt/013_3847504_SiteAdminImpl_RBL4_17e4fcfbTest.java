package ch.entwine.weblounge.common.impl.security;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class SiteAdminImpl_RBL4_17e4fcfbTest {

    private SiteAdminImpl siteAdmin;
    private Site mockSite;

    @Before
    public void setUp() {
        mockSite = mock(Site.class);
        siteAdmin = new SiteAdminImpl("admin");
    }

    @Test
    public void testConstructor() {
        assertEquals("admin", siteAdmin.getLogin());
        assertEquals("Site Administrator (admin)", siteAdmin.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetRealm() {
        siteAdmin.setRealm("newRealm");
    }

    @Test
    public void testImplies() {
        assertTrue(siteAdmin.implies(SystemRole.SITEADMIN));
        assertFalse(siteAdmin.implies(SystemRole.SYSTEMADMIN));
    }

    @Test
    public void testEquals() {
        SiteAdminImpl anotherAdmin = new SiteAdminImpl("admin");
        assertTrue(siteAdmin.equals(anotherAdmin));
        assertFalse(siteAdmin.equals(null));
        assertFalse(siteAdmin.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        assertEquals(siteAdmin.hashCode(), new SiteAdminImpl("admin").hashCode());
    }

    @Test
    public void testFromXml() throws Exception {
        String xml = "<administrator><login>admin</login><name>Admin User</name><email>admin@example.com</email></administrator>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new java.io.ByteArrayInputStream(xml.getBytes()));
        Node userNode = document.getDocumentElement();

        when(mockSite.getDefaultLanguage()).thenReturn(Language.ENGLISH);

        SiteAdminImpl adminFromXml = SiteAdminImpl.fromXml(userNode, mockSite);
        assertNotNull(adminFromXml);
        assertEquals("admin", adminFromXml.getLogin());
        assertEquals("Admin User", adminFromXml.getName());
        assertEquals("admin@example.com", adminFromXml.getEmail());
    }

    @Test
    public void testToXml() {
        siteAdmin.setEmail("admin@example.com");
        String xml = siteAdmin.toXml();
        assertTrue(xml.contains("<login>admin</login>"));
        assertTrue(xml.contains("<email>admin@example.com</email>"));
    }
}
