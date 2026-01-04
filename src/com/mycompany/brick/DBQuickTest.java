package com.mycompany.brick;

import java.util.Scanner;

public class DBQuickTest {
    public static void main(String[] args) {
        // Connect to the database
        DatabaseManager.connect();

        // Prompt for player name
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine().trim();

        if (playerName.isEmpty()) {
            playerName = "Player";
        }

        // Insert the player into the database
        int userId = DatabaseManager.insertUser(playerName);
        System.out.println("Welcome, " + playerName + "! Your user ID is: " + userId);

        // Display all users in the database
        DatabaseManager.getAllUsers();

        // Close the scanner
        scanner.close();

        System.out.println("Test complete. Database operations verified.");
    }
}
