package org.frontier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.frontier.utils.Constants;

import javax.swing.*;
import java.awt.*;

public class ClientApp {

    private static final Logger log = LogManager.getLogger(ClientApp.class);

    public static void main(String[] args) {
        try {
            JFrame frame = new JFrame(Constants.APP_NAME);
            JLabel imageLabel = new JLabel();

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            frame.add(imageLabel);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(
                    Double.valueOf(screenSize.getWidth()).intValue(),
                    Double.valueOf(screenSize.getHeight()).intValue());

            ClientRunner runner = new ClientRunner();
            runner.run(args, new ClientRunner.JFrameUIContext(frame, imageLabel));
        } catch (Exception e) {
            log.error(e);
        }
    }
}