package org.globus.gsi;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import static org.junit.Assert.*;

public class OpenSSLKeyTest {

    private OpenSSLKey openSSLKey;
    private String pemKey;
    private String encryptedPemKey;
    private String password = "testPassword";

    @Before
    public void setUp() throws Exception {
        // Example PEM key (replace with a valid one for real tests)
        pemKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIIEpAIBAAKCAQEA...\n" + // truncated for brevity
                "-----END RSA PRIVATE KEY-----";

        // Example encrypted PEM key (replace with a valid one for real tests)
        encryptedPemKey = "-----BEGIN ENCRYPTED PRIVATE KEY-----\n" +
                "Proc-Type: 4,ENCRYPTED\n" +
                "DEK-Info: AES-256-CBC,1234567890abcdef\n" +
                "...\n" + // truncated for brevity
                "-----END ENCRYPTED PRIVATE KEY-----";
    }

    @Test
    public void testOpenSSLKeyFromInputStream() throws Exception {
        openSSLKey = new OpenSSLKey(new ByteArrayInputStream(pemKey.getBytes()));
        assertNotNull(openSSLKey.getPrivateKey());
        assertFalse(openSSLKey.isEncrypted());
    }

    @Test
    public void testOpenSSLKeyFromFile() throws Exception {
        // Assuming a method to create a temporary file with the PEM key
        String tempFilePath = createTempFileWithContent(pemKey);
        openSSLKey = new OpenSSLKey(tempFilePath);
        assertNotNull(openSSLKey.getPrivateKey());
        assertFalse(openSSLKey.isEncrypted());
    }

    @Test
    public void testEncryptAndDecrypt() throws Exception {
        openSSLKey = new OpenSSLKey(new ByteArrayInputStream(pemKey.getBytes()));
        openSSLKey.encrypt(password);
        assertTrue(openSSLKey.isEncrypted());

        openSSLKey.decrypt(password);
        assertFalse(openSSLKey.isEncrypted());
        assertNotNull(openSSLKey.getPrivateKey());
    }

    @Test(expected = GeneralSecurityException.class)
    public void testDecryptWithoutEncryption() throws Exception {
        openSSLKey = new OpenSSLKey(new ByteArrayInputStream(pemKey.getBytes()));
        openSSLKey.decrypt(password); // Should not throw
    }

    @Test
    public void testWriteToOutputStream() throws Exception {
        openSSLKey = new OpenSSLKey(new ByteArrayInputStream(pemKey.getBytes()));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        openSSLKey.writeTo(outputStream);
        String result = outputStream.toString();
        assertTrue(result.contains("-----BEGIN RSA PRIVATE KEY-----"));
    }

    @Test
    public void testEqualsAndHashCode() throws Exception {
        OpenSSLKey key1 = new OpenSSLKey(new ByteArrayInputStream(pemKey.getBytes()));
        OpenSSLKey key2 = new OpenSSLKey(new ByteArrayInputStream(pemKey.getBytes()));
        assertEquals(key1, key2);
        assertEquals(key1.hashCode(), key2.hashCode());
    }

    @Test
    public void testSetEncryptionAlgorithm() throws Exception {
        openSSLKey = new OpenSSLKey(new ByteArrayInputStream(pemKey.getBytes()));
        openSSLKey.setEncryptionAlgorithm("AES-256-CBC");
        assertTrue(openSSLKey.isEncrypted());
    }

    private String createTempFileWithContent(String content) throws IOException {
        // Implement a method to create a temporary file with the given content
        // and return the file path
        return "tempFilePath"; // Placeholder
    }
}
