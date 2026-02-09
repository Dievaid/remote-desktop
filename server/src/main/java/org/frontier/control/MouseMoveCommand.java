package org.frontier.control;

import org.frontier.service.RobotService;

import java.io.DataInputStream;
import java.io.IOException;

public class MouseMoveCommand implements Command {
    private final DataInputStream din;
    private final RobotService robotService;

    public MouseMoveCommand(DataInputStream din, RobotService robotService) {
        this.din = din;
        this.robotService = robotService;
    }

    @Override
    public void execute() throws IOException {
        int x = din.readInt();
        int y = din.readInt();
        robotService.mouseMove(x, y);
    }
}
