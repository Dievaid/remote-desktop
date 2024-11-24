package org.frontier;

import lombok.extern.log4j.Log4j2;
import org.frontier.control.KeyStrokeHandler;
import org.frontier.control.MouseClickHandler;
import org.frontier.control.MouseMoveHandler;
import org.frontier.utils.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.stream.IntStream;

@Log4j2
public class ClientApp {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
            List<Socket> socketList = IntStream.range(0, Constants.MOUSE_CLICK_EVENT + 1)
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

            frame.addMouseListener(new MouseClickHandler(socketList.get(Constants.MOUSE_CLICK_EVENT)));
            frame.addMouseMotionListener(new MouseMoveHandler(socketList.get(Constants.MOUSE_MOVE_EVENT)));
            frame.addKeyListener(new KeyStrokeHandler(socketList.get(Constants.KEY_STROKE_EVENT)));

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
            log.error(e);
        }
    }
}