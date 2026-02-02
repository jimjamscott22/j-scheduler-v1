package com.jscheduler.data;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton class for managing database connections to MariaDB.
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private String url;
    private String username;
    private String password;

    private DatabaseConnection() {
        loadDatabaseProperties();
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private void loadDatabaseProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("database.properties")) {
            if (input == null) {
                System.err.println("Unable to find database.properties");
                setDefaultProperties();
                return;
            }
            props.load(input);

            String host = props.getProperty("db.host", "localhost");
            String port = props.getProperty("db.port", "3306");
            String dbName = props.getProperty("db.name", "jscheduler");
            this.username = props.getProperty("db.username", "root");
            this.password = props.getProperty("db.password", "");

            this.url = String.format("jdbc:mariadb://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true",
                    host, port, dbName);

            System.out.println("Database configuration loaded successfully");
        } catch (IOException ex) {
            System.err.println("Error loading database properties: " + ex.getMessage());
            setDefaultProperties();
        }
    }

    private void setDefaultProperties() {
        this.url = "jdbc:mariadb://localhost:3306/jscheduler?useSSL=false&allowPublicKeyRetrieval=true";
        this.username = "root";
        this.password = "";
        System.out.println("Using default database configuration");
    }

    /**
     * Get a new database connection.
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MariaDB JDBC Driver not found", e);
        } catch (SQLException e) {
            // If remote connection fails due to authentication, try localhost fallback
            if (e.getMessage().contains("Access denied") && !url.contains("localhost")) {
                System.err.println("Remote database authentication failed: " + e.getMessage());
                System.err.println("Attempting to connect to local database instead...");
                
                String localUrl = "jdbc:mariadb://localhost:3306/" + 
                    url.substring(url.lastIndexOf("/") + 1);
                try {
                    Connection localConn = DriverManager.getConnection(localUrl, "root", "");
                    System.out.println("Successfully connected to local database");
                    // Update connection settings for future calls
                    this.url = localUrl;
                    this.username = "root";
                    this.password = "";
                    return localConn;
                } catch (SQLException localEx) {
                    System.err.println("Local database connection also failed: " + localEx.getMessage());
                }
            }
            throw new SQLException("Database authentication failed. Please check credentials in database.properties\n" +
                    "Current config: " + url + " (user: " + username + ")\n" +
                    "Original error: " + e.getMessage(), e);
        }
    }

    /**
     * Test the database connection.
     * @return true if connection is successful, false otherwise
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get the database URL (for debugging).
     * @return database URL
     */
    public String getUrl() {
        return url;
    }
}
