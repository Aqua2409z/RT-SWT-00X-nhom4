
package org.globus.gsi.provider.simple;

import org.junit.Before;
import org.junit.Test;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertStoreException;
import java.security.cert.CertStoreParameters;
import java.security.cert.X509Certificate;
import java.security.cert.X509CRL;
import java.security.cert.CRLSelector;
import java.security.cert.CertSelector;
import java.security.cert.X509CertSelector;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collection;

public class SimpleMemoryCertStoreTest {

    private SimpleMemoryCertStore certStore;
    private SimpleMemoryCertStoreParams params;

    @Before
    public void setUp() throws InvalidAlgorithmParameterException {
        params = mock(SimpleMemoryCertStoreParams.class);
        certStore = new SimpleMemoryCertStore(params);
    }

    @Test(expected = InvalidAlgorithmParameterException.class)
    public void testConstructorWithNullParams() throws InvalidAlgorithmParameterException {
        new SimpleMemoryCertStore(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithWrongParamsType() throws InvalidAlgorithmParameterException {
        CertStoreParameters wrongParams = mock(CertStoreParameters.class);
        new SimpleMemoryCertStore(wrongParams);
    }

    @Test
    public void testConstructorWithValidParams() throws InvalidAlgorithmParameterException {
        X509Certificate cert = mock(X509Certificate.class);
        X509CRL crl = mock(X509CRL.class);
        when(params.getCerts()).thenReturn(new X509Certificate[]{cert});
        when(params.getCrls()).thenReturn(new X509CRL[]{crl});

        certStore = new SimpleMemoryCertStore(params);

        assertNotNull(certStore);
    }

    @Test
    public void testEngineGetCRLs() throws CertStoreException {
        X509CRL crl1 = mock(X509CRL.class);
        X509CRL crl2 = mock(X509CRL.class);
        when(params.getCrls()).thenReturn(new X509CRL[]{crl1, crl2});
        certStore = new SimpleMemoryCertStore(params);

        CRLSelector selector = mock(CRLSelector.class);
        when(selector.match(crl1)).thenReturn(true);
        when(selector.match(crl2)).thenReturn(false);

        Collection<? extends CRL> crls = certStore.engineGetCRLs(selector);
        assertEquals(1, crls.size());
        assertTrue(crls.contains(crl1));
    }

    @Test
    public void testEngineGetCertificates() throws CertStoreException {
        X509Certificate cert1 = mock(X509Certificate.class);
        X509Certificate cert2 = mock(X509Certificate.class);
        when(params.getCerts()).thenReturn(new X509Certificate[]{cert1, cert2});
        certStore = new SimpleMemoryCertStore(params);

        X509CertSelector selector = new X509CertSelector();
        when(selector.match(cert1)).thenReturn(true);
        when(selector.match(cert2)).thenReturn(false);

        Collection<? extends Certificate> certs = certStore.engineGetCertificates(selector);
        assertEquals(1, certs.size());
        assertTrue(certs.contains(cert1));
    }
}
