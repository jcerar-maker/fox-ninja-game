package com.foxgame;

import com.foxgame.entities.Coin;
import com.foxgame.entities.Enemy;
import com.foxgame.entities.Platform;
import com.foxgame.entities.Player;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel principal: contiene el bucle de juego (game loop), la logica de
 * colisiones, la camara y el renderizado de todo lo que se ve en pantalla.
 */
public class GamePanel extends JPanel implements ActionListener {

    public static final int SCREEN_WIDTH = 960;
    public static final int SCREEN_HEIGHT = 540;
    private static final int FPS = 60;

    private enum State { PLAYING, GAME_OVER, WIN }

    private final Timer gameTimer;
    private final KeyHandler keyHandler = new KeyHandler();

    private Level level;
    private Player player;
    private int cameraOffsetX;

    private State state = State.PLAYING;
    private int score;
    private int lives;
    private static final int MAX_LIVES = 3;

    public GamePanel() {
        setPreferredSize(new java.awt.Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(new Color(135, 206, 235));
        setFocusable(true);
        addKeyListener(keyHandler);

        startNewGame();

        gameTimer = new Timer(1000 / FPS, this);
        gameTimer.start();
    }

    private void startNewGame() {
        level = new Level();
        player = new Player(level.getStartX(), level.getStartY());
        cameraOffsetX = 0;
        score = 0;
        lives = MAX_LIVES;
        state = State.PLAYING;
    }

    private void respawnPlayer() {
        player.setX(level.getStartX());
        player.setY(level.getStartY());
        player.setVelocityX(0);
        player.setVelocityY(0);
        cameraOffsetX = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (keyHandler.isRestartPressed() && (state == State.GAME_OVER || state == State.WIN)) {
            startNewGame();
        }
        if (state == State.PLAYING) {
            update();
        }
        repaint();
    }

    private void update() {
        handleInput();
        player.applyGravity();

        moveHorizontally();
        moveVertically();

        player.updateAnimation();
        updateCamera();

        for (Enemy enemy : level.getEnemies()) {
            enemy.update();
        }
        for (Coin coin : level.getCoins()) {
            coin.update();
        }

        handleEnemyCollisions();
        handleCoinCollisions();
        checkFallDeath();
        checkGoal();
    }

    private void handleInput() {
        if (keyHandler.isLeft() && !keyHandler.isRight()) {
            player.moveLeft();
        } else if (keyHandler.isRight() && !keyHandler.isLeft()) {
            player.moveRight();
        } else {
            player.stopHorizontal();
        }
        if (keyHandler.isJump()) {
            player.jump();
        }
    }

    private void moveHorizontally() {
        player.setX(player.getX() + player.getVelocityX());

        // No dejar salir del mundo por la izquierda
        if (player.getX() < 0) {
            player.setX(0);
        }

        Rectangle bounds = player.getBounds();
        for (Platform platform : level.getPlatforms()) {
            Rectangle pBounds = platform.getBounds();
            if (bounds.intersects(pBounds)) {
                if (player.getVelocityX() > 0) {
                    player.setX(pBounds.x - bounds.width);
                } else if (player.getVelocityX() < 0) {
                    player.setX(pBounds.x + pBounds.width);
                }
                bounds = player.getBounds();
            }
        }
    }

    private void moveVertically() {
        player.setY(player.getY() + player.getVelocityY());
        player.setOnGround(false);

        Rectangle bounds = player.getBounds();
        for (Platform platform : level.getPlatforms()) {
            Rectangle pBounds = platform.getBounds();
            if (bounds.intersects(pBounds)) {
                if (player.getVelocityY() > 0) {
                    // Cayendo: aterriza encima de la plataforma
                    player.setY(pBounds.y - bounds.height);
                    player.setVelocityY(0);
                    player.setOnGround(true);
                } else if (player.getVelocityY() < 0) {
                    // Saltando: golpea la plataforma por debajo
                    player.setY(pBounds.y + pBounds.height);
                    player.setVelocityY(0);
                }
                bounds = player.getBounds();
            }
        }
    }

    private void updateCamera() {
        int targetOffset = (int) player.getX() - SCREEN_WIDTH / 3;
        if (targetOffset < 0) {
            targetOffset = 0;
        }
        int maxOffset = level.getWorldWidth() - SCREEN_WIDTH;
        if (targetOffset > maxOffset) {
            targetOffset = maxOffset;
        }
        cameraOffsetX = targetOffset;
    }

    private void handleEnemyCollisions() {
        Rectangle playerBounds = player.getBounds();
        for (Enemy enemy : level.getEnemies()) {
            if (!enemy.isAlive()) {
                continue;
            }
            Rectangle enemyBounds = enemy.getBounds();
            if (!playerBounds.intersects(enemyBounds)) {
                continue;
            }
            boolean fallingOnTop = player.getVelocityY() > 0
                    && player.getFeetProbe().intersects(enemyBounds);
            if (fallingOnTop) {
                enemy.kill();
                player.setVelocityY(Player.JUMP_STRENGTH * 0.55);
                score += 50;
            } else {
                loseLife();
                return;
            }
        }
    }

    private void handleCoinCollisions() {
        Rectangle playerBounds = player.getBounds();
        for (Coin coin : level.getCoins()) {
            if (!coin.isCollected() && playerBounds.intersects(coin.getBounds())) {
                coin.collect();
                score += 10;
            }
        }
    }

    private void checkFallDeath() {
        if (player.getY() > SCREEN_HEIGHT + 100) {
            loseLife();
        }
    }

    private void checkGoal() {
        if (player.getX() >= level.getGoalX()) {
            state = State.WIN;
        }
    }

    private void loseLife() {
        lives--;
        if (lives <= 0) {
            state = State.GAME_OVER;
        } else {
            respawnPlayer();
        }
    }

    // ---------------------------------------------------------------
    // Renderizado
    // ---------------------------------------------------------------

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBackground(g2);
        drawGoal(g2);

        for (Platform platform : level.getPlatforms()) {
            platform.draw(g2, cameraOffsetX);
        }
        for (Coin coin : level.getCoins()) {
            coin.draw(g2, cameraOffsetX);
        }
        for (Enemy enemy : level.getEnemies()) {
            enemy.draw(g2, cameraOffsetX);
        }
        player.draw(g2, cameraOffsetX);

        drawHud(g2);

        if (state == State.GAME_OVER) {
            drawOverlay(g2, "GAME OVER", "Pulsa R para reintentar", new Color(180, 30, 30));
        } else if (state == State.WIN) {
            drawOverlay(g2, "¡NIVEL COMPLETADO!", "Pulsa R para jugar de nuevo", new Color(30, 140, 60));
        }
    }

