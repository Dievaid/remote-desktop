package org.frontier.graphic;

import java.awt.*;
import java.util.Optional;

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

        graphicsDevice.setDisplayMode(
                modeToSet.orElse(graphicsDevice.getDisplayMode())
        );
    }
}
