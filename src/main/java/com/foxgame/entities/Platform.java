package com.foxgame.entities;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Plataforma solida sobre la que el jugador y los enemigos pueden caminar.
 */
public class Platform extends GameObject {

    private static final Color TOP_COLOR = new Color(96, 170, 90);
    private static final Color BODY_COLOR = new Color(122, 84, 58);

    public Platform(double x, double y, int width, int height) {
        super(x, y, width, height);
    }

    public void draw(Graphics2D g, int cameraOffsetX) {
        int drawX = (int) x - cameraOffsetX;

        // Cuerpo de tierra
        g.setColor(BODY_COLOR);
        g.fillRect(drawX, (int) y, width, height);

        // Capa de "cesped" arriba
        g.setColor(TOP_COLOR);
        g.fillRect(drawX, (int) y, width, Math.min(10, height));

        g.setColor(new Color(0, 0, 0, 40));
        g.drawRect(drawX, (int) y, width, height);
    }
}
