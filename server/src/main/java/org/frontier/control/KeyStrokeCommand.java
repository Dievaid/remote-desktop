package org.frontier.control;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class KeyStrokeCommand implements Command {
    private final DataInputStream dis;
    private final Robot robot;

    private static final java.util.List<Integer> combinationKeyCodes = List.of(
            KeyEvent.VK_SHIFT, KeyEvent.VK_CONTROL, KeyEvent.VK_ALT);

    @Override
    public void execute() {
        try {
            int keyCode = dis.readInt();
            robot.keyPress(keyCode);

            if (combinationKeyCodes.contains(keyCode)) {
                int combinationKeyCode = dis.readInt();
                while (combinationKeyCode != KeyEvent.KEY_RELEASED) {
                    robot.keyPress(combinationKeyCode);
                    robot.keyRelease(combinationKeyCode);
                    combinationKeyCode = dis.readInt();
                }
            }

            robot.keyRelease(keyCode);
        } catch (IOException e) {
            log.error("Error reading key stroke command", e);
        }
    }
}
