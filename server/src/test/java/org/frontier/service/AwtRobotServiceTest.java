package org.frontier.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AwtRobotServiceTest {

    @Mock
    private Robot robot;

    @InjectMocks
    private AwtRobotService awtRobotService;

    @Test
    void testMouseMove() {
        int x = 10, y = 20;
        awtRobotService.mouseMove(x, y);
        verify(robot).mouseMove(x, y);
    }

    @Test
    void testMousePress() {
        int buttons = InputEvent.BUTTON1_DOWN_MASK;
        awtRobotService.mousePress(buttons);
        verify(robot).mousePress(buttons);
    }

    @Test
    void testMouseRelease() {
        int buttons = InputEvent.BUTTON1_DOWN_MASK;
        awtRobotService.mouseRelease(buttons);
        verify(robot).mouseRelease(buttons);
    }

    @Test
    void testMouseWheel() {
        int amt = 5;
        awtRobotService.mouseWheel(amt);
        verify(robot).mouseWheel(amt);
    }

    @Test
    void testKeyPress() {
        int key = 65;
        awtRobotService.keyPress(key);
        verify(robot).keyPress(key);
    }

    @Test
    void testKeyRelease() {
        int key = 65;
        awtRobotService.keyRelease(key);
        verify(robot).keyRelease(key);
    }

    @Test
    void testCreateScreenCapture() {
        Rectangle rect = new Rectangle(0, 0, 100, 100);
        BufferedImage mockImage = mock(BufferedImage.class);
        when(robot.createScreenCapture(rect)).thenReturn(mockImage);

        BufferedImage result = awtRobotService.createScreenCapture(rect);
        assertEquals(mockImage, result);
        verify(robot).createScreenCapture(rect);
    }

    @Test
    void testDefaultConstructor() {
        try {
            new AwtRobotService();
        } catch (AWTException | HeadlessException e) {
        }
    }
}
