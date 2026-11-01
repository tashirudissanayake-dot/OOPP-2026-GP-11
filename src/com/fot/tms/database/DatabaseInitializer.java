package com.fot.tms.database;

import com.fot.tms.config.DBConfig;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;



public class DatabaseInitializer {

    public static boolean initializeDatabaseIfNeeded() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(DBConfig.BASE_URL, DBConfig.DB_USER, DBConfig.DB_PASSWORD);
                 Statement stmt = conn.createStatement()) {
                
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DBConfig.DB_NAME + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");
            }


            try (Connection conn = DBConnection.getInstance().getConnection();
                 Statement stmt = conn.createStatement()) {
                

                try {
                    syncCourseMaterialsTable(conn);
                } catch (Exception ex) {
                    System.err.println("Course materials sync warning: " + ex.getMessage());
                }

                boolean needSeed = false;
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users;")) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        needSeed = true;
                    }
                } catch (Exception ex) {
                    needSeed = true;
                }

                if (needSeed) {
                    System.out.println("Executing database initialization script...");
                    executeSqlScript("database/fot_tms_db.sql");
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("Database initialization warning: " + e.getMessage());
            return false;
        }
    }

    public static void executeSqlScript(String scriptPath) {
        File file = new File(scriptPath);
        if (!file.exists()) {
            file = new File("../" + scriptPath);
        }
        if (!file.exists()) {
            System.err.println("SQL script not found at: " + scriptPath);
            return;
        }

        try (Connection conn = DBConnection.getInstance().getConnection();
             BufferedReader br = new BufferedReader(new FileReader(file));
             Statement stmt = conn.createStatement()) {

            StringBuilder sb = new StringBuilder();
            String line;
            boolean inDelimiterBlock = false;

            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("--") || trimmed.startsWith("//")) {
                    continue;
                }
                if (trimmed.startsWith("DELIMITER")) {
                    inDelimiterBlock = !inDelimiterBlock;
                    continue;
                }

                sb.append(line).append("\n");
                if (!inDelimiterBlock && trimmed.endsWith(";")) {
                    String query = sb.toString().trim();
                    if (query.endsWith(";")) {
                        query = query.substring(0, query.length() - 1);
                    }
                    if (!query.isEmpty()) {
                        try {
                            stmt.execute(query);
                        } catch (Exception e) {
                            System.err.println("Error running statement: " + e.getMessage());
                        }
                    }
                    sb.setLength(0);
                }
            }
            System.out.println("SQL script executed successfully.");
        } catch (Exception e) {
            System.err.println("Error executing SQL script: " + e.getMessage());
        }
    }

    private static void syncCourseMaterialsTable(Connection conn) {
        File matDir = new File("materials");
        if (!matDir.exists()) matDir = new File("../materials");
        if (!matDir.exists() || !matDir.isDirectory()) return;

        File[] files = matDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf") || name.toLowerCase().endsWith(".pptx"));
        if (files == null || files.length == 0) return;

        try (Statement stmt = conn.createStatement()) {

            boolean hasOld = false;
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM course_materials WHERE file_path LIKE '%ict2123_lec%' OR file_path LIKE '%ict2132_lab%';")) {
                if (rs.next() && rs.getInt(1) > 0) hasOld = true;
            }
            if (hasOld) {
                stmt.executeUpdate("DELETE FROM course_materials WHERE file_path LIKE '%ict2123_lec%' OR file_path LIKE '%ict2132_lab%';");
            }

            for (File f : files) {
                String fname = f.getName();
                String relPath = "materials/" + fname;
                

                int courseId = 0;
                if (fname.startsWith("ICT2113")) courseId = 1;
                else if (fname.startsWith("ICT2123")) courseId = 2;
                else if (fname.startsWith("ICT2133")) courseId = 3;
                else if (fname.startsWith("ICT2143")) courseId = 4;
                else if (fname.startsWith("ICT2132")) courseId = 5;
                else if (fname.startsWith("ICT2153")) courseId = 6;
                else if (fname.startsWith("ICT2163")) courseId = 7;
                else if (fname.startsWith("ICT2172")) courseId = 8;
                else if (fname.startsWith("ICT2182")) courseId = 9;

                if (courseId > 0) {
                    try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM course_materials WHERE file_path = '" + relPath + "'")) {
                        if (rs.next() && rs.getInt(1) == 0) {
                            String title = fname.replace("_", " ").replace(".pdf", "").replace(".pptx", "");
                            stmt.executeUpdate("INSERT INTO course_materials (course_id, title, file_path, uploaded_date) VALUES (" 
                                + courseId + ", '" + title + "', '" + relPath + "', NOW())");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("syncCourseMaterialsTable error: " + e.getMessage());
        }
    }
}