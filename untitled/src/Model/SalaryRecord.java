package Model;

public class SalaryRecord {
    private int id;
    private int employeeId;
    private int month;
    private int year;
    private double baseSalary;
    private double bonus;
    private double deductions;
    private int totalWorkHours;

    public SalaryRecord() {}

    public SalaryRecord(int id, int employeeId, int month, int year,
                        double baseSalary, double bonus, double deductions,int totalWorkHours) {
        this.id = id;
        this.employeeId = employeeId;
        this.month = month;
        this.year = year;
        this.baseSalary = baseSalary;
        this.bonus = bonus;
        this.deductions = deductions;
        this.totalWorkHours = totalWorkHours;
    }

    public double getNetSalary() {
        return baseSalary + bonus - deductions;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public int getTotalWorkHours() {
        return totalWorkHours;
    }
    // Getters and Setters
}
