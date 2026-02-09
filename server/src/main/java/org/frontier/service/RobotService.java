package org.frontier.service;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public interface RobotService {
    BufferedImage createScreenCapture(Rectangle screenRect);

    void mouseMove(int x, int y);

    void mousePress(int buttons);

    void mouseRelease(int buttons);

    void mouseWheel(int wheelAmt);

    void keyPress(int keycode);

    void keyRelease(int keycode);
}
