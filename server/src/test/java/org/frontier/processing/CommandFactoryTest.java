package org.frontier.processing;

import org.frontier.control.*;
import org.frontier.service.RobotService;
import org.frontier.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.DataInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CommandFactoryTest {

    @Mock
    private DataInputStream din;

    @Mock
    private RobotService robotService;

    @Test
    void testGetCommands() throws IOException {
        CommandFactory factory = new CommandFactory();

        assertInstanceOf(MouseMoveCommand.class, factory.get(Constants.MOUSE_MOVE_EVENT, din, robotService));
        assertInstanceOf(MouseScrollCommand.class, factory.get(Constants.MOUSE_SCROLL_EVENT, din, robotService));
        assertInstanceOf(MouseClickCommand.class, factory.get(Constants.MOUSE_CLICK_EVENT, din, robotService));
        assertInstanceOf(KeyStrokeCommand.class, factory.get(Constants.KEY_STROKE_EVENT, din, robotService));
    }

    @Test
    void testUnknownCommand() {
        CommandFactory factory = new CommandFactory();
        assertThrows(IllegalArgumentException.class, () -> factory.get(-1, din, robotService));
    }
}
