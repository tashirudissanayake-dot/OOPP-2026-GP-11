package com.fot.tms.database;

import com.fot.tms.config.DBConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection{
    private static volatile DBConnection instance;
    private Connection connection;

    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL JDBC Driver not found. Add MySQL Connector/J to the project.", e);
        }
        
    }

    
    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                  }
            }
        }
        return instance;
    }
    

    public synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()){
            connection = DriverManager.getConnection(
                    DBConfig.DB_URL, DBConfig.DB_USER, DBConfig.DB_PASSWORD);
        }
        return connection;
    }

    public synchronized void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
}

}
