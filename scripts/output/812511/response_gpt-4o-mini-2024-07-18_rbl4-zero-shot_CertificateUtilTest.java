package org.globus.gsi.util;

import org.globus.gsi.util.CertificateUtil;
import org.globus.gsi.GSIConstants;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERBitString;
import org.junit.Test;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.EnumSet;

import static org.junit.Assert.*;

public class CertificateUtilTest {

    @Test
    public void testSetProvider() {
        CertificateUtil.setProvider("BC");
        // Verify provider is set correctly
        assertNotNull(CertificateUtil.getProvider());
    }

    @Test
    public void testGenerateKeyPair() throws GeneralSecurityException {
        KeyPair keyPair = CertificateUtil.generateKeyPair("RSA", 2048);
        assertNotNull(keyPair);
        assertNotNull(keyPair.getPublic());
        assertNotNull(keyPair.getPrivate());
    }

    @Test
    public void testGetCAPathConstraint() throws IOException {
        TBSCertificateStructure cert = createMockTBSCertificateStructure();
        int constraint = CertificateUtil.getCAPathConstraint(cert);
        assertEquals(-1, constraint); // Adjust based on mock data
    }

    @Test
    public void testGetCertificateType() throws CertificateException, IOException {
        TBSCertificateStructure cert = createMockTBSCertificateStructure();
        GSIConstants.CertificateType type = CertificateUtil.getCertificateType(cert);
        assertEquals(GSIConstants.CertificateType.EEC, type); // Adjust based on mock data
    }

    @Test
    public void testToASN1Primitive() throws IOException {
        byte[] data = new byte[]{0x30, 0x0A}; // Example DER-encoded data
        ASN1Primitive primitive = CertificateUtil.toASN1Primitive(data);
        assertNotNull(primitive);
    }

    @Test
    public void testGetBasicConstraints() throws IOException {
        X509Extension ext = createMockX509Extension();
        BasicConstraints constraints = CertificateUtil.getBasicConstraints(ext);
        assertNotNull(constraints);
    }

    @Test
    public void testGetKeyUsage() throws IOException {
        TBSCertificateStructure cert = createMockTBSCertificateStructure();
        EnumSet<KeyUsage> keyUsage = CertificateUtil.getKeyUsage(cert);
        assertNotNull(keyUsage);
    }

    @Test
    public void testToGlobusID() {
        String dn = "CN=Test, OU=Unit, O=Org";
        String globusID = CertificateUtil.toGlobusID(dn);
        assertEquals("/CN=Test/OU=Unit/O=Org", globusID);
    }

    @Test
    public void testToPrincipal() {
        String globusID = "/CN=Test/OU=Unit/O=Org";
        X500Principal principal = CertificateUtil.toPrincipal(globusID);
        assertNotNull(principal);
        assertEquals(globusID, principal.getName());
    }

    // Mock methods for testing
    private TBSCertificateStructure createMockTBSCertificateStructure() {
        // Create and return a mock TBSCertificateStructure
        return null; // Replace with actual mock
    }

    private X509Extension createMockX509Extension() {
        // Create and return a mock X509Extension
        return null; // Replace with actual mock
    }
}
