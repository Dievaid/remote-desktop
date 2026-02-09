package org.frontier.control;

import org.frontier.service.RobotService;

import java.io.DataInputStream;
import java.io.IOException;

public class MouseScrollCommand implements Command {
    private final DataInputStream din;
    private final RobotService robotService;

    public MouseScrollCommand(DataInputStream din, RobotService robotService) {
        this.din = din;
        this.robotService = robotService;
    }

    @Override
    public void execute() throws IOException {
        int scrollAmount = din.readInt();
        robotService.mouseWheel(scrollAmount);
    }
}
