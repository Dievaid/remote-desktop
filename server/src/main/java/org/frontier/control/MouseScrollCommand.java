package org.frontier.control;

import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;

@RequiredArgsConstructor
public class MouseScrollCommand implements Command {
    private final DataInputStream din;
    private final Robot robot;

    @Override
    public void execute() throws IOException {
        int scrollAmount = din.readInt();
        robot.mouseWheel(scrollAmount);
    }
}
