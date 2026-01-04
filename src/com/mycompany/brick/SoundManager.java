package com.mycompany.brick;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundManager {
	private static void play(String resourceName) {
		try {
			InputStream raw = SoundManager.class.getClassLoader().getResourceAsStream(resourceName);
			if (raw == null) return; // graceful fallback
			try (BufferedInputStream bis = new BufferedInputStream(raw)) {
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(bis);
				Clip clip = AudioSystem.getClip();
				clip.open(audioIn);
				clip.start();
			}
		} catch (Exception ignored) {
			// No crash if audio unavailable
		}
	}

	public static void paddleHit() { play("hit_paddle.wav"); }
	public static void wallHit() { play("hit_wall.wav"); }
	public static void brickBreak() { play("break_brick.wav"); }
	public static void powerUp() { play("powerup.wav"); }
	public static void win() { play("win.wav"); }
	public static void gameOver() { play("gameover.wav"); }
}


