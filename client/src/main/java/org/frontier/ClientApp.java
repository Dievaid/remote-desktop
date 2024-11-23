package org.frontier;

import org.frontier.utils.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) {
        try (Socket socket = new Socket(args[0], Integer.parseInt(args[1]))) {
            JFrame frame = new JFrame(Constants.APP_NAME);
            JLabel imageLabel = new JLabel();

            frame.add(imageLabel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setVisible(true);

            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            while (true) {
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