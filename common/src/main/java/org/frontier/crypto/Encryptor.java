package org.frontier.crypto;

public interface Encryptor {
    byte[] encrypt(byte[] plaintext);
    byte[] decrypt(byte[] ciphertext);
}
