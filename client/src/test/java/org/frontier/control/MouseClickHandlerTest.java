package org.frontier.control;

import org.frontier.utils.Constants;
import org.junit.jupiter.api.Test;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;

class MouseClickHandlerTest {

    @Test
    void testMousePressed() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MouseClickHandler handler = new MouseClickHandler(baos);

        Component source = new Component() {
        };
        int button = MouseEvent.BUTTON1;
        int x = 50, y = 60;
        MouseEvent event = new MouseEvent(source, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, x, y, 1,
                false, button);

        handler.mousePressed(event);

        // Expected format: button (int), x (int), y (int)
        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        DataOutputStream das = new DataOutputStream(expected);
        das.writeInt(button);
        das.writeInt(x);
        das.writeInt(y);
        das.flush();

        assertArrayEquals(expected.toByteArray(), baos.toByteArray());
    }

    @Test
    void testMousePressedException() throws IOException {
        OutputStream errorStream = mock(OutputStream.class);
        doThrow(new IOException("test")).when(errorStream).write(anyInt());
        try {
            doThrow(new IOException("test")).when(errorStream).write(any(byte[].class), anyInt(), anyInt());
        } catch (Exception e) {
        }

        MouseClickHandler handler = new MouseClickHandler(errorStream);
        Component source = new Component() {
        };
        MouseEvent event = new MouseEvent(source, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 0, 0, 1,
                false, MouseEvent.BUTTON1);

        assertThrows(RuntimeException.class, () -> handler.mousePressed(event));
    }

    @Test
    void testGetType() {
        MouseClickHandler handler = new MouseClickHandler(new ByteArrayOutputStream());
        assertEquals(Constants.MOUSE_CLICK_EVENT, handler.getType());
    }
}
