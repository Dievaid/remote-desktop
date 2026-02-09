package org.frontier.processing;

import org.frontier.control.Command;

import org.frontier.service.RobotService;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.List;
import java.util.stream.IntStream;

import java.util.function.Function;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class CommandMonitor implements Runnable {
    private static final Logger log = LogManager.getLogger(CommandMonitor.class);
    private final List<Socket> socketList;
    private final RobotService robotService;
    private final CommandFactory commandFactory;
    private final Function<Socket, Boolean> loopCondition;

    public CommandMonitor(List<Socket> socketList, RobotService robotService, CommandFactory commandFactory) {
        this(socketList, robotService, commandFactory, Socket::isConnected);
    }

    public CommandMonitor(List<Socket> socketList, RobotService robotService, CommandFactory commandFactory,
            Function<Socket, Boolean> loopCondition) {
        this.socketList = socketList;
        this.robotService = robotService;
        this.commandFactory = commandFactory;
        this.loopCondition = loopCondition;
    }

    @Override
    public void run() {
        List<Thread> threads = IntStream.range(0, socketList.size())
                .mapToObj(idx -> new Thread(() -> this.runSocketHandler(idx)))
                .toList();

        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                log.error("Thread interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    private void runSocketHandler(int commandType) {
        Socket socket = socketList.get(commandType);
        try {
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            Command socketCommand = commandFactory.get(commandType, dataInputStream, robotService);

            while (loopCondition.apply(socket)) {
                try {
                    socketCommand.execute();
                } catch (IllegalArgumentException e) {
                    log.error(e.getMessage(), e);
                } catch (IOException e) {
                    log.info("Connection to {} was closed", socket.getRemoteSocketAddress());
                    break;
                }
            }

        } catch (IOException e) {
            log.error("Error while reading running monitor", e);
        }
    }
}
