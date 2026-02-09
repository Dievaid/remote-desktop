package org.frontier.control;

import org.frontier.utils.Constants;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.io.IOException;

public class MouseClickHandler extends MouseAdapter implements SocketHandler<MouseEvent> {
    private final DataOutputStream dataOutputStream;

    public MouseClickHandler(java.io.OutputStream outputStream) {
        this.dataOutputStream = new DataOutputStream(outputStream);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        try {
            this.handle(e);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void handle(MouseEvent e) throws IOException {
        dataOutputStream.writeInt(e.getButton());
        dataOutputStream.writeInt(e.getX());
        dataOutputStream.writeInt(e.getY());
        dataOutputStream.flush();
    }

    @Override
    public int getType() {
        return Constants.MOUSE_CLICK_EVENT;
    }
}
