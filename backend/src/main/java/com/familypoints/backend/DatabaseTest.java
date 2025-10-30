package com.familypoints.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseTest {
    private static final String DB_URL = "jdbc:postgresql://47.122.6.12:5432/family_points";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Wasd1234!";

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("org.postgresql.Driver");
            
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            System.out.println("Database connection successful!");
            
            statement = connection.createStatement();
            
            // Test tables existence
            System.out.println("Testing table structure...");
            String[] tables = {"user_account", "family", "member", "task", "task_submission", 
                              "reward", "exchange", "points_log", "user_balance"};
            
            for (String tableName : tables) {
                String query = "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = '" + tableName + "')";
                resultSet = statement.executeQuery(query);
                if (resultSet.next() && resultSet.getBoolean(1)) {
                    System.out.println("PASS: Table " + tableName + " exists");
                } else {
                    System.out.println("FAIL: Table " + tableName + " does not exist");
                }
                resultSet.close();
            }
            
            // Test UUID extension
            System.out.println("Testing UUID extension...");
            try {
                String uuidQuery = "SELECT uuid_generate_v4()";
                resultSet = statement.executeQuery(uuidQuery);
                if (resultSet.next()) {
                    System.out.println("PASS: UUID extension is enabled");
                }
                resultSet.close();
            } catch (SQLException e) {
                System.out.println("FAIL: UUID extension is not enabled: " + e.getMessage());
            }
            
            System.out.println("Database test completed.");
            
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: PostgreSQL JDBC driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("ERROR: Database operation failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("ERROR: " + e.getMessage());
            }
        }
    }
}