Brick Breaker Game (Java Swing)
================================

How to build
------------

Option A (simple):

1. Open a terminal in `BrickBreakerGame/src`
2. Compile:

```
javac com/mycompany/brick/*.java
```

3. Run:

```
java com.mycompany.brick.Main
```

Option B (with out folder):

```
javac -d ../out com/mycompany/brick/*.java
java -cp ../out com.mycompany.brick.Main
```

Optional sounds
---------------
Place WAV files in classpath root (or use resources on your IDE classpath):

- `hit_paddle.wav`
- `hit_wall.wav`
- `break_brick.wav`
- `powerup.wav`
- `win.wav`
- `gameover.wav`

Features
--------
- 700x600 window, paddle, ball, bricks
- Score, Game Over, You Win, Enter-to-Restart
- Colored bricks, borders, smooth Timer-based loop

Files
-----
- `Main.java` – launcher JFrame
- `GamePlay.java` – gameplay, input, physics, rendering
- `MapGenerator.java` – brick grid and drawing
- `PowerUp.java` – optional power-up entity (not yet wired)
- `SoundManager.java` – optional audio helper (graceful fallback)

