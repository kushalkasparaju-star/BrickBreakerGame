package com.mycompany.brick;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GamePlay extends JPanel implements KeyListener, ActionListener {
    // Game state machine for menu and gameplay
    private enum GameState { MENU, HOW_TO, PLAYING, GAME_OVER, WIN }
    private GameState state = GameState.MENU;
    private boolean play = false;
	private int score = 0;
	private int totalBricks;
	private Timer timer;
    private int delay = 10; // smooth update ~100 FPS; 8-12ms is fine
	private int playerX = 310;
	private int ballposX = 120;
	private int ballposY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
	private MapGenerator map;

    public GamePlay() {
		map = new MapGenerator(3, 7);
		totalBricks = 3 * 7;
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
        // Mouse for menu/buttons
        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
		timer = new Timer(delay, this);
		timer.start();
	}

    @Override
    protected void paintComponent(Graphics g) {
        // Swing best practice: clear background and paint within paintComponent
        super.paintComponent(g);
        // Background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        if (state == GameState.MENU || state == GameState.HOW_TO) {
            drawMenu(g);
            return;
        }

        // Game field
        map.draw((Graphics2D) g);
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score: " + score, 540, 30);
        g.setColor(Color.yellow);
        g.fillRect(playerX, 550, 100, 8);
        g.setColor(Color.GREEN);
        g.fillOval(ballposX, ballposY, 20, 20);

        // End states
        if (state == GameState.GAME_OVER) {
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 190, 300);
            drawEndButtons(g);
        } else if (state == GameState.WIN) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Win! Score: " + score, 200, 300);
            drawEndButtons(g);
        }
	}

	@Override
    public void actionPerformed(ActionEvent e) {
        if (state == GameState.PLAYING && play) {
			if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
				ballYdir = -ballYdir;
				int paddleCenter = playerX + 50;
				int hitPos = ballposX - paddleCenter;
				if (hitPos < 0) ballXdir = -Math.abs(ballXdir); else ballXdir = Math.abs(ballXdir);
			}
			A:
			for (int i = 0; i < map.map.length; i++) {
				for (int j = 0; j < map.map[0].length; j++) {
					if (map.map[i][j] > 0) {
						int brickX = j * map.bricksWidth + 80;
						int brickY = i * map.bricksHeight + 50;
						int bricksWidth = map.bricksWidth;
						int bricksHeight = map.bricksHeight;
						Rectangle rect = new Rectangle(brickX, brickY, bricksWidth, bricksHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						if (ballRect.intersects(rect)) {
							map.setBricksValue(0, i, j);
							totalBricks--;
							score += 5;
							int ballLeft = ballposX;
							int ballRight = ballposX + 20;
							int brickLeft = rect.x;
							int brickRight = rect.x + bricksWidth;
							if (ballRight - ballXdir <= brickLeft || ballLeft - ballXdir >= brickRight) {
								ballXdir = -ballXdir;
							} else {
								ballYdir = -ballYdir;
							}
							break A;
						}
					}
				}
            }
			ballposX += ballXdir;
			ballposY += ballYdir;
			if (ballposX < 0) {
				ballXdir = -ballXdir;
				ballposX = 0;
			}
			if (ballposY < 0) {
				ballYdir = -ballYdir;
				ballposY = 0;
			}
			if (ballposX > 670) {
				ballXdir = -ballXdir;
				ballposX = 670;
			}
            if (ballposY > 570) {
                play = false;
                ballXdir = 0;
                ballYdir = 0;
                state = GameState.GAME_OVER;
            }
            if (totalBricks == 0) {
                play = false;
                ballYdir = 0;
                ballXdir = 0;
                state = GameState.WIN;
            }
		}
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
        if (state == GameState.PLAYING && e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (playerX >= 600) playerX = 600; else moveRight();
		}
        if (state == GameState.PLAYING && e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (playerX <= 10) playerX = 10; else moveLeft();
		}
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (state == GameState.MENU) startGame();
            else if (state == GameState.GAME_OVER || state == GameState.WIN) restartFromEnd();
		}
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (state == GameState.PLAYING || state == GameState.GAME_OVER || state == GameState.WIN || state == GameState.HOW_TO) {
                state = GameState.MENU;
                repaint();
            }
        }
	}

    private void restart() {
		play = false;
		ballposX = 120;
		ballposY = 350;
		ballXdir = -1;
		ballYdir = -2;
		playerX = 310;
		score = 0;
		map = new MapGenerator(3, 7);
		totalBricks = 3 * 7;
		repaint();
	}

    private void startGame() {
        restart();
        state = GameState.PLAYING;
        play = true;
        requestFocusInWindow();
    }

    private void restartFromEnd() {
        startGame();
    }

    public void moveRight() {
		play = true;
        playerX += 15;
	}

    public void moveLeft() {
		play = true;
        playerX -= 15;
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

    private void handleMouseClick(int mx, int my) {
        if (state == GameState.MENU) {
            // Buttons: Start, How to Play, Exit
            if (inRect(mx, my, 250, 240, 200, 50)) { startGame(); }
            else if (inRect(mx, my, 250, 310, 200, 50)) { state = GameState.HOW_TO; repaint(); }
            else if (inRect(mx, my, 250, 380, 200, 50)) { System.exit(0); }
        } else if (state == GameState.GAME_OVER || state == GameState.WIN) {
            if (inRect(mx, my, 200, 360, 140, 45)) { restartFromEnd(); }
            else if (inRect(mx, my, 360, 360, 180, 45)) { state = GameState.MENU; repaint(); }
        } else if (state == GameState.HOW_TO) {
            // Click anywhere to go back to menu
            state = GameState.MENU;
            repaint();
        }
    }

    private boolean inRect(int x, int y, int rx, int ry, int rw, int rh) {
        return x >= rx && x <= rx + rw && y >= ry && y <= ry + rh;
    }

    private void drawMenu(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 36));
        g.drawString("Brick Break Game", 200, 150);

        if (state == GameState.MENU) {
            drawButton(g, 250, 240, 200, 50, "Start Game");
            drawButton(g, 250, 310, 200, 50, "How to Play");
            drawButton(g, 250, 380, 200, 50, "Exit");
            g.setFont(new Font("serif", Font.PLAIN, 16));
            g.drawString("Tip: Press Enter to start, Esc for menu", 210, 460);
        } else if (state == GameState.HOW_TO) {
            g.setFont(new Font("serif", Font.PLAIN, 18));
            g.drawString("Controls:", 80, 220);
            g.drawString("Left/Right arrows - Move paddle", 80, 250);
            g.drawString("Enter - Start / Restart", 80, 280);
            g.drawString("Esc - Back to Menu", 80, 310);
            g.drawString("Click anywhere to return to menu", 80, 360);
        }
    }

    private void drawButton(Graphics g, int x, int y, int w, int h, String label) {
        g.setColor(new Color(60, 60, 60));
        g.fillRect(x, y, w, h);
        g.setColor(Color.white);
        g.drawRect(x, y, w, h);
        g.setFont(new Font("serif", Font.BOLD, 18));
        int textWidth = g.getFontMetrics().stringWidth(label);
        int tx = x + (w - textWidth) / 2;
        int ty = y + (h + g.getFontMetrics().getAscent()) / 2 - 4;
        g.drawString(label, tx, ty);
    }

    private void drawEndButtons(Graphics g) {
        drawButton(g, 200, 360, 140, 45, "Restart");
        drawButton(g, 360, 360, 180, 45, "Back to Menu");
    }
}


