package org.frontier.control;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.DataInputStream;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class MouseClickCommand implements Command {
    private final DataInputStream din;
    private final Robot robot;

    @Override
    public void execute() {
        try {
            int button = din.readInt();
            int x = din.readInt();
            int y = din.readInt();

            robot.mouseMove(x, y);
            robot.mousePress(InputEvent.getMaskForButton(button));
            robot.mouseRelease(InputEvent.getMaskForButton(button));
        } catch (IOException e) {
            log.error("Error while trying to read mouse click command", e);
        }
    }
}
