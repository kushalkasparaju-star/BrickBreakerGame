package com.mycompany.brick;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class PowerUp {
	public enum Type { EXPAND_PADDLE, SLOW_BALL, EXTRA_LIFE }

	public final Type type;
	public int x;
	public int y;
	public int width = 20;
	public int height = 20;
	public int fallSpeed = 3;
	public boolean active = true;

	public PowerUp(Type type, int x, int y) {
		this.type = type;
		this.x = x;
		this.y = y;
	}

	public void update() {
		if (!active) return;
		y += fallSpeed;
	}

	public void draw(Graphics2D g) {
		if (!active) return;
		switch (type) {
			case EXPAND_PADDLE:
				g.setColor(new Color(100, 200, 255));
				break;
			case SLOW_BALL:
				g.setColor(new Color(255, 180, 100));
				break;
			default:
				g.setColor(new Color(150, 255, 150));
		}
		g.fillRect(x, y, width, height);
		g.setColor(Color.black);
		g.drawRect(x, y, width, height);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
}


