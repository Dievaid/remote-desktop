package org.frontier.crypto;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;

public class AESEncryptor implements Encryptor {
    private final SecretKey secretKey;

    public AESEncryptor(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public byte[] encrypt(byte[] plaintext) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] iv = getIV();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
            byte[] encryptedBytes = cipher.doFinal(plaintext);

            ByteArrayOutputStream bytesMerge = new ByteArrayOutputStream();
            bytesMerge.write(iv);
            bytesMerge.write(encryptedBytes);
            return bytesMerge.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] ciphertext) {
        try {
            ByteArrayInputStream inputBytes = new ByteArrayInputStream(ciphertext);
            byte[] iv = inputBytes.readNBytes(16);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
            return cipher.doFinal(inputBytes.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] getIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
}
