package org.apache.calcite.avatica.remote;

import org.apache.calcite.avatica.remote.KerberosConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.Map.Entry;

import static org.junit.Assert.*;

public class KerberosConnection_RBL4_e0bbdbb3Test {
    private KerberosConnection kerberosConnection;
    private final String principal = "testPrincipal";
    private final File keytab = new File("path/to/keytab");

    @Before
    public void setUp() {
        kerberosConnection = new KerberosConnection(principal, keytab);
    }

    @After
    public void tearDown() {
        kerberosConnection.stopRenewalThread();
    }

    @Test
    public void testConstructor() {
        assertNotNull(kerberosConnection);
        assertEquals(principal, kerberosConnection.getSubject().getPrincipals().iterator().next().getName());
    }

    @Test
    public void testLogin() {
        try {
            kerberosConnection.login();
            Subject subject = kerberosConnection.getSubject();
            assertNotNull(subject);
            assertFalse(subject.getPrincipals().isEmpty());
        } catch (RuntimeException e) {
            fail("Login should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testPerformKerberosLogin() {
        try {
            Entry<LoginContext, Subject> entry = kerberosConnection.performKerberosLogin();
            assertNotNull(entry);
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
        } catch (RuntimeException e) {
            fail("Perform Kerberos login should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testStopRenewalThread() {
        kerberosConnection.login();
        kerberosConnection.stopRenewalThread();
        assertNull(kerberosConnection.getSubject());
    }

    @Test
    public void testIsTGSPrincipal() {
        assertTrue(KerberosConnection.isTGSPrincipal(new javax.security.auth.kerberos.KerberosPrincipal("krbtgt/REALM@REALM")));
        assertFalse(KerberosConnection.isTGSPrincipal(new javax.security.auth.kerberos.KerberosPrincipal("otherPrincipal")));
    }

    @Test
    public void testIsIbmJava() {
        boolean isIbm = KerberosConnection.isIbmJava();
        assertTrue(isIbm == (System.getProperty("java.vendor").contains("IBM")));
    }

    @Test
    public void testGetKrb5LoginModuleName() {
        String expectedModule = System.getProperty("java.vendor").contains("IBM") ?
                "com.ibm.security.auth.module.Krb5LoginModule" :
                "com.sun.security.auth.module.Krb5LoginModule";
        assertEquals(expectedModule, KerberosConnection.getKrb5LoginModuleName());
    }
}
