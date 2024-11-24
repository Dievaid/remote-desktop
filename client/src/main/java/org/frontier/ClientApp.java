package org.frontier;

import org.frontier.control.KeyStrokeHandler;
import org.frontier.control.MouseClickHandler;
import org.frontier.control.MouseMoveHandler;
import org.frontier.utils.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class ClientApp {
    public static void main(String[] args) {
        try (Socket socket = new Socket(args[0], Integer.parseInt(args[1]))) {
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
            OutputStream outputStream = socket.getOutputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            ReentrantLock lock = new ReentrantLock();
            frame.addMouseListener(new MouseClickHandler(outputStream, lock));
            frame.addMouseMotionListener(new MouseMoveHandler(outputStream, lock));
            frame.addKeyListener(new KeyStrokeHandler(outputStream, lock));

            while (socket.isConnected()) {
                int imageLength = dataInputStream.readInt();

                byte[] imageBytes = new byte[imageLength];
                dataInputStream.readFully(imageBytes);

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
                BufferedImage receivedImage = ImageIO.read(byteArrayInputStream);

                imageLabel.setIcon(new ImageIcon(receivedImage));
                frame.repaint();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}