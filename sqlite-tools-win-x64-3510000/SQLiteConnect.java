import java.sql.*;

public class SQLiteConnect {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:sample.db"; // SQLite DB file

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("✅ Connected to SQLite successfully!");

                Statement stmt = conn.createStatement();
                stmt.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, name TEXT)");
                stmt.execute("INSERT INTO users (name) VALUES ('Alice'), ('Bob')");

                ResultSet rs = stmt.executeQuery("SELECT * FROM users");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Database error: " + e.getMessage());
        }
    }
}
