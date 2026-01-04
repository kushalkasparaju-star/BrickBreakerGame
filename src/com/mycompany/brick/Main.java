package com.mycompany.brick;

import javax.swing.JFrame;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // 1️⃣ Ask only the username
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine().trim();

        if (playerName.isEmpty()) {
            playerName = "Player";
        }

        // 2️⃣ Store user silently in database
        // (No prints, no warnings)
        int userId = DatabaseManager.insertUser(playerName);

        // 3️⃣ Close scanner (input done)
        scanner.close();

        // 4️⃣ Start the game immediately
        JFrame frame = new JFrame();
        GamePlay gamePlay = new GamePlay();

        frame.setBounds(10, 10, 700, 600);
        frame.setTitle("Brick Breaker Game");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePlay);
        frame.setVisible(true);

        // Ensure keyboard focus works
        gamePlay.requestFocusInWindow();
        gamePlay.requestFocus();
    }
}
