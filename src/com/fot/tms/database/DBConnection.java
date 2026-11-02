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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Centralized Database Connection Singleton.
 * Provides safe parameterized connection lifecycle management.
 * Features intelligent automatic driver resolution: recursively searches
 * parent and child folders to guarantee finding MySQL JDBC jar in any environment.
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
     * Dynamically finds and registers the MySQL JDBC Driver from candidate directories.
     */
    private static void autoLoadDriverFromLib() {
        try {
            File jarFile = findMySQLJar();
            if (jarFile != null && jarFile.exists()) {
                URLClassLoader loader = new URLClassLoader(
                    new URL[]{jarFile.toURI().toURL()},
                    DBConnection.class.getClassLoader()
                );
                
                // Try com.mysql.cj.jdbc.Driver (MySQL 8+), then com.mysql.jdbc.Driver (MySQL 5)
                Driver driver = null;
                try {
                    driver = (Driver) Class.forName("com.mysql.cj.jdbc.Driver", true, loader)
                            .getDeclaredConstructor().newInstance();
                } catch (ClassNotFoundException e) {
                    driver = (Driver) Class.forName("com.mysql.jdbc.Driver", true, loader)
                            .getDeclaredConstructor().newInstance();
                }

                if (driver != null) {
                    DriverManager.registerDriver(new DriverShim(driver));
                    System.out.println("[FoT-TMS] SUCCESS: MySQL JDBC Driver auto-loaded from: " + jarFile.getAbsolutePath());
                }
            } else {
                System.err.println("[FoT-TMS ERROR] Cannot find MySQL jar file.");
            }
        } catch (Exception ex) {
            System.err.println("[FoT-TMS ERROR] Auto-load failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Recursively searches upward and downward to locate any lib folder with the MySQL jar.
     */
    private static File findMySQLJar() {
        List<File> roots = new ArrayList<>();

        // 1. Current working directory and parents
        File cur = new File("").getAbsoluteFile();
        while (cur != null) {
            roots.add(cur);
            cur = cur.getParentFile();
        }

        // 2. Code source location and parents
        try {
            String path = DBConnection.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            path = java.net.URLDecoder.decode(path, "UTF-8");
            File codeDir = new File(path);
            while (codeDir != null) {
                if (!roots.contains(codeDir)) {
                    roots.add(codeDir);
                }
                codeDir = codeDir.getParentFile();
            }
        } catch (Exception ignored) {}

        // For each root, search direct folders and child folders up to 3 levels deep
        for (File root : roots) {
            File found = searchDirRecursively(root, 0);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private static File searchDirRecursively(File dir, int depth) {
        if (dir == null || !dir.exists() || depth > 3) return null;
        String dirName = dir.getName().toLowerCase();
        if (dirName.equals(".git") || dirName.equals(".idea") || dirName.equals("out")) return null;

        File[] files = dir.listFiles();
        if (files == null) return null;

        // If this folder is a lib folder, look for jar files inside
        if (dirName.equals("lib") || dirName.equals("libs")) {
            for (File f : files) {
                if (f.isFile()) {
                    String name = f.getName().toLowerCase();
                    if (name.endsWith(".jar") && !name.contains("flatlaf")) {
                        return f;
                    }
                    if (name.contains("mysql")) {
                        return f;
                    }
                }
            }
        }

        // Search subdirectories
        for (File f : files) {
            if (f.isDirectory()) {
                File found = searchDirRecursively(f, depth + 1);
                if (found != null) return found;
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
