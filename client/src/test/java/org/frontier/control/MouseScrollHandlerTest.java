package org.frontier.control;

import org.frontier.utils.Constants;
import org.junit.jupiter.api.Test;

import java.awt.Component;
import java.awt.event.MouseWheelEvent;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class MouseScrollHandlerTest {

    @Test
    void testMouseWheelMoved() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MouseScrollHandler handler = new MouseScrollHandler(baos);

        Component source = new Component() {
        };
        MouseWheelEvent event = new MouseWheelEvent(source, MouseWheelEvent.MOUSE_WHEEL, System.currentTimeMillis(), 0,
                0, 0, 0, false, MouseWheelEvent.WHEEL_UNIT_SCROLL, 1, 5);

        handler.mouseWheelMoved(event);

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        DataOutputStream das = new DataOutputStream(expected);
        das.writeInt(5);
        das.flush();

        assertArrayEquals(expected.toByteArray(), baos.toByteArray());
    }

    @Test
    void testMouseWheelMovedException() throws IOException {
        OutputStream errorStream = mock(OutputStream.class);
        doThrow(new IOException("test")).when(errorStream).write(anyInt());
        try {
            doThrow(new IOException("test")).when(errorStream).write(any(byte[].class), anyInt(), anyInt());
        } catch (Exception e) {
        }

        MouseScrollHandler handler = new MouseScrollHandler(errorStream);
        Component source = new Component() {
        };
        MouseWheelEvent event = new MouseWheelEvent(source, MouseWheelEvent.MOUSE_WHEEL, System.currentTimeMillis(), 0,
                0, 0, 0, false, MouseWheelEvent.WHEEL_UNIT_SCROLL, 1, 5);

        assertThrows(RuntimeException.class, () -> handler.mouseWheelMoved(event));
    }

    @Test
    void testGetType() {
        MouseScrollHandler handler = new MouseScrollHandler(new ByteArrayOutputStream());
        assertEquals(Constants.MOUSE_SCROLL_EVENT, handler.getType());
    }
}
