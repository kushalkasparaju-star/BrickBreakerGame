package com.mycompany.brick;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class DatabaseManager {
    private static Connection dbConnection = null;

    /**
     * Connects to the SQLite database file `brickbreaker.db` located in the
     * working directory. If the file does not exist, SQLite will create it.
     * Also initializes the database schema (creates tables if needed).
     *
     * @return a java.sql.Connection or null if connection failed
     */
    public static Connection connect() {
        if (dbConnection != null) {
            return dbConnection;
        }

        try {
            // Load the SQLite JDBC driver (optional with newer drivers but kept for clarity)
            Class.forName("org.sqlite.JDBC");

            String url = "jdbc:sqlite:brickbreaker.db";
            dbConnection = DriverManager.getConnection(url);
            System.out.println("Database connected successfully!");

            // Initialize the database schema
            initializeSchema(dbConnection);
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found. Make sure sqlite-jdbc-3.51.0.0.jar is on the classpath.");
        } catch (SQLException e) {
            System.err.println("SQL Error connecting to database: " + e.getMessage());
        }

        return dbConnection;
    }

    /**
     * Initializes the database schema by creating the users table if it doesn't exist.
     *
     * @param conn the database connection
     */
    private static void initializeSchema(Connection conn) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                + "user_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_name TEXT NOT NULL);";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Users table initialized.");
        } catch (SQLException e) {
            System.err.println("SQL Error creating table: " + e.getMessage());
        }
    }

    /**
     * Inserts a new user into the users table and returns the generated user_id.
     *
     * @param userName the name of the user
     * @return the generated user_id, or -1 if insertion failed
     */
    public static int insertUser(String userName) {
        String insertSQL = "INSERT INTO users (user_name) VALUES (?)";
        int generatedUserId = -1;

        Connection conn = (dbConnection != null ? dbConnection : connect());
        if (conn == null) {
            System.err.println("Cannot insert user: database connection is null.");
            return generatedUserId;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, userName);
            pstmt.executeUpdate();

            // Retrieve the generated user_id
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedUserId = generatedKeys.getInt(1);
                    System.out.println("User '" + userName + "' inserted with ID: " + generatedUserId);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error inserting user: " + e.getMessage());
        }

        return generatedUserId;
    }

    /**
     * Retrieves and prints all users from the users table.
     */
    public static void getAllUsers() {
        String selectSQL = "SELECT user_id, user_name FROM users";

        Connection conn = (dbConnection != null ? dbConnection : connect());
        if (conn == null) {
            System.err.println("Cannot retrieve users: database connection is null.");
            return;
        }

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            System.out.println("\n--- All Users ---");
            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String userName = rs.getString("user_name");
                System.out.println("ID: " + userId + ", Name: " + userName);
            }
            System.out.println("--- End of Users ---\n");
        } catch (SQLException e) {
            System.err.println("SQL Error retrieving users: " + e.getMessage());
        }
    }
}
