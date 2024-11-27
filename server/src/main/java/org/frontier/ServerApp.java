package org.frontier;

import org.frontier.processing.CommandMonitor;
import org.frontier.processing.ScreenRecorder;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServerApp {
    public static void main(String[] args) throws IOException, AWTException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));

        Socket screenSocket = serverSocket.accept();
        Socket keystrokeSocket = serverSocket.accept();
        Socket mouseMoveSocket = serverSocket.accept();
        Socket mouseClickSocket = serverSocket.accept();
        Socket mouseScrollSocket = serverSocket.accept();

        Robot robot = new Robot();

        List<Socket> socketList = List.of(
                keystrokeSocket,
                mouseMoveSocket,
                mouseClickSocket,
                mouseScrollSocket
        );

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