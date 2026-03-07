package lld;

import java.io.*;
import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

public enum AppService {
    INSTANCE;

    private final Properties config;
    private final Connection dbConnection;
    private final Logger logger;
    private final List<String> activeUsers;

    AppService() {
        this.logger = Logger.getLogger(AppService.class.getName());

        // Load config from file
        this.config = new Properties();
        try (InputStream input = new FileInputStream("app.properties")) {
            config.load(input);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to load config: " + e.getMessage());
        }

        // DB connection using config values
        try {
            this.dbConnection = DriverManager.getConnection(
                    config.getProperty("db.url"),
                    config.getProperty("db.user"),
                    config.getProperty("db.password")
            );
        } catch (SQLException e) {
            throw new ExceptionInInitializerError("Failed to connect to DB: " + e.getMessage());
        }

        this.activeUsers = new ArrayList<>();
    }

    // --- Business Methods ---
    public String getProperty(String key) {
        return config.getProperty(key);
    }

    public Connection getConnection() {
        return dbConnection;
    }

    public void addUser(String username) {
        activeUsers.add(username);
        logger.info("User added: " + username);
    }

    public List<String> getActiveUsers() {
        return Collections.unmodifiableList(activeUsers);
    }
}