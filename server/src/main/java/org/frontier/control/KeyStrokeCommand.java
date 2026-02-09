package org.frontier.control;

import org.frontier.service.RobotService;

import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public class KeyStrokeCommand implements Command {
    private final DataInputStream dis;
    private final RobotService robotService;

    public KeyStrokeCommand(DataInputStream dis, RobotService robotService) {
        this.dis = dis;
        this.robotService = robotService;
    }

    private static final java.util.List<Integer> combinationKeyCodes = List.of(
            KeyEvent.VK_SHIFT, KeyEvent.VK_CONTROL, KeyEvent.VK_ALT);

    @Override
    public void execute() throws IOException {
        int keyCode = dis.readInt();
        robotService.keyPress(keyCode);

        if (combinationKeyCodes.contains(keyCode)) {
            int combinationKeyCode = dis.readInt();
            while (combinationKeyCode != KeyEvent.KEY_RELEASED) {
                robotService.keyPress(combinationKeyCode);
                robotService.keyRelease(combinationKeyCode);
                combinationKeyCode = dis.readInt();
            }
        }

        robotService.keyRelease(keyCode);
    }
}
