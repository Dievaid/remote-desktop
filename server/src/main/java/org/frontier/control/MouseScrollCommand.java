package org.frontier.control;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class MouseScrollCommand implements Command {
    private final DataInputStream din;
    private final Robot robot;


    @Override
    public void execute() {
        try {
            int scrollAmount = din.readInt();
            robot.mouseWheel(scrollAmount);
        } catch (IOException e) {
            log.error("Could not read scroll amount", e);
        }
    }
}
