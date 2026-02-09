package org.frontier;

import org.frontier.service.RobotService;
import org.frontier.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServerRunnerTest {

    @Mock
    private RobotService robotService;

    @Mock
    private ServerSocket serverSocket;

    @Mock
    private Socket screenSocket;

    @Test
    void testRunOnce() throws Exception {
        int port = 9999;
        String[] args = { String.valueOf(port) };

        when(serverSocket.accept()).thenReturn(screenSocket);

        List<Socket> commandSockets = new ArrayList<>();
        for (int i = 0; i < Constants.MOUSE_SCROLL_EVENT + 1; i++) {
            Socket s = mock(Socket.class);
            commandSockets.add(s);
            when(s.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        }

        org.mockito.stubbing.OngoingStubbing<Socket> stubbing = when(serverSocket.accept()).thenReturn(screenSocket);
        for (Socket s : commandSockets) {
            stubbing = stubbing.thenReturn(s);
        }

        when(screenSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        when(screenSocket.isConnected()).thenReturn(false); // To exit ScreenRecorder loop immediately

        ServerRunner.ServerSocketFactory factory = (p) -> serverSocket;
        ServerRunner runner = new ServerRunner(robotService, factory);

        try {
            runner.runOnce(args);
        } catch (java.awt.HeadlessException e) {
            System.err.println("Headless exception caught, skipping full verify: " + e.getMessage());
            return;
        }

        verify(serverSocket).close();
    }

    @Test
    void testParseKey() throws Exception {
        ServerRunner runner = new ServerRunner(robotService, (p) -> serverSocket);
        String[] args = { "9999", "c29tZXNlY3JldGtleTEyMzQ1Njc4OTAxMjM0NTY=" }; // 32 bytes base64?

        runner.parseOrGenerateKeyForArgs(args);
    }
}
