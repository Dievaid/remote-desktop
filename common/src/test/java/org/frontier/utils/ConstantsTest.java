package org.frontier.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConstantsTest {

    @Test
    void testConstantsValues() {
        assertEquals("Client Remote Frontier", Constants.APP_NAME);
        assertEquals("png", Constants.PNG_FILE_EXTENSION);
        assertEquals(0, Constants.KEY_STROKE_EVENT);
        assertEquals(1, Constants.MOUSE_MOVE_EVENT);
        assertEquals(2, Constants.MOUSE_CLICK_EVENT);
        assertEquals(3, Constants.MOUSE_SCROLL_EVENT);
    }

    @Test
    void testConstructor() {
        Constants constants = new Constants();
        assertNotNull(constants);
    }
}