    private void drawBackground(Graphics2D g2) {
        GradientPaint sky = new GradientPaint(0, 0, new Color(135, 206, 235), 0, SCREEN_HEIGHT, new Color(200, 235, 245));
        g2.setPaint(sky);
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Nubes con parallax suave
        g2.setColor(new Color(255, 255, 255, 210));
        int cloudOffset = cameraOffsetX / 3;
        for (int i = 0; i < 6; i++) {
            int cx = (i * 260 - cloudOffset % 260) - 100;
            int cy = 60 + (i % 3) * 40;
            g2.fillOval(cx, cy, 70, 30);
            g2.fillOval(cx + 30, cy - 10, 60, 30);
            g2.fillOval(cx + 55, cy, 60, 26);
        }

        // Colinas de fondo
        g2.setColor(new Color(120, 190, 120, 160));
        int hillOffset = cameraOffsetX / 2;
        for (int i = 0; i < 8; i++) {
            int hx = i * 220 - hillOffset % 220 - 100;
            g2.fillOval(hx, level.getGroundY() - 40, 220, 140);
        }
    }

    private void drawGoal(Graphics2D g2) {
        int poleX = (int) level.getGoalX() - cameraOffsetX;
        int poleTop = level.getGroundY() - 180;
        g2.setColor(new Color(90, 90, 100));
        g2.fillRect(poleX + 18, poleTop, 6, level.getGroundY() - poleTop);

        int[] flagXs = {poleX + 24, poleX + 24 + 40, poleX + 24};
        int[] flagYs = {poleTop + 5, poleTop + 20, poleTop + 35};
        g2.setColor(new Color(60, 160, 90));
        g2.fillPolygon(flagXs, flagYs, 3);

        g2.setColor(new Color(70, 70, 80));
        g2.fillOval(poleX + 12, poleTop - 12, 18, 18);
    }

    private void drawHud(Graphics2D g2) {
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));

        g2.setColor(new Color(0, 0, 0, 140));
        g2.fillRoundRect(10, 10, 230, 74, 14, 14);

        g2.setColor(Color.WHITE);
        g2.drawString("Puntos: " + score, 24, 36);
        g2.drawString("Vidas: ", 24, 62);

        for (int i = 0; i < MAX_LIVES; i++) {
            int hx = 100 + i * 24;
            if (i < lives) {
                g2.setColor(new Color(220, 50, 60));
                g2.fillOval(hx, 48, 18, 16);
            } else {
                g2.setColor(new Color(255, 255, 255, 90));
                g2.drawOval(hx, 48, 18, 16);
            }
        }
    }

    private void drawOverlay(Graphics2D g2, String title, String subtitle, Color accent) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        g2.setFont(new Font("SansSerif", Font.BOLD, 48));
        g2.setColor(accent);
        centerText(g2, title, SCREEN_HEIGHT / 2 - 20);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 22));
        g2.setColor(Color.WHITE);
        centerText(g2, subtitle, SCREEN_HEIGHT / 2 + 24);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        centerText(g2, "Puntuación final: " + score, SCREEN_HEIGHT / 2 + 56);
    }

    private void centerText(Graphics2D g2, String text, int y) {
        int textWidth = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, (SCREEN_WIDTH - textWidth) / 2, y);
    }
}
