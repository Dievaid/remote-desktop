package org.frontier.processing;

import lombok.extern.log4j.Log4j2;
import org.frontier.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.Robot;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

@Log4j2
public final class ScreenRecorder implements Runnable {
    private final Robot robot;

    private final Rectangle frame = new Rectangle(
            Toolkit.getDefaultToolkit().getScreenSize());

    private final Socket socket;

    public ScreenRecorder(Socket socket, Robot robot) {
        this.socket = socket;
        this.robot = robot;
    }

    public void startRecording() throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        while (socket.isConnected()) {
            BufferedImage image = robot.createScreenCapture(frame);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, Constants.PNG_FILE_EXTENSION, byteArrayOutputStream);

            byte[] bytes = byteArrayOutputStream.toByteArray();
            dataOutputStream.writeInt(bytes.length);
            dataOutputStream.write(bytes);
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
