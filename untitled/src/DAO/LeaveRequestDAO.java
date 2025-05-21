package DAO;

import Model.LeaveRequest;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LeaveRequestDAO {
    public List<LeaveRequest> getApprovedLeavesByEmployee(int empId) {
        List<LeaveRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM LeaveRequest WHERE EmployeeId = ? AND Status = 'Approved' ORDER BY FromDate";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LeaveRequest lr = new LeaveRequest();
                    lr.setId(rs.getInt("Id"));
                    lr.setEmployeeId(empId);
                    lr.setFromDate(rs.getDate("FromDate").toLocalDate());
                    lr.setToDate(rs.getDate("ToDate").toLocalDate());
                    lr.setReason(rs.getString("Reason"));
                    lr.setStatus(rs.getString("Status"));
                    list.add(lr);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<LeaveRequest> getAllLeaveRequests() {
        List<LeaveRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM LeaveRequest ORDER BY FromDate DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                LeaveRequest lr = new LeaveRequest();
                lr.setId(rs.getInt("Id"));
                lr.setEmployeeId(rs.getInt("EmployeeId"));
                lr.setFromDate(rs.getDate("FromDate").toLocalDate());
                lr.setToDate(rs.getDate("ToDate").toLocalDate());
                lr.setReason(rs.getString("Reason"));
                lr.setStatus(rs.getString("Status"));
                list.add(lr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getEmployeeNameById(int id) {
        String name = "";
        String sql = "SELECT FullName FROM Employee WHERE Id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) name = rs.getString("FullName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    public String getDepartmentNameByEmployeeId(int empId) {
        String dept = "";
        String sql = "SELECT d.Name FROM Employee e JOIN Department d ON e.DepartmentId = d.Id WHERE e.Id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) dept = rs.getString("Name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dept;
    }
    public boolean updateLeaveStatus(int leaveId, boolean approved) {
        String sql = "UPDATE LeaveRequest SET Status = ? WHERE Id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, approved ? "Approved" : "Cancelled");
            stmt.setInt(2, leaveId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}