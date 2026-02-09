package org.frontier.control;

import org.frontier.service.RobotService;

import java.awt.event.InputEvent;
import java.io.DataInputStream;
import java.io.IOException;

public class MouseClickCommand implements Command {
    private final DataInputStream din;
    private final RobotService robotService;

    public MouseClickCommand(DataInputStream din, RobotService robotService) {
        this.din = din;
        this.robotService = robotService;
    }

    @Override
    public void execute() throws IOException {
        int button = din.readInt();
        int x = din.readInt();
        int y = din.readInt();

        robotService.mouseMove(x, y);
        robotService.mousePress(InputEvent.getMaskForButton(button));
        robotService.mouseRelease(InputEvent.getMaskForButton(button));
    }
}
