package org.frontier.crypto;

import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class AESEncryptorTest {

    @Test
    void testEncryptDecrypt() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();
        AESEncryptor encryptor = new AESEncryptor(secretKey);

        String originalText = "Hello World";
        byte[] plaintext = originalText.getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = encryptor.encrypt(plaintext);
        byte[] decrypted = encryptor.decrypt(encrypted);

        assertNotNull(encrypted);
        assertNotEquals(plaintext.length, encrypted.length);
        assertArrayEquals(plaintext, decrypted);
        assertEquals(originalText, new String(decrypted, StandardCharsets.UTF_8));
    }
}
