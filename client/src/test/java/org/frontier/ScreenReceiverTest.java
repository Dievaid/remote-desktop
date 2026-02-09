package org.frontier;

import com.github.luben.zstd.Zstd;
import org.frontier.crypto.Encryptor;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ScreenReceiverTest {

    @Test
    void testScreenReceiverReceivesAndProcessesImage() throws IOException, InterruptedException {
        BufferedImage sentImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream pngStream = new ByteArrayOutputStream();
        ImageIO.write(sentImage, "png", pngStream);
        byte[] imageBytes = pngStream.toByteArray();

        byte[] compressedBytes = Zstd.compress(imageBytes);

        StubEncryptor encryptor = new StubEncryptor();
        StubImageConsumer consumer = new StubImageConsumer();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        dataOutputStream.writeInt(compressedBytes.length);
        dataOutputStream.writeInt(imageBytes.length);
        dataOutputStream.write(compressedBytes);
        dataOutputStream.flush();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        AtomicBoolean keepRunning = new AtomicBoolean(true);
        ScreenReceiver receiver = new ScreenReceiver(inputStream, encryptor, (img) -> {
            consumer.accept(img);
            keepRunning.set(false);
        }, keepRunning::get);

        try {
            receiver.run();
        } catch (RuntimeException e) {
        }

        assertTrue(encryptor.decryptCalled, "Decrypt should be called");
        assertTrue(consumer.accepted, "Consumer should accept image");
    }

    static class StubEncryptor implements Encryptor {
        boolean decryptCalled = false;

        @Override
        public byte[] encrypt(byte[] plaintext) {
            return plaintext;
        }

        @Override
        public byte[] decrypt(byte[] ciphertext) {
            decryptCalled = true;
            return ciphertext;
        }
    }

    static class StubImageConsumer implements Consumer<BufferedImage> {
        boolean accepted = false;

        @Override
        public void accept(BufferedImage bufferedImage) {
            accepted = true;
        }
    }
}
