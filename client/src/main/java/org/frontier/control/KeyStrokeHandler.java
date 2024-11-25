package org.frontier.control;

import lombok.extern.log4j.Log4j2;
import org.frontier.utils.Constants;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

@Log4j2
public class KeyStrokeHandler extends KeyAdapter implements SocketHandler<KeyEvent> {
    private final DataOutputStream dataOutputStream;

    private static final List<Integer> combinationKeyList = List.of(
            KeyEvent.VK_SHIFT, KeyEvent.VK_CONTROL, KeyEvent.VK_ALT);

    public KeyStrokeHandler(Socket socket) throws IOException {
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (combinationKeyList.contains(e.getKeyCode())) {
            try {
                dataOutputStream.writeInt(KeyEvent.KEY_RELEASED);
                dataOutputStream.flush();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
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
