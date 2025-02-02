package org.frontier;

import com.github.luben.zstd.Zstd;
import lombok.extern.log4j.Log4j2;
import org.frontier.control.KeyStrokeHandler;
import org.frontier.control.MouseClickHandler;
import org.frontier.control.MouseMoveHandler;
import org.frontier.control.MouseScrollHandler;
import org.frontier.crypto.AESEncryptor;
import org.frontier.utils.Constants;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.Base64;
import java.util.List;
import java.util.stream.IntStream;

@Log4j2
public class ClientApp {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
            SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(args[2]), "AES");
            AESEncryptor encryptor = new AESEncryptor(secretKey);

            List<Socket> socketList = IntStream.range(0, Constants.MOUSE_SCROLL_EVENT + 1)
                    .mapToObj(idx -> {
                        try {
                            return new Socket(args[0], Integer.parseInt(args[1]));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

            JFrame frame = new JFrame(Constants.APP_NAME);
            JLabel imageLabel = new JLabel();

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            frame.add(imageLabel);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(
                    Double.valueOf(screenSize.getWidth()).intValue(),
                    Double.valueOf(screenSize.getHeight()).intValue()
            );

            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            frame.addKeyListener(new KeyStrokeHandler(socketList.get(Constants.KEY_STROKE_EVENT)));
            frame.addMouseMotionListener(new MouseMoveHandler(socketList.get(Constants.MOUSE_MOVE_EVENT)));
            frame.addMouseListener(new MouseClickHandler(socketList.get(Constants.MOUSE_CLICK_EVENT)));
            frame.addMouseWheelListener(new MouseScrollHandler(socketList.get(Constants.MOUSE_SCROLL_EVENT)));

            while (socket.isConnected()) {
                int imageLength = dataInputStream.readInt();
                int compressedLength = dataInputStream.readInt();
                byte[] encryptedImageBytes = dataInputStream.readNBytes(imageLength);
                byte[] compressedBytes = encryptor.decrypt(encryptedImageBytes);
                byte[] imageBytes = Zstd.decompress(compressedBytes, compressedLength);

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
                BufferedImage receivedImage = ImageIO.read(byteArrayInputStream);

                imageLabel.setIcon(new ImageIcon(receivedImage));
                frame.repaint();
            }

        } catch (Exception e) {
            log.error(e);
        }
    }
}