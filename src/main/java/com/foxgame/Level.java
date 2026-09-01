package com.foxgame;

import com.foxgame.entities.Coin;
import com.foxgame.entities.Enemy;
import com.foxgame.entities.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * Define el diseño (layout) del nivel: plataformas, enemigos, monedas,
 * punto de inicio y la meta final.
 */
public class Level {

    private final List<Platform> platforms = new ArrayList<>();
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Coin> coins = new ArrayList<>();

    private final double startX = 60;
    private final double startY = 380;
    private final double goalX;
    private final int worldWidth;
    private final int groundY = 460;

    public Level() {
        // Suelo principal dividido en tramos, con algunos huecos (pozos)
        addGround(0, 400);
        addGround(470, 260);
        addGround(820, 340);
        addGround(1260, 200);
        addGround(1560, 840);

        // Plataformas flotantes
        platforms.add(new Platform(300, 300, 100, 20));
        platforms.add(new Platform(520, 260, 90, 20));
        platforms.add(new Platform(700, 340, 80, 20));
        platforms.add(new Platform(900, 260, 100, 20));
        platforms.add(new Platform(1080, 200, 90, 20));
        platforms.add(new Platform(1300, 320, 100, 20));
        platforms.add(new Platform(1480, 250, 90, 20));
        platforms.add(new Platform(1700, 340, 110, 20));
        platforms.add(new Platform(1900, 260, 90, 20));
        platforms.add(new Platform(2050, 200, 100, 20));

        // Enemigos patrullando
        enemies.add(new Enemy(520, groundY - 24, 470, 700));
        enemies.add(new Enemy(900, 230, 900, 990));
        enemies.add(new Enemy(1300, groundY - 24, 1260, 1440));
        enemies.add(new Enemy(1700, groundY - 24, 1560, 1900));
        enemies.add(new Enemy(2100, groundY - 24, 2050, 2260));

        // Monedas / gemas
        addCoinRow(320, 260, 3, 30);
        addCoinRow(530, 220, 2, 30);
        addCoinRow(900, 220, 3, 30);
        addCoinRow(1080, 160, 2, 30);
        addCoinRow(1300, 280, 3, 30);
        addCoinRow(1700, 300, 4, 30);
        addCoinRow(2050, 160, 3, 30);

        this.worldWidth = 2400;
        this.goalX = worldWidth - 120;
    }

    private void addGround(double x, double width) {
        platforms.add(new Platform(x, groundY, (int) width, 60));
    }

    private void addCoinRow(double startX, double y, int count, double spacing) {
        for (int i = 0; i < count; i++) {
            coins.add(new Coin(startX + i * spacing, y));
        }
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getGoalX() {
        return goalX;
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getGroundY() {
        return groundY;
    }
}
