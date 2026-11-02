package com.fot.tms.database;

import com.fot.tms.config.DBConfig;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Centralized Database Connection Singleton.
 * Provides safe parameterized connection lifecycle management.
 * Features intelligent automatic driver resolution: dynamically loads
 * the MySQL JDBC Driver from lib/ folder if not found on classpath.
 */
public class DBConnection {
    private static volatile DBConnection instance;
    private Connection connection;

    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Intelligent Fallback: Auto-load driver from lib folder
            autoLoadDriverFromLib();
        }
    }

    /**
     * Dynamically finds and registers the MySQL JDBC Driver from the lib directory.
     * Guarantees database connectivity across all environments and folder names.
     */
    private static void autoLoadDriverFromLib() {
        try {
            File jarFile = findMySQLJar();
            if (jarFile != null && jarFile.exists()) {
                URLClassLoader loader = new URLClassLoader(
                    new URL[]{jarFile.toURI().toURL()},
                    DBConnection.class.getClassLoader()
                );
                Driver driver = (Driver) Class.forName("com.mysql.cj.jdbc.Driver", true, loader)
                        .getDeclaredConstructor().newInstance();
                DriverManager.registerDriver(new DriverShim(driver));
                System.out.println("[FoT-TMS] MySQL JDBC Driver successfully auto-loaded from: " + jarFile.getPath());
            } else {
                System.err.println("MySQL JDBC Driver not found: Please ensure mysql-connector jar exists in lib/ directory.");
            }
        } catch (Exception ex) {
            System.err.println("Database auto-driver loader notification: " + ex.getMessage());
        }
    }

    /**
     * Searches common relative paths for the MySQL connector jar file.
     */
    private static File findMySQLJar() {
        String[] possiblePaths = {
            "lib/mysql-connector-j-8.3.0.jar",
            "../lib/mysql-connector-j-8.3.0.jar",
            "lib",
            "../lib"
        };

        for (String path : possiblePaths) {
            File f = new File(path);
            if (f.exists() && f.isFile() && f.getName().endsWith(".jar")) {
                return f;
            }
            if (f.exists() && f.isDirectory()) {
                File[] jars = f.listFiles((dir, name) -> name.startsWith("mysql-connector") && name.endsWith(".jar"));
                if (jars != null && jars.length > 0) {
                    return jars[0];
                }
            }
        }
        return null;
    }

    /**
     * Driver wrapper enabling DriverManager to accept drivers from custom ClassLoaders.
     */
    private static class DriverShim implements Driver {
        private final Driver driver;

        DriverShim(Driver d) {
            this.driver = d;
        }

        @Override
        public boolean acceptsURL(String u) throws SQLException {
            return driver.acceptsURL(u);
        }

        @Override
        public Connection connect(String u, Properties p) throws SQLException {
            return driver.connect(u, p);
        }

        @Override
        public int getMajorVersion() {
            return driver.getMajorVersion();
        }

        @Override
        public int getMinorVersion() {
            return driver.getMinorVersion();
        }

        @Override
        public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
            return driver.getPropertyInfo(u, p);
        }

        @Override
        public boolean jdbcCompliant() {
            return driver.jdbcCompliant();
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return driver.getParentLogger();
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
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                DBConfig.DB_URL,
                DBConfig.DB_USER,
                DBConfig.DB_PASSWORD
            );
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
                System.err.println("Error closing connection: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }
}
