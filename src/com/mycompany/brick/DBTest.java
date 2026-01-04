package com.mycompany.brick;

public class DBTest {
    public static void main(String[] args) {
        java.sql.Connection conn = DatabaseManager.connect();
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }
}
