package Model;

import java.time.LocalDate;

public class Attendance {
    private int id;
    private int employeeId;
    private LocalDate date;
    private boolean present;
    private int overTime;

    public Attendance() {}

    public Attendance(int id, int employeeId, LocalDate date, boolean present, int overTime) {
        this.id = id;
        this.employeeId = employeeId;
        this.date = date;
        this.present = present;
        this.overTime = overTime;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public boolean isPresent() { return present; }
    public void setPresent(boolean present) { this.present = present; }

    public int getOverTime() { return overTime; }
    public void setOverTime(int overTime) { this.overTime = overTime; }
}