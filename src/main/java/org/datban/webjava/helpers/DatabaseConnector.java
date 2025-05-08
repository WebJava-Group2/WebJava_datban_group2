//package org.datban.webjava.helpers;
//
//import java.io.InputStream;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.Properties;
//
//public class DatabaseConnector {
//    private static String DATABASE;
//    private static String USERNAME;
//    private static String PASSWORD;
//    private static String DB_PORT;
//    private static String DB_HOST;
//    private static String DB_URL;
//
//    static {
//        try {
//            Properties properties = new Properties();
//            // Đọc file application.pro
//            InputStream input = DatabaseConnector.class.getClassLoader().getResourceAsStream("database.properties");
//            if (input != null) {
//                properties.load(input);
//                DATABASE = properties.getProperty("DATABASE");
//                USERNAME = properties.getProperty("USERNAME");
//                PASSWORD = properties.getProperty("PASSWORD");
//                DB_PORT = properties.getProperty("DB_PORT");
//                DB_HOST = properties.getProperty("DB_HOST");
//
//            } else {
//                throw new RuntimeException("Không tìm thấy file db.properties!");
//            }
//
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            System.out.println("JDBC Driver loaded!");
//
//        } catch (Exception e) {
//            System.err.println("Lỗi khi tải driver: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    public static Connection getConnection() throws SQLException {
//        Connection connection = null;
//        try {
//            // Cố gắng kết nối đến cơ sở dữ liệu
//            connection = DriverManager.getConnection(URL, USER, PASSWORD);
//            System.out.println("Connection successful!");
//        } catch (SQLException e) {
//            // In thông báo lỗi nếu kết nối thất bại
//            System.out.println("Connection failed: " + e.getMessage());
//            e.printStackTrace(); // In chi tiết lỗi
//        }
//        return connection;
//    }
//}