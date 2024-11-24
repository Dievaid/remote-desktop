package org.frontier.control;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class KeyStrokeCommand implements Command {
    private final DataInputStream dis;
    private final Robot robot;

    @Override
    public void execute() {
        try {
            int keyCode = dis.readInt();
            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
        } catch (IOException e) {
            log.error("Error reading key stroke command", e);
        }
    }
}
