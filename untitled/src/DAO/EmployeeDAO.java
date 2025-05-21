package DAO;

import Model.Department;
import Model.Employee;
import Model.Position;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    // SQL string
    private static final String INSERT_EMPLOYEE_SQL =
            "INSERT INTO Employee (FullName, Gender, DateOfBirth, PhoneNumber, DepartmentId, PositionId, "
                    + "StartDate, EmploymentType, BankName, AccountNumber, Branch) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_EMPLOYEES_SQL =
            "SELECT e.*, d.Name AS DepartmentName, p.Title AS PositionName " +
                    "FROM Employee e " +
                    "JOIN Department d ON e.DepartmentId = d.Id " +
                    "JOIN Position p ON e.PositionId = p.Id";

    private static final String SELECT_EMPLOYEE_BY_ID_SQL =
            "SELECT e.*, d.Name AS DepartmentName, p.Title AS PositionName " +
                    "FROM Employee e " +
                    "JOIN Department d ON e.DepartmentId = d.Id " +
                    "JOIN Position p ON e.PositionId = p.Id " +
                    "WHERE e.Id = ?";

    private static final String UPDATE_EMPLOYEE_SQL =
            "UPDATE Employee SET FullName=?, Gender=?, DateOfBirth=?, PhoneNumber=?, DepartmentId=?, PositionId=?, " +
                    "StartDate=?, EmploymentType=?, BankName=?, AccountNumber=?, Branch=? " +
                    "WHERE Id=?";

    private static final String DELETE_EMPLOYEE_SQL =
            "DELETE FROM Employee WHERE Id = ?";

    // Thêm nhân viên
    public boolean addEmployee(Employee employee) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_EMPLOYEE_SQL)) {
            statement.setString(1, employee.getFullName());
            statement.setString(2, employee.getGender());
            statement.setDate(3, Date.valueOf(employee.getDateOfBirth()));
            statement.setString(4, employee.getPhoneNumber());
            statement.setInt(5, employee.getDepartment().getId());
            statement.setInt(6, employee.getPosition().getId());
            statement.setDate(7, Date.valueOf(employee.getStartDate()));
            statement.setString(8, employee.getEmploymentType());
            statement.setString(9, employee.getBankName());
            statement.setString(10, employee.getAccountNumber());
            statement.setString(11, employee.getBranch());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    // Lấy tất cả nhân viên
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_EMPLOYEES_SQL);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                employees.add(mapEmployee(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees: " + e.getMessage());
        }
        return employees;
    }

    // Lấy tên nhân viên theo ID
    public String getEmployeeNameById(int employeeId) {
        String name = "";
        String sql = "SELECT FullName FROM Employee WHERE Id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("FullName");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employee name by id: " + e.getMessage());
        }
        return name;
    }

    // Lấy tên chức vụ theo ID
    public String getPositionById(int employeeId) {
        String position = "";
        String sql = "SELECT p.Title FROM Employee e JOIN Position p ON e.PositionId = p.Id WHERE e.Id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    position = rs.getString("Title");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching position by employee id: " + e.getMessage());
        }
        return position;
    }

    // Cập nhật nhân viên
    public boolean updateEmployee(Employee emp) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_EMPLOYEE_SQL)) {
            statement.setString(1, emp.getFullName());
            statement.setString(2, emp.getGender());
            statement.setDate(3, Date.valueOf(emp.getDateOfBirth()));
            statement.setString(4, emp.getPhoneNumber());
            statement.setInt(5, emp.getDepartment().getId());
            statement.setInt(6, emp.getPosition().getId());
            statement.setDate(7, Date.valueOf(emp.getStartDate()));
            statement.setString(8, emp.getEmploymentType());
            statement.setString(9, emp.getBankName());
            statement.setString(10, emp.getAccountNumber());
            statement.setString(11, emp.getBranch());
            statement.setInt(12, emp.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            return false;
        }
    }

    // Xóa nhân viên
    public boolean deleteEmployee(int id) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_EMPLOYEE_SQL)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
            return false;
        }
    }

    // ===== Hàm mapping từ ResultSet sang Employee =====
    private Employee mapEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getInt("Id"));
        employee.setFullName(rs.getString("FullName"));
        employee.setGender(rs.getString("Gender"));
        employee.setDateOfBirth(rs.getDate("DateOfBirth").toLocalDate());
        employee.setPhoneNumber(rs.getString("PhoneNumber"));
        employee.setStartDate(rs.getDate("StartDate").toLocalDate());
        employee.setEmploymentType(rs.getString("EmploymentType"));
        employee.setBankName(rs.getString("BankName"));
        employee.setAccountNumber(rs.getString("AccountNumber"));
        employee.setBranch(rs.getString("Branch"));

        Department department = new Department();
        department.setId(rs.getInt("DepartmentId"));
        department.setName(rs.getString("DepartmentName"));
        employee.setDepartment(department);

        Position position = new Position();
        position.setId(rs.getInt("PositionId"));
        position.setName(rs.getString("PositionName"));
        employee.setPosition(position);

        return employee;
    }
    public String getEmploymentTypeById(int employeeId) {
        String sql = "SELECT EmploymentType FROM Employee WHERE Id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("EmploymentType");
        } catch (SQLException e) { e.printStackTrace(); }
        return "";
    }
}