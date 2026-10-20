package com.fot.tms.database;

import com.fot.tms.config.DBConfig;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public final class DatabaseInitializer {
    private static final String SQL_SCRIPT = "database/fot_tms_db.sql";

    private DatabaseInitializer() {
    }



    public static boolean initializeDatabaseIfNeeded(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection serverConnection = DriverManager.getConnection(
                    DBConfig.BASE_URL, DBConfig.DB_USER, DBConfig.DB_PASSWORD);
                 Statement statement = serverConnection.createStatement()) {
                statement.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + DBConfig.DB_NAME + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            }
            executeSqlScript(SQL_SCRIPT);
            return true;
        }catch (Exception e){
            System.err.println("Database initialization failed: " + e.getMessage());
            return false;
        }
    }



    public static void executeSqlScript(String scriptPath) throws SQLException, IOException {
        File file = new File(scriptPath);
        if (!file.isFile()) {
            file = new File("../" + scriptPath);
        }
        if (!file.isFile()) {
            throw new IOException("SQL script not found: " + scriptPath + ". Run the application from the project root folder.");
        }




        try (Connection connection = DBConnection.getInstance().getConnection();
             BufferedReader reader = new BufferedReader(new FileReader(file));
             Statement statement = connection.createStatement()) {
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("--") || trimmed.startsWith("//")) {
                    continue;
                }
                sql.append(line).append('\n');
                if (trimmed.endsWith(";")) {
                    String query = sql.toString().trim();
                    query = query.substring(0, query.length() - 1).trim();
                    if (!query.isEmpty()) {
                        statement.execute(query);
                    }
                    sql.setLength(0);
                }

            }
            if (!sql.toString().trim().isEmpty()) {
                statement.execute(sql.toString().trim());


            }
        }

    }












}
