package org.frontier.control;

import java.io.IOException;

public interface Command {
    void execute() throws IOException;
}
