package com.foxgame.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

/**
 * El personaje principal: Kiro, un zorro ninja.
 * Maneja movimiento, gravedad y salto. La colision contra el mundo
 * se resuelve desde GamePanel para mantener esta clase simple.
 */
public class Player extends GameObject {

    public static final double MOVE_SPEED = 4.2;
    public static final double JUMP_STRENGTH = -13.5;
    public static final double GRAVITY = 0.65;
    public static final double MAX_FALL_SPEED = 14;

    private double velocityX;
    private double velocityY;
    private boolean onGround;
    private boolean facingRight = true;
    private int animTimer = 0;
    private boolean moving = false;

    public Player(double x, double y) {
        super(x, y, 34, 42);
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityX(double vx) {
        this.velocityX = vx;
    }

    public void setVelocityY(double vy) {
        this.velocityY = vy;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void moveLeft() {
        velocityX = -MOVE_SPEED;
        facingRight = false;
        moving = true;
    }

    public void moveRight() {
        velocityX = MOVE_SPEED;
        facingRight = true;
        moving = true;
    }

    public void stopHorizontal() {
        velocityX = 0;
        moving = false;
    }

    public void jump() {
        if (onGround) {
            velocityY = JUMP_STRENGTH;
            onGround = false;
        }
    }

    public void applyGravity() {
        velocityY += GRAVITY;
        if (velocityY > MAX_FALL_SPEED) {
            velocityY = MAX_FALL_SPEED;
        }
    }

    /**
     * Solo actualiza la animacion. El movimiento real de x/y se resuelve
     * en GamePanel eje por eje, para poder detectar colisiones con precision.
     */
    public void updateAnimation() {
        if (moving) {
            animTimer++;
        }
    }

    public void draw(Graphics2D g, int cameraOffsetX) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int drawX = (int) x - cameraOffsetX;
        int drawY = (int) y;

        int bob = moving && onGround ? (int) (Math.sin(animTimer * 0.4) * 2) : 0;
        drawY += bob;

        Color furOrange = new Color(230, 126, 34);
        Color furDark = new Color(180, 90, 20);
        Color bandColor = new Color(40, 40, 45);

        int cx = drawX + width / 2;

        // --- Cola ---
        g.setColor(furOrange);
        int tailX = facingRight ? drawX - 10 : drawX + width - 6;
        g.fillOval(tailX, drawY + 14, 18, 12);
        g.setColor(Color.WHITE);
        g.fillOval(tailX + (facingRight ? 8 : 2), drawY + 16, 8, 8);

        // --- Cuerpo ---
        g.setColor(furOrange);
        g.fillRoundRect(drawX, drawY + 14, width, height - 14, 12, 12);

        // Pecho blanco
        g.setColor(Color.WHITE);
        g.fillRoundRect(drawX + width / 2 - 7, drawY + 22, 14, height - 24, 8, 8);

        // --- Cabeza ---
        g.setColor(furOrange);
        g.fillOval(drawX + 2, drawY, width - 4, 26);

        // Orejas
        int earOffset = facingRight ? 0 : 0;
        int[] earXsL = {drawX + 4, drawX + 10, drawX - 1};
        int[] earYsL = {drawY + 2, drawY + 2, drawY - 12};
        g.fillPolygon(earXsL, earYsL, 3);
        int[] earXsR = {drawX + width - 4, drawX + width - 10, drawX + width + 1};
        int[] earYsR = {drawY + 2, drawY + 2, drawY - 12};
        g.fillPolygon(earXsR, earYsR, 3);
        g.setColor(furDark);
        int[] innerL = {drawX + 5, drawX + 9, drawX + 1};
        int[] innerLy = {drawY + 1, drawY + 1, drawY - 7};
        g.fillPolygon(innerL, innerLy, 3);
        int[] innerR = {drawX + width - 5, drawX + width - 9, drawX + width - 1};
        int[] innerRy = {drawY + 1, drawY + 1, drawY - 7};
        g.fillPolygon(innerR, innerRy, 3);

        // Banda ninja
        g.setColor(bandColor);
        g.fillRect(drawX, drawY + 6, width, 6);
        int flagX = facingRight ? drawX - 6 : drawX + width;
        g.fillPolygon(new int[]{flagX, flagX + (facingRight ? -8 : 8), flagX},
                new int[]{drawY + 5, drawY + 9, drawY + 13}, 3);
        g.setColor(new Color(200, 30, 30));
        g.fillOval(cx - 3, drawY + 5, 6, 6);

        // Hocico + ojos
        g.setColor(Color.WHITE);
        int muzzleX = facingRight ? drawX + width - 12 : drawX;
        g.fillOval(muzzleX, drawY + 12, 14, 10);
        g.setColor(Color.BLACK);
        g.fillOval(muzzleX + (facingRight ? 8 : 2), drawY + 15, 3, 3);

        int eyeX = facingRight ? drawX + width - 14 : drawX + 6;
        g.setColor(Color.BLACK);
        g.fillOval(eyeX, drawY + 12, 4, 4);

        // --- Patas ---
        g.setColor(furDark);
        int legOffset = moving && onGround ? (int) (Math.sin(animTimer * 0.6) * 4) : 0;
        g.fillRoundRect(drawX + 4, drawY + height - 8 + legOffset / 2, 8, 10, 4, 4);
        g.fillRoundRect(drawX + width - 12, drawY + height - 8 - legOffset / 2, 8, 10, 4, 4);
    }

    /**
     * Circulo de "pisada" usado para saber si el jugador cae desde arriba
     * sobre un enemigo (para poder derrotarlo saltando encima).
     */
    public Ellipse2D getFeetProbe() {
        return new Ellipse2D.Double(x + 6, y + height - 4, width - 12, 8);
    }
}
