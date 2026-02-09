package org.frontier.control;

import org.frontier.service.RobotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.DataInputStream;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MouseMoveCommandTest {

    @Mock
    private DataInputStream din;

    @Mock
    private RobotService robotService;

    @InjectMocks
    private MouseMoveCommand mouseMoveCommand;

    @Test
    void testExecute() throws IOException {
        int x = 100;
        int y = 200;

        when(din.readInt()).thenReturn(x, y);

        mouseMoveCommand.execute();

        verify(din, times(2)).readInt();
        verify(robotService).mouseMove(x, y);
    }
}
