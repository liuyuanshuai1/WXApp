package com.familypoints.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class CRUDTest {
    private static final String DB_URL = "jdbc:postgresql://47.122.6.12:5432/family_points";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Wasd1234!";

    public static void main(String[] args) {
        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");
            
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            System.out.println("Database connection successful!");
            
            // Test CRUD operations
            testUserCRUD(connection);
            
            System.out.println("All CRUD tests completed.");
            
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: PostgreSQL JDBC driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("ERROR: Database operation failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("ERROR: " + e.getMessage());
            }
        }
    }
    
    private static void testUserCRUD(Connection connection) throws SQLException {
        System.out.println("\n=== Testing User CRUD Operations ===");
        
        String openId = "test_open_id_" + System.currentTimeMillis();
        String nickname = "Test User";
        String avatarUrl = "http://example.com/avatar.jpg";
        
        // CREATE - Insert a new user
        String insertSQL = "INSERT INTO user_account (open_id, nickname, avatar_url) VALUES (?, ?, ?)";
        PreparedStatement insertStmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
        insertStmt.setString(1, openId);
        insertStmt.setString(2, nickname);
        insertStmt.setString(3, avatarUrl);
        
        int rowsInserted = insertStmt.executeUpdate();
        System.out.println("CREATE: Inserted " + rowsInserted + " user(s)");
        
        // Get the generated user ID
        ResultSet generatedKeys = insertStmt.getGeneratedKeys();
        String userId = null;
        if (generatedKeys.next()) {
            userId = generatedKeys.getString(1);
            System.out.println("Generated User ID: " + userId);
        }
        generatedKeys.close();
        insertStmt.close();
        
        if (userId != null) {
            // READ - Retrieve the user
            String selectSQL = "SELECT id, open_id, nickname, avatar_url, created_at FROM user_account WHERE id = ?";
            PreparedStatement selectStmt = connection.prepareStatement(selectSQL);
            selectStmt.setObject(1, UUID.fromString(userId));
            
            ResultSet resultSet = selectStmt.executeQuery();
            if (resultSet.next()) {
                System.out.println("READ: Found user with ID: " + resultSet.getObject("id"));
                System.out.println("  OpenID: " + resultSet.getString("open_id"));
                System.out.println("  Nickname: " + resultSet.getString("nickname"));
                System.out.println("  Avatar URL: " + resultSet.getString("avatar_url"));
                System.out.println("  Created At: " + resultSet.getTimestamp("created_at"));
            } else {
                System.out.println("READ: User not found");
            }
            resultSet.close();
            selectStmt.close();
            
            // UPDATE - Update the user's nickname
            String updateSQL = "UPDATE user_account SET nickname = ? WHERE id = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateSQL);
            String newNickname = "Updated Test User";
            updateStmt.setString(1, newNickname);
            updateStmt.setObject(2, UUID.fromString(userId));
            
            int rowsUpdated = updateStmt.executeUpdate();
            System.out.println("UPDATE: Updated " + rowsUpdated + " user(s)");
            updateStmt.close();
            
            // Verify update
            selectStmt = connection.prepareStatement("SELECT nickname FROM user_account WHERE id = ?");
            selectStmt.setObject(1, UUID.fromString(userId));
            resultSet = selectStmt.executeQuery();
            if (resultSet.next()) {
                String updatedNickname = resultSet.getString("nickname");
                System.out.println("VERIFY UPDATE: Nickname is now '" + updatedNickname + "'");
            }
            resultSet.close();
            selectStmt.close();
            
            // DELETE - Delete the user
            String deleteSQL = "DELETE FROM user_account WHERE id = ?";
            PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL);
            deleteStmt.setObject(1, UUID.fromString(userId));
            
            int rowsDeleted = deleteStmt.executeUpdate();
            System.out.println("DELETE: Deleted " + rowsDeleted + " user(s)");
            deleteStmt.close();
            
            // Verify deletion
            selectStmt = connection.prepareStatement("SELECT COUNT(*) as count FROM user_account WHERE id = ?");
            selectStmt.setObject(1, UUID.fromString(userId));
            resultSet = selectStmt.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                System.out.println("VERIFY DELETE: User count after deletion: " + count);
            }
            resultSet.close();
            selectStmt.close();
        }
    }
}