package org.frontier.screen;

import org.frontier.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public final class ScreenRecorder {
    private final Robot robot = new Robot();

    private final Rectangle frame = new Rectangle(
            Toolkit.getDefaultToolkit().getScreenSize());

    private final Socket socket;

    public ScreenRecorder(Socket socket) throws AWTException {
        this.socket = socket;
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
}
