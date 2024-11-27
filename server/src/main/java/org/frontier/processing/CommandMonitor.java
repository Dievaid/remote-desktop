package org.frontier.processing;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.frontier.control.Command;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.List;
import java.util.stream.IntStream;

@Log4j2
@RequiredArgsConstructor
public class CommandMonitor implements Runnable {
    private final List<Socket> socketList;
    private final Robot robot;

    @SneakyThrows
    @Override
    public void run() {
        List<Thread> threads = IntStream.range(0, socketList.size())
                .mapToObj(idx -> new Thread(() -> this.runSocketHandler(idx)))
                .toList();

        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            thread.join();
        }
    }

    private void runSocketHandler(int commandType) {
        Socket socket = socketList.get(commandType);
        try {
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            Command socketCommand = CommandFactory.get(commandType, dataInputStream, robot);

            while (socket.isConnected()) {
                try {
                    socketCommand.execute();
                } catch (IllegalArgumentException e ) {
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
