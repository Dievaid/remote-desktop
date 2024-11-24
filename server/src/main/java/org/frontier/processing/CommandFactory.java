package org.frontier.processing;

import org.frontier.control.Command;
import org.frontier.control.KeyStrokeCommand;
import org.frontier.control.MouseClickCommand;
import org.frontier.control.MouseMoveCommand;
import org.frontier.utils.Constants;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;

public final class CommandFactory {
    public static Command get(int command, DataInputStream dataInputStream, Robot robot) throws IOException {
        return switch (command) {
            case Constants.KEY_STROKE_EVENT -> new KeyStrokeCommand(dataInputStream, robot);
            case Constants.MOUSE_MOVE_EVENT -> new MouseMoveCommand(dataInputStream, robot);
            case Constants.MOUSE_CLICK_EVENT -> new MouseClickCommand(dataInputStream, robot);
            default -> throw new IllegalArgumentException("Unknown command: " + command);
        };
    }
}