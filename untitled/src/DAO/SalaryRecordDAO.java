package DAO;

import Model.SalaryRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaryRecordDAO {

    private int totalWorkHours;

    // Lấy tất cả bản ghi lương đã tính theo tháng/năm
    public List<SalaryRecord> getSalaryRecords(int month, int year) {
        List<SalaryRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM SalaryRecord WHERE month = ? AND year = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                SalaryRecord sr = new SalaryRecord(
                        rs.getInt("id"),
                        rs.getInt("employeeId"),
                        rs.getInt("month"),
                        rs.getInt("year"),
                        rs.getDouble("baseSalary"),
                        rs.getDouble("bonus"),
                        rs.getDouble("deductions"),
                        rs.getInt("totalWorkHours")
                );
                list.add(sr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tính lương cho từng nhân viên (demo, có thể chạy hàng tháng)
    public SalaryRecord calculateSalary(int employeeId, int month, int year) {
        double baseSalary = getEmployeeBaseSalary(employeeId);
        int absenceDays = getAbsenceDays(employeeId); // số ngày vắng
        boolean kpiAchieved = getKPI(employeeId, month, year); // true nếu đạt
        double bonus = kpiAchieved ? 1_500_000 : 0;
        double deductions = absenceDays * 200_000;
        return new SalaryRecord(0, employeeId, month, year, baseSalary, bonus, deductions,totalWorkHours);
    }

    // Lấy lương cơ bản từ bảng Employee
    private double getEmployeeBaseSalary(int employeeId) {
        String sql = "SELECT BaseSalary FROM Employee WHERE Id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble("BaseSalary");
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    // Lấy số ngày nghỉ (present != 'present') trong bảng Attendance
    private int getAbsenceDays(int employeeId) {
        String sql = "SELECT COUNT(*) AS absence FROM Attendance " +
                "WHERE EmployeeId = ? AND Present != 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("absence");
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    // Lấy KPI (bit) từ bảng KPI, trả true nếu đạt
    private boolean getKPI(int employeeId, int month, int year) {
        String sql = "SELECT Achieved FROM KPI WHERE EmployeeId = ? AND Month = ? AND Year = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, employeeId);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getBoolean("Achieved");
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
    public void updateTotalWorkHours(int empId, int totalWorkHours) {
        String sql = "UPDATE SalaryRecord SET totalWorkHours = ? WHERE employeeId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, totalWorkHours);
            stmt.setInt(2, empId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}