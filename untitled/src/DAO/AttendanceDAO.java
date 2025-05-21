package DAO;

import Model.AttendanceSummary;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceDAO {
    public List<AttendanceSummary> getAllAttendanceSummary() {
        List<AttendanceSummary> list = new ArrayList<>();
        String sql = "SELECT e.Id as EmployeeId, e.FullName, d.Name as DepartmentName, " +
                "SUM(CASE WHEN a.Present = 1 THEN 1 ELSE 0 END) as WorkDays, " +
                "SUM(CASE WHEN a.Present = 0 THEN 1 ELSE 0 END) as LeaveDays, " +
                "SUM(a.OverTime) as TotalOvertime " +
                "FROM Employee e " +
                "JOIN Department d ON e.DepartmentId = d.Id " +
                "LEFT JOIN Attendance a ON e.Id = a.EmployeeId " +
                "GROUP BY e.Id, e.FullName, d.Name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int workDays = 28 - rs.getInt("WorkDays");
                int overtime = rs.getInt("TotalOvertime");
                int totalWorkHours = workDays * 8 + overtime;
                AttendanceSummary att = new AttendanceSummary(
                        rs.getInt("EmployeeId"),
                        rs.getString("FullName"),
                        rs.getString("DepartmentName"),
                        workDays,
                        0, // leaveDays sẽ cập nhật sau
                        overtime,
                        totalWorkHours
                );
                list.add(att);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public int getAbsenceDays(int employeeId) {
        String sql = "SELECT COUNT(*) AS absence FROM Attendance " +
                "WHERE EmployeeId = ? " +
                "AND Present = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("absence");
            }
        } catch (SQLException e) {
            System.err.println("Error querying absence days in AttendanceDAO: " + e.getMessage());
        }
        return 0;
    }

    public boolean updateAttendance(int employeeId, int workDays, int absentDays, int overtime) {
        String sql = "UPDATE Attendance SET WorkDays = ?, AbsentDays = ?, Overtime = ? WHERE EmployeeId = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Thiết lập các tham số cho câu truy vấn
            stmt.setInt(1, workDays);    // Số ngày làm việc
            stmt.setInt(2, absentDays); // Số ngày nghỉ phép
            stmt.setInt(3, overtime);   // Tổng số giờ tăng ca
            stmt.setInt(4, employeeId); // ID của nhân viên

            // Thực thi câu lệnh cập nhật
            int rowsUpdated = stmt.executeUpdate();

            // Kiểm tra xem có dòng nào được cập nhật hay không
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating Attendance in AttendanceDAO: " + e.getMessage());
        }

        return false; // Trả về false nếu xảy ra lỗi
    }
}
