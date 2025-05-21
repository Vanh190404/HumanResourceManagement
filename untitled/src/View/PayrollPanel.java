package View;

import DAO.*;
import Model.LeaveRequest;
import Model.SalaryRecord;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class PayrollPanel extends JPanel {
    private JTextField txtSearch;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTable table;

    public PayrollPanel() {
        setLayout(new BorderLayout());

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(30);
        JButton btnSearch = new JButton("Tìm kiếm");
        searchPanel.add(new JLabel("Tìm nhân viên: "));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // Export button
        JButton btnExport = new JButton("Xuất bảng lương");
        btnExport.addActionListener(e -> exportPayrollToExcel());

        // Title and top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Bảng lương");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(lblTitle, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(btnExport, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

        // Table columns
        String[] columns = {
                "Nhân viên", "Chức vụ", "Hình thức", "Tháng", "Lương cơ bản", "Thưởng KPI", "Phạt nghỉ", "Tổng"
        };

        // Table model
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent editing
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column >= 3 && column <= 6) return Double.class;
                return String.class;
            }
        };

        // Initialize table
        table = new JTable(model);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Search functionality
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (keyword.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
            }
        });

        // Load payroll data
        loadPayrollData(model, 5, 2025); // Example: May 2025
    }

    private void loadPayrollData(DefaultTableModel model, int month, int year) {
        SalaryRecordDAO salaryDao = new SalaryRecordDAO();
        EmployeeDAO empDao = new EmployeeDAO();
        ReportDAO reportDao = new ReportDAO();
        AttendanceDAO attDao = new AttendanceDAO();
        LeaveRequestDAO leaveDao = new LeaveRequestDAO();

        DecimalFormat vndFormat = new DecimalFormat("#,### 'VNĐ'");

        List<SalaryRecord> list = salaryDao.getSalaryRecords(month, year);

        for (SalaryRecord sr : list) {
            int empId = sr.getEmployeeId();
            String name = empDao.getEmployeeNameById(empId);
            String position = empDao.getPositionById(empId);
            String employmentType = empDao.getEmploymentTypeById(empId);

            // Approved leave days
            int leaveDays = 0;
            List<LeaveRequest> leaves = leaveDao.getApprovedLeavesByEmployee(empId);
            for (LeaveRequest lr : leaves) {
                leaveDays += (int) (lr.getToDate().toEpochDay() - lr.getFromDate().toEpochDay()) + 1;
            }

            // KPI bonus
            boolean kpiAchieved = reportDao.isKPIAchieved(empId);
            double bonus = kpiAchieved ? 1_500_000.0 : 0.0;

            // Absence penalty
            int absenceDays = attDao.getAbsenceDays(empId);
            double deductions = absenceDays * 200_000.0;

            // Total salary calculation
            int totalWorkHours = sr.getTotalWorkHours();
            double total = sr.getBaseSalary() * totalWorkHours + bonus - deductions;

            model.addRow(new Object[]{
                    name,
                    position,
                    employmentType,
                    month,
                    sr.getBaseSalary(),
                    bonus,
                    deductions,
                    vndFormat.format(total)
            });
        }
    }

    private void exportPayrollToExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file bảng lương");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xlsx"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx"; // Add extension if missing
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Payroll");

                // Check table data
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int rowCount = model.getRowCount();
                if (rowCount == 0) {
                    JOptionPane.showMessageDialog(this, "Không có dữ liệu để xuất!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Create header row
                Row headerRow = sheet.createRow(0);
                String[] columnNames = {
                        "Nhân viên", "Chức vụ", "Hình thức", "Tháng",
                        "Lương cơ bản", "Thưởng KPI", "Phạt nghỉ", "Tổng"
                };

                CellStyle headerStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font font = workbook.createFont(); // POI font
                font.setBold(true); // Bold font
                headerStyle.setFont(font);

                for (int i = 0; i < columnNames.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnNames[i]);
                    cell.setCellStyle(headerStyle);
                }

                // Fill data rows
                for (int i = 0; i < rowCount; i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Cell cell = row.createCell(j);
                        Object value = model.getValueAt(i, j);
                        if (value instanceof Double) {
                            cell.setCellValue((Double) value);
                        } else if (value instanceof Integer) {
                            cell.setCellValue((Integer) value);
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                }

                // Autosize columns
                for (int i = 0; i < columnNames.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    workbook.write(fos);
                    System.out.println("File đã được lưu tại: " + filePath);
                    JOptionPane.showMessageDialog(this, "Xuất bảng lương thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất bảng lương: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}