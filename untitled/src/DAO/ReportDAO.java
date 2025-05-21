package DAO;

import Model.ReportData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    public boolean isKPIAchieved(int employeeId) {
        String sql = "SELECT KPI FROM Report WHERE employeeId = ? ORDER BY id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("KPI");
            }
        } catch (SQLException e) {
            System.err.println("Error querying KPI in ReportDAO: " + e.getMessage());
        }
        return false;
    }

    public List<ReportData> getEmployeeReportData() {
        List<ReportData> reportDataList = new ArrayList<>();
        String sql = """
                SELECT 
                    e.FullName AS EmployeeName, 
                    d.Name AS DepartmentName, 
                    a.WorkDays, 
                    a.AbsentDays, 
                    a.Overtime, 
                    r.KPI
                FROM 
                    Employee e
                JOIN 
                    Attendance a ON e.Id = a.EmployeeId
                JOIN 
                    Report r ON e.Id = r.EmployeeId
                JOIN 
                    Department d ON e.DepartmentId = d.Id
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ReportData reportData = new ReportData();
                reportData.setEmployeeName(rs.getString("EmployeeName"));
                reportData.setDepartment(rs.getString("DepartmentName"));
                reportData.setWorkDays(rs.getInt("WorkDays"));
                reportData.setAbsentDays(rs.getInt("AbsentDays"));
                reportData.setOvertimeHours(rs.getInt("Overtime"));
                reportData.setKpi(rs.getInt("KPI"));
                reportDataList.add(reportData);
            }
        } catch (SQLException e) {
            System.err.println("Error querying employee report data: " + e.getMessage());
        }

        return reportDataList;
    }
}