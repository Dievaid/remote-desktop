package org.frontier.processing;

import org.frontier.control.*;
import org.frontier.service.RobotService;
import org.frontier.utils.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class CommandFactory {
    public Command get(int command, DataInputStream dataInputStream, RobotService robotService)
            throws IOException, IllegalArgumentException {
        return switch (command) {
            case Constants.KEY_STROKE_EVENT -> new KeyStrokeCommand(dataInputStream, robotService);
            case Constants.MOUSE_MOVE_EVENT -> new MouseMoveCommand(dataInputStream, robotService);
            case Constants.MOUSE_CLICK_EVENT -> new MouseClickCommand(dataInputStream, robotService);
            case Constants.MOUSE_SCROLL_EVENT -> new MouseScrollCommand(dataInputStream, robotService);
            default -> throw new IllegalArgumentException("Unknown command: " + command);
        };
    }
}
