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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class MouseMoveHandlerTest {

    @Test
    void testMouseMoved() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MouseMoveHandler handler = new MouseMoveHandler(baos);

        Component source = new Component() {
        };
        MouseEvent event = new MouseEvent(source, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, 100, 200, 0,
                false);

        handler.mouseMoved(event);

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        DataOutputStream das = new DataOutputStream(expected);
        das.writeInt(100);
        das.writeInt(200);
        das.flush();

        assertArrayEquals(expected.toByteArray(), baos.toByteArray());
    }

    @Test
    void testMouseMovedException() throws IOException {
        OutputStream errorStream = mock(OutputStream.class);
        doThrow(new IOException("test")).when(errorStream).write(anyInt());
        try {
            doThrow(new IOException("test")).when(errorStream).write(any(byte[].class), anyInt(), anyInt());
        } catch (Exception e) {
        }

        MouseMoveHandler handler = new MouseMoveHandler(errorStream);
        Component source = new Component() {
        };
        MouseEvent event = new MouseEvent(source, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, 100, 200, 0,
                false);

        assertThrows(RuntimeException.class, () -> handler.mouseMoved(event));
    }

    @Test
    void testGetType() {
        MouseMoveHandler handler = new MouseMoveHandler(new ByteArrayOutputStream());
        assertEquals(Constants.MOUSE_MOVE_EVENT, handler.getType());
    }
}
