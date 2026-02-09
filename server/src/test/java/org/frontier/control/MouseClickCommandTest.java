package org.frontier.control;

import org.frontier.service.RobotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.event.InputEvent;
import java.io.DataInputStream;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MouseClickCommandTest {

    @Mock
    private DataInputStream din;

    @Mock
    private RobotService robotService;

    @InjectMocks
    private MouseClickCommand mouseClickCommand;

    @Test
    void testExecute() throws IOException {
        int button = 1;
        int x = 150;
        int y = 250;
        int mask = InputEvent.getMaskForButton(button);

        when(din.readInt()).thenReturn(button, x, y);

        mouseClickCommand.execute();

        verify(din, times(3)).readInt();
        verify(robotService).mouseMove(x, y);
        verify(robotService).mousePress(mask);
        verify(robotService).mouseRelease(mask);
    }
}
