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
 * Features intelligent automatic driver resolution: recursively searches
 * and dynamically loads the MySQL JDBC Driver from any project lib/ folder.
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
                System.out.println("[FoT-TMS] MySQL JDBC Driver successfully auto-loaded from: " + jarFile.getAbsolutePath());
            } else {
                System.err.println("[FoT-TMS WARNING] Could not find mysql-connector*.jar in any 'lib' folder!");
                System.err.println("Working Directory: " + new File(".").getAbsolutePath());
            }
        } catch (Exception ex) {
            System.err.println("Database auto-driver loader notification: " + ex.getMessage());
        }
    }

    /**
     * Searches upwards from both class location and working directory for mysql-connector*.jar.
     */
    private static File findMySQLJar() {
        // 1. Search upwards from the running class location (e.g. out/production/module -> project root)
        try {
            File current = new File(DBConnection.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            for (int i = 0; i < 6 && current != null; i++) {
                File found = searchDirForJar(new File(current, "lib"));
                if (found != null) return found;
                current = current.getParentFile();
            }
        } catch (Exception ignored) {}

        // 2. Search upwards from working directory
        try {
            File current = new File(".").getAbsoluteFile();
            for (int i = 0; i < 6 && current != null; i++) {
                File found = searchDirForJar(new File(current, "lib"));
                if (found != null) return found;
                current = current.getParentFile();
            }
        } catch (Exception ignored) {}

        return null;
    }

    private static File searchDirForJar(File dir) {
        if (dir != null && dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> {
                String n = name.toLowerCase();
                return n.contains("mysql") && n.endsWith(".jar");
            });
            if (files != null && files.length > 0) {
                return files[0];
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
