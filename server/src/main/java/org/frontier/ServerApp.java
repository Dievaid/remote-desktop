package org.frontier;

import lombok.extern.log4j.Log4j2;
import org.frontier.processing.CommandMonitor;
import org.frontier.processing.ScreenRecorder;
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

@Log4j2
public class ServerApp {
    public static void main(String[] args) throws IOException, AWTException, InterruptedException, NoSuchAlgorithmException {
        while (true) {
            setupConnectionToClient(args);
        }
    }

    private static void setupConnectionToClient(String[] args) throws IOException, AWTException, InterruptedException, NoSuchAlgorithmException {
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));

        SecretKey key = parseOrGenerateKeyForArgs(args);

        Socket screenSocket = serverSocket.accept();

        List<Socket> socketList = new ArrayList<>(Constants.MOUSE_SCROLL_EVENT + 1);
        for (int i = 0; i < Constants.MOUSE_SCROLL_EVENT + 1; i++) {
            socketList.add(serverSocket.accept());
        }

        Robot robot = new Robot();

        ScreenRecorder screenRecorder = new ScreenRecorder(screenSocket, robot, key);
        CommandMonitor commandMonitor = new CommandMonitor(socketList, robot);

        Thread recordingThread = new Thread(screenRecorder);
        Thread commandThread = new Thread(commandMonitor);

        recordingThread.start();
        commandThread.start();

        recordingThread.join();
        commandThread.join();

        for (Socket socket : socketList) {
            socket.close();
        }
        serverSocket.close();
    }

    private static SecretKey parseOrGenerateKeyForArgs(String[] args) throws NoSuchAlgorithmException {
        if (args.length < 2) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            SecretKey key = keyGenerator.generateKey();
            System.out.printf(
                    "Your key is: %s%n",
                    Base64.getEncoder().encodeToString(key.getEncoded())
            );
            return key;
        }
        return new SecretKeySpec(Base64.getDecoder().decode(args[1]), "AES");
    }
}