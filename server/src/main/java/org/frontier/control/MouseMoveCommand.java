package org.frontier.control;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class MouseMoveCommand implements Command {
    private final DataInputStream din;
    private final Robot robot;

    @Override
    public void execute() {
        try {
            int x = din.readInt();
            int y = din.readInt();
            robot.mouseMove(x, y);
            log.info("Mouse moved to {}:{}", x, y);
        } catch (IOException e) {
            log.error("Error while reading mouse move command", e);
        }
    }
}
