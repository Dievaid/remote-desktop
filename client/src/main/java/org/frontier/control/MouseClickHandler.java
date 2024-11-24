package org.frontier.control;

import org.frontier.utils.Constants;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantLock;

public class MouseClickHandler extends MouseAdapter implements SocketHandler<MouseEvent> {
    private final DataOutputStream dataOutputStream;
    private final ReentrantLock lock;

    public MouseClickHandler(OutputStream outputStream, ReentrantLock lock) {
        this.dataOutputStream = new DataOutputStream(outputStream);
        this.lock = lock;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        try {
            this.lock.lock();
            this.handle(e);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        finally {
            this.lock.unlock();
        }
    }

    @Override
    public void handle(MouseEvent e) throws IOException {
        dataOutputStream.writeInt(this.getType());
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
