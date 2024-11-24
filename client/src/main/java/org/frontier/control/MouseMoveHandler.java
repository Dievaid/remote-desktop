package org.frontier.control;

import org.frontier.utils.Constants;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MouseMoveHandler extends MouseMotionAdapter implements SocketHandler<MouseEvent> {
    private final DataOutputStream dataOutputStream;

    public MouseMoveHandler(Socket socket) throws IOException {
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        try {
            this.handle(e);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public void handle(MouseEvent e) throws IOException {
        dataOutputStream.writeInt(e.getX());
        dataOutputStream.writeInt(e.getY());
        dataOutputStream.flush();
    }

    @Override
    public int getType() {
        return Constants.MOUSE_MOVE_EVENT;
    }
}
