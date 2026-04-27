package project.bank.server.db;

import java.sql.*;

public class DatabaseConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/BANK";
    private static final String USER     = "root";
    private static final String PASSWORD = "admin";

    private static Connection connection;
    private static DatabaseConnection INSTANCE;

    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println(" Connected to MySQL DB");
        } catch (SQLException e) {
            System.err.println(" Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseConnection();
        }
        return INSTANCE;
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println(" Reconnected to MySQL DB");
            } catch (SQLException e) {
                System.err.println(" Failed to reconnect: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }
}