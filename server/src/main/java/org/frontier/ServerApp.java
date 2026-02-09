package org.frontier;

import lombok.extern.log4j.Log4j2;

import java.awt.AWTException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Log4j2
public class ServerApp {
    public static void main(String[] args)
            throws IOException, AWTException, InterruptedException, NoSuchAlgorithmException {
        new ServerRunner().run(args);
    }
}