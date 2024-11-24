package org.frontier.processing;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

@Log4j2
@RequiredArgsConstructor
public class CommandMonitor implements Runnable {
    private final Socket socket;
    private final Robot robot;

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            while (socket.isConnected()) {
                int commandType = dataInputStream.readInt();
                log.info("Command detected: {}", commandType);
                try {
                    CommandFactory.get(commandType, dataInputStream, robot).execute();
                } catch (IOException | IllegalArgumentException e) {
                    log.error(e.getMessage(), e);
                }
            }

        } catch (IOException e) {
            log.error("Error while reading running monitor", e);
        }
    }
}
