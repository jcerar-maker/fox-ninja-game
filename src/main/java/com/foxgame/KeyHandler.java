package com.foxgame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Escucha el teclado y expone el estado actual de las teclas relevantes.
 * Soporta flechas y WASD, ademas de espacio para saltar y R para reiniciar.
 */
public class KeyHandler extends KeyAdapter {

    private boolean left;
    private boolean right;
    private boolean jump;
    private boolean restartPressed;

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                left = true;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                right = true;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
            case KeyEvent.VK_SPACE:
                jump = true;
                break;
            case KeyEvent.VK_R:
                restartPressed = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                left = false;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                right = false;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
            case KeyEvent.VK_SPACE:
                jump = false;
                break;
            case KeyEvent.VK_R:
                restartPressed = false;
                break;
            default:
                break;
        }
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isJump() {
        return jump;
    }

    public boolean isRestartPressed() {
        return restartPressed;
    }
}
