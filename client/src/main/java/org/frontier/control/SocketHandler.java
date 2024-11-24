package org.frontier.control;

import java.awt.*;
import java.io.IOException;

public interface SocketHandler<T extends AWTEvent> {
    void handle(T e) throws IOException;
    int getType();
}
