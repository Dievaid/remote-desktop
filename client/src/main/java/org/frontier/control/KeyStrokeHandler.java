package org.frontier.control;

import lombok.extern.log4j.Log4j2;
import org.frontier.utils.Constants;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantLock;

@Log4j2
public class KeyStrokeHandler extends KeyAdapter implements SocketHandler<KeyEvent> {
    private final DataOutputStream dataOutputStream;
    private final ReentrantLock lock;

    public KeyStrokeHandler(OutputStream outputStream, ReentrantLock lock) {
        this.dataOutputStream = new DataOutputStream(outputStream);
        this.lock = lock;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            this.lock.lock();
            log.info("keyPressed = {}", e.getKeyCode());
            this.handle(e);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        finally {
            this.lock.unlock();
        }
    }

    @Override
    public void handle(KeyEvent e) throws IOException {
        dataOutputStream.writeInt(this.getType());
        dataOutputStream.writeInt(e.getKeyCode());
        dataOutputStream.flush();
    }

    @Override
    public int getType() {
        return Constants.KEY_STROKE_EVENT;
    }
}
