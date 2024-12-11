import extensions.CryptoExtension;
import org.frontier.crypto.Encryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SomeImageTest {
    public SomeImageTest() throws NoSuchAlgorithmException {
    }

    @RegisterExtension
    CryptoExtension cryptoExtension = new CryptoExtension();


    @Test
    public void readImageSuccessfullyAfterEncryptAndDecrypt() throws IOException {
        InputStream inputStream = cryptoExtension.inputStream;
        Encryptor aesEncryptor = cryptoExtension.encryptor;

        byte[] notEncryptedImage = inputStream.readAllBytes();
        byte[] encryptedImage = aesEncryptor.encrypt(notEncryptedImage);
        byte[] decryptedImage = aesEncryptor.decrypt(encryptedImage);

        BufferedImage img = ImageIO.read(new ByteArrayInputStream(decryptedImage));

        assertThat(notEncryptedImage).contains(decryptedImage);
        assertThat(img).isNotNull();
    }

    @Test
    public void readImageSuccessFullyAfterPackingSizeOfDataAndEncryptedText() throws IOException {
        InputStream inputStream = cryptoExtension.inputStream;
        Encryptor aesEncryptor = cryptoExtension.encryptor;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        byte[] notEncryptedBytes = inputStream.readAllBytes();
        byte[] encryptedImage = aesEncryptor.encrypt(notEncryptedBytes);
        dataOutputStream.writeInt(encryptedImage.length);
        dataOutputStream.write(encryptedImage);

        byte[] packageBytes = byteArrayOutputStream.toByteArray();
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(packageBytes));
        int size = dataInputStream.readInt();
        byte[] encryptedBytes = dataInputStream.readNBytes(size);
        byte[] decryptedBytes = aesEncryptor.decrypt(encryptedBytes);

        assertThat(decryptedBytes).contains(notEncryptedBytes);
    }
}
