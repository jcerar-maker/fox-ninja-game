package com.foxgame;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la aplicación.
 *
 * Controles:
 *   Flechas / A-D  -> mover izquierda / derecha
 *   Flecha arriba, W o ESPACIO -> saltar
 *   R -> reiniciar (al perder o ganar)
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameFrame::new);
    }
}
