import org.frontier.crypto.AESEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class EncryptionTest {
    @Test
    public void whenEncryptingText_andDecryptingTextAgain_thenOriginalStringIsReturned()
            throws NoSuchAlgorithmException {
        String originalContent = "foobar";

        KeyGenerator.getInstance("AES").init(256);
        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();

        AESEncryptor aesEncryptor = new AESEncryptor(secretKey);

        byte[] encrypted = aesEncryptor.encrypt(originalContent.getBytes());

        Assertions.assertArrayEquals(
                originalContent.getBytes(),
                aesEncryptor.decrypt(encrypted)
        );
    }
}
