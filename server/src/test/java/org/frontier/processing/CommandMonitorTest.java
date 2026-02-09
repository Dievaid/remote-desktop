package org.frontier.processing;

import org.frontier.control.Command;
import org.frontier.service.RobotService;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandMonitorTest {

    @Test
    void testCommandMonitorExecutesCommand() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        Socket socket = new StubSocket(inputStream);
        List<Socket> sockets = Collections.singletonList(socket);

        StubRobotService robotService = new StubRobotService();
        StubCommand command = new StubCommand();
        StubCommandFactory commandFactory = new StubCommandFactory(command);

        AtomicBoolean keepRunning = new AtomicBoolean(true);
        CommandMonitor monitor = new CommandMonitor(sockets, robotService, commandFactory, (s) -> {
            boolean val = keepRunning.get();
            keepRunning.set(false);
            return val;
        });

        monitor.run();

        assertTrue(commandFactory.getCalled, "CommandFactory.get should be called");
        assertTrue(command.executed, "Command.execute should be called");
    }

    static class StubSocket extends Socket {
        private final InputStream inputStream;

        public StubSocket(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public InputStream getInputStream() {
            return inputStream;
        }

        @Override
        public boolean isConnected() {
            return true;
        }
    }

    static class StubCommandFactory extends CommandFactory {
        boolean getCalled = false;
        Command commandToReturn;

        public StubCommandFactory(Command command) {
            this.commandToReturn = command;
        }

        @Override
        public Command get(int command, DataInputStream dataInputStream, RobotService robotService) {
            getCalled = true;
            return commandToReturn;
        }
    }

    static class StubCommand implements Command {
        boolean executed = false;

        @Override
        public void execute() throws IOException {
            executed = true;
        }
    }

    static class StubRobotService implements RobotService {
        @Override
        public java.awt.image.BufferedImage createScreenCapture(Rectangle screenRect) {
            return null;
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
}
