package Model;

public class AttendanceSummary {
    private int employeeId;
    private String employeeName;
    private String departmentName;
    private int workDays;
    private int leaveDays;
    private int totalOvertime;
    private int totalWorkHours;

    public AttendanceSummary() {}

    public AttendanceSummary(int employeeId, String employeeName, String departmentName,
                             int workDays, int leaveDays, int totalOvertime, int totalWorkHours) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.departmentName = departmentName;
        this.workDays = workDays;
        this.leaveDays = leaveDays;
        this.totalOvertime = totalOvertime;
        this.totalWorkHours = totalWorkHours;
    }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public int getWorkDays() { return workDays; }
    public void setWorkDays(int workDays) { this.workDays = workDays; }

    public int getLeaveDays() { return leaveDays; }
    public void setLeaveDays(int leaveDays) { this.leaveDays = leaveDays; }

    public int getTotalOvertime() { return totalOvertime; }
    public void setTotalOvertime(int totalOvertime) { this.totalOvertime = totalOvertime; }

    public int getTotalWorkHours() { return totalWorkHours; }
    public void setTotalWorkHours(int totalWorkHours) { this.totalWorkHours = totalWorkHours; }
}