package com.foxgame.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Gema coleccionable que suma puntos al jugador.
 */
public class Coin extends GameObject {

    private boolean collected = false;
    private double bobTimer;
    private final double baseY;

    public Coin(double x, double y) {
        super(x, y, 20, 20);
        this.baseY = y;
        this.bobTimer = Math.random() * Math.PI * 2;
    }

    public void update() {
        // Pequeña animacion de flotacion arriba/abajo
        bobTimer += 0.12;
        y = baseY + Math.sin(bobTimer) * 4;
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
    }

    public void draw(Graphics2D g, int cameraOffsetX) {
        if (collected) {
            return;
        }
        int drawX = (int) x - cameraOffsetX;
        int drawY = (int) y;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(255, 215, 0));
        g.fillOval(drawX, drawY, width, height);
        g.setColor(new Color(255, 245, 180));
        g.fillOval(drawX + 5, drawY + 4, 7, 7);
        g.setColor(new Color(180, 140, 0));
        g.drawOval(drawX, drawY, width, height);
    }
}
