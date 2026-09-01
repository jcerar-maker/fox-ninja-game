package com.foxgame.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Enemigo tipo "slime" que patrulla entre dos limites horizontales.
 * El jugador lo derrota saltando encima; si lo toca de lado, pierde vida.
 */
public class Enemy extends GameObject {

    private final double leftLimit;
    private final double rightLimit;
    private double speed = 1.6;
    private boolean alive = true;
    private double squashTimer = 0;

    public Enemy(double x, double y, double leftLimit, double rightLimit) {
        super(x, y, 30, 24);
        this.leftLimit = leftLimit;
        this.rightLimit = rightLimit;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        alive = false;
        squashTimer = 15;
    }

    public boolean isFinishedDying() {
        return !alive && squashTimer <= 0;
    }

    public void update() {
        if (!alive) {
            squashTimer--;
            return;
        }
        x += speed;
        if (x <= leftLimit) {
            x = leftLimit;
            speed = Math.abs(speed);
        } else if (x + width >= rightLimit) {
            x = rightLimit - width;
            speed = -Math.abs(speed);
        }
    }

    public void draw(Graphics2D g, int cameraOffsetX) {
        if (!alive && squashTimer <= 0) {
            return;
        }
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int drawX = (int) x - cameraOffsetX;
        int drawY = (int) y;
        int h = alive ? height : Math.max(6, (int) (height * (squashTimer / 15.0)));
        int adjY = drawY + (height - h);

        g.setColor(new Color(190, 60, 90));
        g.fillRoundRect(drawX, adjY, width, h, 12, 12);

        if (alive) {
            g.setColor(Color.WHITE);
            g.fillOval(drawX + 5, adjY + 5, 7, 7);
            g.fillOval(drawX + width - 12, adjY + 5, 7, 7);
            g.setColor(Color.BLACK);
            g.fillOval(drawX + 7, adjY + 7, 3, 3);
            g.fillOval(drawX + width - 10, adjY + 7, 3, 3);
        }
    }
}
