package org.frontier.control;

import lombok.extern.log4j.Log4j2;
import org.frontier.utils.Constants;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

@Log4j2
public class KeyStrokeHandler extends KeyAdapter implements SocketHandler<KeyEvent> {
    private final DataOutputStream dataOutputStream;

    public KeyStrokeHandler(Socket socket) throws IOException {
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            log.info("keyPressed = {}", e.getKeyCode());
            this.handle(e);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void handle(KeyEvent e) throws IOException {
        dataOutputStream.writeInt(e.getKeyCode());
        dataOutputStream.flush();
    }

    @Override
    public int getType() {
        return Constants.KEY_STROKE_EVENT;
    }
}
