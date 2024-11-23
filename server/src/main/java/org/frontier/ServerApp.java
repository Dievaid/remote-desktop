package org.frontier;

import org.frontier.screen.ScreenRecorder;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;

public class ServerApp {
    public static void main(String[] args) throws IOException, AWTException {
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
        ScreenRecorder screenRecorder = new ScreenRecorder(serverSocket.accept());

        screenRecorder.startRecording();
        serverSocket.close();
    }
}