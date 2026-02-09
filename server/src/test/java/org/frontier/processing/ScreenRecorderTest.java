package org.frontier.processing;

import org.frontier.crypto.Encryptor;
import org.frontier.service.RobotService;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ScreenRecorderTest {

    @Test
    void testStartRecordingSendsData() throws IOException {
        Rectangle frame = new Rectangle(100, 100);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        StubRobotService robotService = new StubRobotService();
        StubEncryptor encryptor = new StubEncryptor();

        AtomicBoolean keepRunning = new AtomicBoolean(true);
        ScreenRecorder screenRecorder = new ScreenRecorder(robotService, frame, encryptor, () -> {
            boolean val = keepRunning.get();
            keepRunning.set(false);
            return val;
        }, outputStream);

        screenRecorder.startRecording();

        assertTrue(robotService.captureCalled, "createScreenCapture should be called");
        assertTrue(encryptor.encryptCalled, "encrypt should be called");
        assertTrue(outputStream.size() > 0, "Output stream should differ from 0");
    }

    static class StubRobotService implements RobotService {
        boolean captureCalled = false;

        @Override
        public BufferedImage createScreenCapture(Rectangle screenRect) {
            captureCalled = true;
            return new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        }

        @Override
        public void mouseMove(int x, int y) {
        }

        @Override
        public void mousePress(int buttons) {
        }

        @Override
        public void mouseRelease(int buttons) {
        }

        @Override
        public void mouseWheel(int wheelAmt) {
        }

        @Override
        public void keyPress(int keycode) {
        }

        @Override
        public void keyRelease(int keycode) {
        }
    }

    static class StubEncryptor implements Encryptor {
        boolean encryptCalled = false;

        @Override
        public byte[] encrypt(byte[] plaintext) {
            encryptCalled = true;
            return plaintext;
        }

        @Override
        public byte[] decrypt(byte[] ciphertext) {
            return ciphertext;
        }
    }
}
