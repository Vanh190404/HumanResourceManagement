package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLServerConnection {
    public static void main(String[] args) {
        // Chuỗi kết nối JDBC
        String url = "jdbc:sqlserver://localhost:1433;databaseName=HumanResourceManagement;encrypt=true;trustServerCertificate=true";
        String user = "java_user";  // Thay bằng username của bạn (nếu dùng SQL Server Authentication)
        String password = "1111111111";  // Thay bằng mật khẩu tương ứng

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            if (connection != null) {
                System.out.println("Kết nối thành công tới SQL Server!");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi kết nối: " + e.getMessage());
        }
    }
}