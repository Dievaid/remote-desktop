package org.frontier.control;

import org.frontier.utils.Constants;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantLock;

public class MouseMoveHandler extends MouseMotionAdapter implements SocketHandler<MouseEvent> {
    private final DataOutputStream dataOutputStream;
    private final ReentrantLock lock;

    public MouseMoveHandler(OutputStream outputStream, ReentrantLock lock) {
        this.dataOutputStream = new DataOutputStream(outputStream);
        this.lock = lock;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
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
        dataOutputStream.writeLong(this.getType());
        dataOutputStream.writeLong(e.getX());
        dataOutputStream.writeLong(e.getY());
        dataOutputStream.flush();
    }

    @Override
    public int getType() {
        return Constants.MOUSE_MOVE_EVENT;
    }
}
