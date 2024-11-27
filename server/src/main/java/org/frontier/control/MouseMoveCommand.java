package org.frontier.control;

import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;

@RequiredArgsConstructor
public class MouseMoveCommand implements Command {
    private final DataInputStream din;
    private final Robot robot;

    @Override
    public void execute() throws IOException {
        int x = din.readInt();
        int y = din.readInt();
        robot.mouseMove(x, y);
    }
}
