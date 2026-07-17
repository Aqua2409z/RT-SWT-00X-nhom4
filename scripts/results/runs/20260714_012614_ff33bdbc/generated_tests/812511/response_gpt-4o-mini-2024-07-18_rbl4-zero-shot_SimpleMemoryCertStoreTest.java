
package org.globus.gsi.provider.simple;

import org.junit.Before;
import org.junit.Test;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertStoreException;
import java.security.cert.CertStoreParameters;
import java.security.cert.CertSelector;
import java.security.cert.X509Certificate;
import java.security.cert.X509CRL;
import java.security.cert.X509CertSelector;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;

public class SimpleMemoryCertStoreTest {

    private SimpleMemoryCertStore certStore;
    private X509Certificate cert;
    private X509CRL crl;

    @Before
    public void setUp() throws Exception {
        cert = createMockCertificate();
        crl = createMockCRL();
        SimpleMemoryCertStoreParams params = new SimpleMemoryCertStoreParams(new X509Certificate[]{cert}, new X509CRL[]{crl});
        certStore = new SimpleMemoryCertStore(params);
    }

    @Test(expected = InvalidAlgorithmParameterException.class)
    public void testConstructorWithNullParams() throws Exception {
        new SimpleMemoryCertStore(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithWrongParamsType() throws Exception {
        CertStoreParameters wrongParams = new CertStoreParameters() {};
        new SimpleMemoryCertStore(wrongParams);
    }

    @Test
    public void testEngineGetCertificates() throws CertStoreException {
        X509CertSelector selector = new X509CertSelector();
        selector.setCertificate(cert);
        Collection<? extends X509Certificate> certificates = certStore.engineGetCertificates(selector);
        assertEquals(1, certificates.size());
        assertTrue(certificates.contains(cert));
    }

    @Test
    public void testEngineGetCRLs() throws CertStoreException {
        CRLSelector selector = new X509CertSelector();
        Collection<? extends CRL> crls = certStore.engineGetCRLs(selector);
        assertEquals(1, crls.size());
        assertTrue(crls.contains(crl));
    }

    @Test
    public void testEngineGetCertificatesWithNoMatch() throws CertStoreException {
        X509CertSelector selector = new X509CertSelector();
        selector.setCertificate(createMockCertificate()); // Different certificate
        Collection<? extends X509Certificate> certificates = certStore.engineGetCertificates(selector);
        assertTrue(certificates.isEmpty());
    }

    @Test
    public void testEngineGetCRLsWithNoMatch() throws CertStoreException {
        CRLSelector selector = new X509CertSelector();
        Collection<? extends CRL> crls = certStore.engineGetCRLs(selector);
        assertTrue(crls.isEmpty());
    }

    private X509Certificate createMockCertificate() {
        // Implement a method to create a mock X509Certificate
        return null; // Replace with actual mock
    }

    private X509CRL createMockCRL() {
        // Implement a method to create a mock X509CRL
        return null; // Replace with actual mock
    }
}
