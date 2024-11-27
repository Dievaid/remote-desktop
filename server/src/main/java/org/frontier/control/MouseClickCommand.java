package org.frontier.control;

import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.DataInputStream;
import java.io.IOException;

@RequiredArgsConstructor
public class MouseClickCommand implements Command {
    private final DataInputStream din;
    private final Robot robot;

    @Override
    public void execute() throws IOException {
        int button = din.readInt();
        int x = din.readInt();
        int y = din.readInt();

        robot.mouseMove(x, y);
        robot.mousePress(InputEvent.getMaskForButton(button));
        robot.mouseRelease(InputEvent.getMaskForButton(button));
    }
}
