package org.frontier.graphic;

import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.util.Optional;

@Log4j2
public final class ResolutionController {
    private static final GraphicsDevice graphicsDevice = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice();

    public static void resizeToClosestHeight(int height) {
        Optional<DisplayMode> modeToSet = Optional.empty();

        for (DisplayMode displayMode : graphicsDevice.getDisplayModes()) {
            if (height < displayMode.getHeight()) {
                break;
            }
            modeToSet = Optional.of(displayMode);
        }

        try {
            graphicsDevice.setDisplayMode(
                    modeToSet.orElse(graphicsDevice.getDisplayMode())
            );
        } catch (Exception e) {
            DisplayMode mode = modeToSet.orElse(graphicsDevice.getDisplayMode());
            log.error("Failed to set display mode to {} - {}", mode.getWidth(), mode.getHeight(), e);
        }
    }
}
