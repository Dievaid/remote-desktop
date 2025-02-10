package org.frontier;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.awt.image.BufferedImage;

@Log4j2
public class BufferedCanvas extends Canvas {
    private final Frame frame;

    @Setter
    private BufferedImage image;

    public BufferedCanvas(Frame frame) {
        super();
        this.frame = frame;
    }

    @Override
    public void paint(Graphics g) {
        if (image == null) {
            return;
        }

        super.paint(g);

        if (g instanceof Graphics2D g2d) {
            g2d.drawImage(image, 0, 0, this);
        } else {
            log.warn("Can't draw image because g is of type {}", g.getClass());
        }
    }
}
