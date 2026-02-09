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
class MouseScrollCommandTest {

    @Mock
    private DataInputStream din;

    @Mock
    private RobotService robotService;

    @InjectMocks
    private MouseScrollCommand mouseScrollCommand;

    @Test
    void testExecute() throws IOException {
        int scrollAmount = 5;

        when(din.readInt()).thenReturn(scrollAmount);

        mouseScrollCommand.execute();

        verify(din).readInt();
        verify(robotService).mouseWheel(scrollAmount);
    }
}
