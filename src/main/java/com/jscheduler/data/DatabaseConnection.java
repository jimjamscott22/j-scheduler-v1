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
