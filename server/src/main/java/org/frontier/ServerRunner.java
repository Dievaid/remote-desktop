package org.frontier;

import org.frontier.crypto.AESEncryptor;
import org.frontier.processing.CommandFactory;
import org.frontier.processing.CommandMonitor;
import org.frontier.processing.ScreenRecorder;
import org.frontier.service.AwtRobotService;
import org.frontier.service.RobotService;
import org.frontier.utils.Constants;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ServerRunner {

    private final RobotService robotService;
    private final ServerSocketFactory serverSocketFactory;

    @FunctionalInterface
    public interface ServerSocketFactory {
        ServerSocket create(int port) throws IOException;
    }

    public ServerRunner() throws AWTException {
        this(new AwtRobotService(), ServerSocket::new);
    }

    public ServerRunner(RobotService robotService, ServerSocketFactory serverSocketFactory) {
        this.robotService = robotService;
        this.serverSocketFactory = serverSocketFactory;
    }

    public void run(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException {
        while (true) {
            runOnce(args);
        }
    }

    void runOnce(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException {
        int port = Integer.parseInt(args[0]);
        try (ServerSocket serverSocket = serverSocketFactory.create(port)) {
            SecretKey key = parseOrGenerateKeyForArgs(args);

            Socket screenSocket = serverSocket.accept();

            List<Socket> socketList = new ArrayList<>(Constants.MOUSE_SCROLL_EVENT + 1);
            for (int i = 0; i < Constants.MOUSE_SCROLL_EVENT + 1; i++) {
                socketList.add(serverSocket.accept());
            }

            AESEncryptor encryptor = new AESEncryptor(key);
            Rectangle screenSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

            ScreenRecorder screenRecorder = new ScreenRecorder(screenSocket, robotService, screenSize, encryptor);
            CommandMonitor commandMonitor = new CommandMonitor(socketList, robotService, new CommandFactory());

            Thread recordingThread = new Thread(screenRecorder);
            Thread commandThread = new Thread(commandMonitor);

            recordingThread.start();
            commandThread.start();

            recordingThread.join();
            commandThread.join();

            for (Socket socket : socketList) {
                socket.close();
            }
        }
    }

    SecretKey parseOrGenerateKeyForArgs(String[] args) throws NoSuchAlgorithmException {
        if (args.length < 2) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            SecretKey key = keyGenerator.generateKey();
            System.out.printf(
                    "Your key is: %s%n",
                    Base64.getEncoder().encodeToString(key.getEncoded()));
            return key;
        }
        return new SecretKeySpec(Base64.getDecoder().decode(args[1]), "AES");
    }
}
