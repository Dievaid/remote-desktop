package org.frontier.control;

import org.frontier.utils.Constants;
import org.junit.jupiter.api.Test;

import java.awt.Component;
import java.awt.event.KeyEvent;
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

class KeyStrokeHandlerTest {

    @Test
    void testKeyPressed() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        KeyStrokeHandler handler = new KeyStrokeHandler(baos);

        Component source = new Component() {
        };
        int keyCode = KeyEvent.VK_A;
        KeyEvent event = new KeyEvent(source, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, keyCode, 'a');

        handler.keyPressed(event);

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        DataOutputStream das = new DataOutputStream(expected);
        das.writeInt(keyCode);
        das.flush();

        assertArrayEquals(expected.toByteArray(), baos.toByteArray());
    }

    @Test
    void testKeyPressedException() throws IOException {
        OutputStream errorStream = mock(OutputStream.class);
        doThrow(new IOException("test")).when(errorStream).write(anyInt());
        try {
            doThrow(new IOException("test")).when(errorStream).write(any(byte[].class), anyInt(), anyInt());
        } catch (Exception e) {
        }

        KeyStrokeHandler handler = new KeyStrokeHandler(errorStream);
        Component source = new Component() {
        };
        KeyEvent event = new KeyEvent(source, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'a');

        assertThrows(RuntimeException.class, () -> handler.keyPressed(event));
    }

    @Test
    void testKeyReleasedNormal() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        KeyStrokeHandler handler = new KeyStrokeHandler(baos);

        Component source = new Component() {
        };
        int keyCode = KeyEvent.VK_A;
        KeyEvent event = new KeyEvent(source, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, keyCode, 'a');

        handler.keyReleased(event);

        assertEquals(0, baos.size());
    }

    @Test
    void testKeyReleasedCombo() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        KeyStrokeHandler handler = new KeyStrokeHandler(baos);

        Component source = new Component() {
        };
        int keyCode = KeyEvent.VK_SHIFT;
        KeyEvent event = new KeyEvent(source, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, keyCode,
                KeyEvent.CHAR_UNDEFINED);

        handler.keyReleased(event);

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        DataOutputStream das = new DataOutputStream(expected);
        das.writeInt(KeyEvent.KEY_RELEASED);
        das.flush();

        assertArrayEquals(expected.toByteArray(), baos.toByteArray());
    }

    @Test
    void testKeyReleasedComboException() throws IOException {
        OutputStream errorStream = mock(OutputStream.class);
        doThrow(new IOException("test")).when(errorStream).write(anyInt());
        try {
            doThrow(new IOException("test")).when(errorStream).write(any(byte[].class), anyInt(), anyInt());
        } catch (Exception e) {
        }

        KeyStrokeHandler handler = new KeyStrokeHandler(errorStream);
        Component source = new Component() {
        };
        int keyCode = KeyEvent.VK_SHIFT;
        KeyEvent event = new KeyEvent(source, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, keyCode,
                KeyEvent.CHAR_UNDEFINED);

        assertThrows(RuntimeException.class, () -> handler.keyReleased(event));
    }

    @Test
    void testGetType() {
        KeyStrokeHandler handler = new KeyStrokeHandler(new ByteArrayOutputStream());
        assertEquals(Constants.KEY_STROKE_EVENT, handler.getType());
    }
}
