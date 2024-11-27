package org.frontier.control;

import org.frontier.utils.Constants;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MouseScrollHandler implements SocketHandler<MouseWheelEvent>, MouseWheelListener {
    private final DataOutputStream dataOutputStream;

    public MouseScrollHandler(Socket socket) throws IOException {
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        try {
            this.handle(e);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void handle(MouseWheelEvent e) throws IOException {
        dataOutputStream.writeInt(e.getUnitsToScroll());
        dataOutputStream.flush();
    }

    @Override
    public int getType() {
        return Constants.MOUSE_SCROLL_EVENT;
    }
}
