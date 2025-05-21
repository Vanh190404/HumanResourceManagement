package Model;

public class ReportData {
    private String employeeName;    // Tên nhân viên
    private String department;      // Phòng ban
    private int workDays;           // Số ngày làm việc
    private int absentDays;         // Số ngày nghỉ
    private int overtimeHours;      // Số giờ tăng ca
    private int kpi;                // Hoàn thành KPI (phần trăm)

    // Constructor mặc định
    public ReportData() {}

    // Constructor đầy đủ
    public ReportData(String employeeName, String department, int workDays, int absentDays, int overtimeHours, int kpi) {
        this.employeeName = employeeName;
        this.department = department;
        this.workDays = workDays;
        this.absentDays = absentDays;
        this.overtimeHours = overtimeHours;
        this.kpi = kpi;
    }

    // Getters và Setters
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getWorkDays() {
        return workDays;
    }

    public void setWorkDays(int workDays) {
        this.workDays = workDays;
    }

    public int getAbsentDays() {
        return absentDays;
    }

    public void setAbsentDays(int absentDays) {
        this.absentDays = absentDays;
    }

    public int getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(int overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public int getKpi() {
        return kpi;
    }

    public void setKpi(int kpi) {
        this.kpi = kpi;
    }
}