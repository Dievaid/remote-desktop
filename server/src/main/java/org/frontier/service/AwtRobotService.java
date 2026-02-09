package org.frontier.service;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

public class AwtRobotService implements RobotService {
    private final Robot robot;

    public AwtRobotService() throws AWTException {
        this(new Robot());
    }

    public AwtRobotService(Robot robot) {
        this.robot = robot;
    }

    @Override
    public BufferedImage createScreenCapture(Rectangle screenRect) {
        return robot.createScreenCapture(screenRect);
    }

    @Override
    public void mouseMove(int x, int y) {
        robot.mouseMove(x, y);
    }

    @Override
    public void mousePress(int buttons) {
        robot.mousePress(buttons);
    }

    @Override
    public void mouseRelease(int buttons) {
        robot.mouseRelease(buttons);
    }

    @Override
    public void mouseWheel(int wheelAmt) {
        robot.mouseWheel(wheelAmt);
    }

    @Override
    public void keyPress(int keycode) {
        robot.keyPress(keycode);
    }

    @Override
    public void keyRelease(int keycode) {
        robot.keyRelease(keycode);
    }
}
