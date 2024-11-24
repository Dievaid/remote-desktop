package org.frontier;

import org.frontier.processing.CommandMonitor;
import org.frontier.processing.ScreenRecorder;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {
    public static void main(String[] args) throws IOException, AWTException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
        Socket socket = serverSocket.accept();
        Robot robot = new Robot();

        ScreenRecorder screenRecorder = new ScreenRecorder(socket, robot);
        CommandMonitor commandMonitor = new CommandMonitor(socket, robot);

        Thread recordingThread = new Thread(screenRecorder);
        Thread commandThread = new Thread(commandMonitor);

        recordingThread.start();
        commandThread.start();

        recordingThread.join();
        commandThread.join();

        serverSocket.close();
    }
}