package org.frontier;

import com.github.luben.zstd.Zstd;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.frontier.crypto.Encryptor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class ScreenReceiver implements Runnable {
    private static final Logger log = LogManager.getLogger(ScreenReceiver.class);
    private final DataInputStream dataInputStream;
    private final Encryptor encryptor;
    private final Consumer<BufferedImage> imageConsumer;
    private final BooleanSupplier loopCondition;

    public ScreenReceiver(InputStream inputStream, Encryptor encryptor, Consumer<BufferedImage> imageConsumer,
            BooleanSupplier loopCondition) {
        this.dataInputStream = new DataInputStream(inputStream);
        this.encryptor = encryptor;
        this.imageConsumer = imageConsumer;
        this.loopCondition = loopCondition;
    }

    @Override
    public void run() {
        try {
            while (loopCondition.getAsBoolean()) {
                int encryptedLength = dataInputStream.readInt();
                int originalLength = dataInputStream.readInt();

                byte[] encryptedBytes = new byte[encryptedLength];
                dataInputStream.readFully(encryptedBytes);

                byte[] compressedBytes = encryptor.decrypt(encryptedBytes);

                byte[] imageBytes = new byte[originalLength];
                long decompressedSize = Zstd.decompress(imageBytes, compressedBytes);

                if (decompressedSize != originalLength) {
                    log.warn("Decompressed size does not match original length: expected {}, got {}", originalLength, decompressedSize);
                }

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
                BufferedImage receivedImage = ImageIO.read(byteArrayInputStream);

                if (receivedImage != null) {
                    imageConsumer.accept(receivedImage);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
