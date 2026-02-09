package org.frontier.processing;

import com.github.luben.zstd.Zstd;
import org.frontier.crypto.Encryptor;
import org.frontier.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

import org.frontier.service.RobotService;

import java.util.function.BooleanSupplier;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public final class ScreenRecorder implements Runnable {
    private static final Logger log = LogManager.getLogger(ScreenRecorder.class);
    private final RobotService robotService;
    private final Rectangle frame;
    private final Encryptor encryptor;
    private final BooleanSupplier loopCondition;
    private final OutputStream outputStream;

    public ScreenRecorder(Socket socket, RobotService robotService, Rectangle frame, Encryptor encryptor)
            throws IOException {
        this(robotService, frame, encryptor, socket::isConnected, socket.getOutputStream());
    }

    public ScreenRecorder(RobotService robotService, Rectangle frame, Encryptor encryptor,
            BooleanSupplier loopCondition, OutputStream outputStream) {
        this.robotService = robotService;
        this.frame = frame;
        this.encryptor = encryptor;
        this.loopCondition = loopCondition;
        this.outputStream = outputStream;
    }

    public void startRecording() throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        while (loopCondition.getAsBoolean()) {
            BufferedImage image = robotService.createScreenCapture(frame);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, Constants.PNG_FILE_EXTENSION, byteArrayOutputStream);

            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            byte[] bytes = Zstd.compress(imageBytes);
            byte[] encryptedBytes = encryptor.encrypt(bytes);

            dataOutputStream.writeInt(encryptedBytes.length);
            dataOutputStream.writeInt(imageBytes.length);
            dataOutputStream.write(encryptedBytes);
            dataOutputStream.flush();
        }
    }

    @Override
    public void run() {
        try {
            this.startRecording();
        } catch (IOException e) {
            log.error("Failed to send screen data", e);
        }
    }
}
