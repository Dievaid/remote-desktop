package org.frontier.control;

import org.frontier.service.RobotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeyStrokeCommandTest {

    @Mock
    private DataInputStream din;

    @Mock
    private RobotService robotService;

    @InjectMocks
    private KeyStrokeCommand keyStrokeCommand;

    @Test
    void testExecuteSimpleKey() throws IOException {
        int keyCode = KeyEvent.VK_A;

        when(din.readInt()).thenReturn(keyCode);

        keyStrokeCommand.execute();

        verify(din).readInt();
        verify(robotService).keyPress(keyCode);
        verify(robotService).keyRelease(keyCode);
    }

    @Test
    void testExecuteCombinationKey() throws IOException {
        int shiftKey = KeyEvent.VK_SHIFT;
        int aKey = KeyEvent.VK_A;
        int releaseKey = KeyEvent.KEY_RELEASED;

        // Sequence: SHIFT (combo start) -> A -> RELEASED
        when(din.readInt()).thenReturn(shiftKey, aKey, releaseKey);

        keyStrokeCommand.execute();

        verify(din, times(3)).readInt();
        verify(robotService).keyPress(shiftKey);

        // Loop execution
        verify(robotService).keyPress(aKey);
        verify(robotService).keyRelease(aKey);

        verify(robotService).keyRelease(shiftKey);
    }
}
