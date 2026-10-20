package com.fot.tms.config;


public final class DBConfig{
    public static final String DB_HOST = "localhost";
    public static final String DB_PORT = "3306";
    public static final String DB_NAME = "fot_tms_db";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "1234";

    public static final String BASE_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";

    public static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME +"?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";

    
    private DBConfig() {
    
    
    }


}
