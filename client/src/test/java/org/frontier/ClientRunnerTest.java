package org.frontier;

import org.frontier.control.KeyStrokeHandler;
import org.frontier.control.MouseClickHandler;
import org.frontier.control.MouseMoveHandler;
import org.frontier.control.MouseScrollHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.net.Socket;
import java.io.ByteArrayOutputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientRunnerTest {

    @Mock
    private ClientRunner.SocketFactory socketFactory;

    @Mock
    private ClientRunner.UIContext uiContext;

    @Mock
    private Socket socket;

    @Test
    void testRun() throws Exception {
        String[] args = { "localhost", "9999", "c29tZXNlY3JldGtleTEyMzQ1Njc4OTAxMjM0NTY=" };

        when(socketFactory.create(anyString(), anyInt())).thenReturn(socket);
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(socket.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        when(socket.isConnected()).thenReturn(false);

        ClientRunner runner = new ClientRunner(socketFactory);

        runner.run(args, uiContext);

        verify(socketFactory, atLeastOnce()).create(anyString(), anyInt());

        verify(uiContext).addKeyListener(any(KeyStrokeHandler.class));
        verify(uiContext).addMouseMotionListener(any(MouseMoveHandler.class));
        verify(uiContext).addMouseListener(any(MouseClickHandler.class));
        verify(uiContext).addMouseWheelListener(any(MouseScrollHandler.class));
    }

    @Test
    void testJFrameUIContext() {
        javax.swing.JFrame frame = mock(javax.swing.JFrame.class);
        javax.swing.JLabel label = mock(javax.swing.JLabel.class);
        ClientRunner.JFrameUIContext context = new ClientRunner.JFrameUIContext(frame, label);

        context.addKeyListener(mock(KeyStrokeHandler.class));
        verify(frame).addKeyListener(any(KeyStrokeHandler.class));

        context.addMouseMotionListener(mock(MouseMoveHandler.class));
        verify(frame).addMouseMotionListener(any(MouseMoveHandler.class));

        context.addMouseListener(mock(MouseClickHandler.class));
        verify(frame).addMouseListener(any(MouseClickHandler.class));

        context.addMouseWheelListener(mock(MouseScrollHandler.class));
        verify(frame).addMouseWheelListener(any(MouseScrollHandler.class));

        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(10, 10,
                java.awt.image.BufferedImage.TYPE_INT_ARGB);
        context.showImage(image);
        verify(label).setIcon(any(javax.swing.ImageIcon.class));
        verify(frame).repaint();
    }
}
