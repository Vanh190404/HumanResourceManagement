package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Thông tin kết nối (thay đổi theo cấu hình của bạn)
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=HumanManangement;encrypt=true;trustServerCertificate=true";
    private static final String USER = "jv_user1";
    private static final String PASSWORD = "1234";

    // Phương thức trả về kết nối
    public static Connection getConnection() {
        try {
            // Đăng ký driver (cho SQL Server)
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi kết nối CSDL: " + e.getMessage());
        }
    }

    // Đóng kết nối
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}