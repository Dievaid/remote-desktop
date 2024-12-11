import extensions.CryptoExtension;
import org.frontier.crypto.Encryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SomeStringTest {
    public SomeStringTest() throws NoSuchAlgorithmException {
    }

    @RegisterExtension
    CryptoExtension cryptoExtension = new CryptoExtension();

    @Test
    public void retrieveOriginalContentAfterEncryptAndDecrypt() {
        String originalContent = "foobar";
        Encryptor aesEncryptor = cryptoExtension.encryptor;

        byte[] encrypted = aesEncryptor.encrypt(originalContent.getBytes());
        byte[] decrypted = aesEncryptor.decrypt(encrypted);

        assertThat(originalContent.getBytes()).isEqualTo(decrypted);
    }
}
