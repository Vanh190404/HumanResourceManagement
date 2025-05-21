package Model;

import java.time.LocalDate;

public class LeaveRequest {
    private int id;
    private int employeeId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String reason;
    private String status; // Pending, Approved, Rejected

    public LeaveRequest() {}

    public LeaveRequest(int id, int employeeId, LocalDate fromDate, LocalDate toDate,
                        String reason, String status) {
        this.id = id;
        this.employeeId = employeeId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reason = reason;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public LocalDate getFromDate() { return fromDate; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }

    public LocalDate getToDate() { return toDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}