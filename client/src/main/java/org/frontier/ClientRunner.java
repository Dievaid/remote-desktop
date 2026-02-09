package org.frontier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.frontier.control.*;
import org.frontier.crypto.AESEncryptor;
import org.frontier.crypto.Encryptor;
import org.frontier.utils.Constants;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.util.Base64;
import java.util.List;
import java.util.stream.IntStream;

public class ClientRunner {

    private static final Logger log = LogManager.getLogger(ClientRunner.class);

    private final SocketFactory socketFactory;

    @FunctionalInterface
    public interface SocketFactory {
        Socket create(String host, int port) throws IOException;
    }

    public interface UIContext {
        void addKeyListener(KeyStrokeHandler handler);

        void addMouseMotionListener(MouseMoveHandler handler);

        void addMouseListener(MouseClickHandler handler);

        void addMouseWheelListener(MouseScrollHandler handler);

        void showImage(BufferedImage image);
    }

    public static class JFrameUIContext implements UIContext {
        private final JFrame frame;
        private final JLabel imageLabel;

        public JFrameUIContext(JFrame frame, JLabel imageLabel) {
            this.frame = frame;
            this.imageLabel = imageLabel;
        }

        @Override
        public void addKeyListener(KeyStrokeHandler handler) {
            frame.addKeyListener(handler);
        }

        @Override
        public void addMouseMotionListener(MouseMoveHandler handler) {
            frame.addMouseMotionListener(handler);
        }

        @Override
        public void addMouseListener(MouseClickHandler handler) {
            frame.addMouseListener(handler);
        }

        @Override
        public void addMouseWheelListener(MouseScrollHandler handler) {
            frame.addMouseWheelListener(handler);
        }

        @Override
        public void showImage(BufferedImage image) {
            imageLabel.setIcon(new ImageIcon(image));
            frame.repaint();
            frame.pack();
        }
    }

    public ClientRunner() {
        this(Socket::new);
    }

    public ClientRunner(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    public void run(String[] args, UIContext uiContext) {
        try {
            String host = args[0];
            int port = Integer.parseInt(args[1]);

            Socket socket = socketFactory.create(host, port);
            SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(args[2]), "AES");
            Encryptor encryptor = new AESEncryptor(secretKey);

            List<Socket> socketList = IntStream.range(0, Constants.MOUSE_SCROLL_EVENT + 1)
                    .mapToObj(idx -> {
                        try {
                            return socketFactory.create(host, port);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

            uiContext
                    .addKeyListener(new KeyStrokeHandler(socketList.get(Constants.KEY_STROKE_EVENT).getOutputStream()));
            uiContext.addMouseMotionListener(
                    new MouseMoveHandler(socketList.get(Constants.MOUSE_MOVE_EVENT).getOutputStream()));
            uiContext.addMouseListener(
                    new MouseClickHandler(socketList.get(Constants.MOUSE_CLICK_EVENT).getOutputStream()));
            uiContext.addMouseWheelListener(
                    new MouseScrollHandler(socketList.get(Constants.MOUSE_SCROLL_EVENT).getOutputStream()));

            ScreenReceiver screenReceiver = new ScreenReceiver(
                    socket.getInputStream(),
                    encryptor,
                    uiContext::showImage,
                    socket::isConnected);
            screenReceiver.run();

        } catch (Exception e) {
            log.error(e);
        }
    }
}
