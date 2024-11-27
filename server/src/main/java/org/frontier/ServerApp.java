package org.frontier;

import org.frontier.processing.CommandMonitor;
import org.frontier.processing.ScreenRecorder;
import org.frontier.utils.Constants;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerApp {
    public static void main(String[] args) throws IOException, AWTException, InterruptedException {
        while (true) {
            setupConnectionToClient(args);
        }
    }

    private static void setupConnectionToClient(String[] args) throws IOException, AWTException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));

        Socket screenSocket = serverSocket.accept();

        List<Socket> socketList = new ArrayList<>(Constants.MOUSE_SCROLL_EVENT + 1);
        for (int i = 0; i < Constants.MOUSE_SCROLL_EVENT + 1; i++) {
            socketList.add(serverSocket.accept());
        }

        Robot robot = new Robot();

        ScreenRecorder screenRecorder = new ScreenRecorder(screenSocket, robot);
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
}