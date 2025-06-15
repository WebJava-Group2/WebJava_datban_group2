package org.datban.webjava.helpers;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
public class DatabaseConnector {
    private static String DATABASE;
    private static String USERNAME;
    private static String PASSWORD;
    private static String DB_PORT;
    private static String DB_HOST;
    private static String DB_URL;
    static {
        try {
            Properties properties = new Properties();
            InputStream input = DatabaseConnector.class.getClassLoader().getResourceAsStream("database.properties");
            if (input != null) {
                properties.load(input);
                DATABASE = properties.getProperty("DATABASE");
                USERNAME = properties.getProperty("USERNAME");
                PASSWORD = properties.getProperty("PASSWORD");
                DB_PORT = properties.getProperty("DB_PORT");
                DB_HOST = properties.getProperty("DB_HOST");
                DB_URL = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8", DB_HOST, DB_PORT, DATABASE);
            } else {
                throw new RuntimeException("Không tìm thấy file database.properties!");
            }
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("JDBC Driver đã được tải thành công!");
        } catch (Exception e) {
            System.err.println("Lỗi khi tải driver: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            System.out.println("Kết nối thành công!");
            return connection;
        } catch (SQLException e) {
            System.err.println("Kết nối thất bại: " + e.getMessage());
            throw e;
        }
    }
}