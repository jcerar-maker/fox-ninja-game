# Kiro el Zorro Ninja 🦊 — Aventura de Plataformas 2D

Un juego de plataformas 2D estilo "Mario Bros", pero con un protagonista
propio: **Kiro**, un zorro ninja que debe atravesar un bosque saltando
plataformas, esquivando (o derrotando) slimes enemigos, recolectando gemas
y llegando hasta el portal final.

Hecho en **Java puro** (Swing/AWT, sin librerías externas ni imágenes que
descargar) como proyecto **Maven**, listo para abrir en IntelliJ IDEA.

## Cómo abrirlo en IntelliJ

1. Abre IntelliJ IDEA.
2. `File > Open...`
3. Selecciona la carpeta `fox-ninja-game` (o directamente el archivo `pom.xml`
   dentro de ella).
4. IntelliJ detectará que es un proyecto Maven e importará todo automáticamente.
5. Busca la clase `Main.java` en `src/main/java/com/foxgame/Main.java`,
   haz clic derecho sobre ella y elige **Run 'Main.main()'** (o el botón ▶
   verde junto al método `main`).

Si prefieres compilarlo por terminal con Maven:

```bash
mvn compile exec:java
```

o generar un `.jar` ejecutable:

```bash
mvn package
java -jar target/fox-ninja-game.jar
```

## Controles

| Acción             | Teclas                     |
|---------------------|-----------------------------|
| Moverse izquierda   | `←` o `A`                   |
| Moverse derecha     | `→` o `D`                   |
| Saltar              | `↑`, `W` o `ESPACIO`        |
| Reiniciar (al perder/ganar) | `R`                  |

## Objetivo

- Recolecta las **gemas** doradas para sumar puntos.
- Salta **encima** de los enemigos (slimes rosas) para derrotarlos.
  Si te tocan de lado, pierdes una vida.
- Evita caer a los pozos/vacíos: también pierdes una vida.
- Tienes 3 vidas. Al perderlas todas, es Game Over (pulsa `R` para reintentar).
- Llega hasta la **bandera** al final del nivel para ganar.

## Estructura del proyecto

```
fox-ninja-game/
├── pom.xml
└── src/main/java/com/foxgame/
    ├── Main.java              # Punto de entrada
    ├── GameFrame.java         # Ventana (JFrame)
    ├── GamePanel.java         # Bucle del juego, física, colisiones, render
    ├── KeyHandler.java        # Entrada de teclado
    ├── Level.java             # Diseño del nivel (plataformas, enemigos, gemas)
    └── entities/
        ├── GameObject.java    # Clase base
        ├── Player.java        # Kiro, el zorro ninja
        ├── Enemy.java         # Slimes patrulleros
        ├── Coin.java          # Gemas coleccionables
        └── Platform.java      # Plataformas/suelo
```

## Ideas para ampliarlo

- Añadir más niveles (crear varias clases `Level` o cargar el diseño desde
  un archivo de texto/JSON).
- Sonido con `javax.sound.sampled`.
- Power-ups (doble salto, velocidad, invencibilidad temporal).
- Sustituir los gráficos vectoriales por sprites/imágenes (usando
  `ImageIO.read()` y `BufferedImage`).
- Pantalla de menú principal y pausa.
