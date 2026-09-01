package com.foxgame;

import javax.swing.JFrame;

/**
 * Ventana principal que aloja el GamePanel.
 */
public class GameFrame extends JFrame {

    public GameFrame() {
        GamePanel gamePanel = new GamePanel();

        setTitle("Kiro el Zorro Ninja - Aventura de Plataformas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(gamePanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        gamePanel.requestFocusInWindow();
    }
}
