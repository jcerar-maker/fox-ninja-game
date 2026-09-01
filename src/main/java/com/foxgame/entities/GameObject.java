package com.foxgame.entities;

import java.awt.Rectangle;

/**
 * Clase base para cualquier objeto del mundo del juego (jugador, enemigos,
 * plataformas, monedas...). Guarda posicion y tamaño, y ofrece un helper
 * para obtener el rectangulo de colision.
 */
public abstract class GameObject {

    protected double x;
    protected double y;
    protected int width;
    protected int height;

    public GameObject(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
