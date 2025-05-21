package View;

import DAO.AttendanceDAO;
import DAO.EmployeeDAO;
import DAO.LeaveRequestDAO;
import DAO.SalaryRecordDAO;
import Model.AttendanceSummary;
import Model.Employee;
import Model.LeaveRequest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AttendancePanel extends JPanel {
    private final TableRowSorter<DefaultTableModel> sorter;
    private JTable attendanceTable;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private List<AttendanceSummary> attendanceList;
    private Employee employee;
    int totalWorkHours;
    SalaryRecordDAO salaryDao = new SalaryRecordDAO();
    public AttendancePanel() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Chấm công và điểm danh");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(30);
        JButton btnSearch = new JButton("Tìm kiếm");
        searchPanel.add(new JLabel("Tìm nhân viên: "));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        String[] columnNames = {
                "Họ tên", "Phòng ban", "Số ngày làm việc", "Số ngày nghỉ phép",
                "Giờ tăng ca", "Tổng giờ làm", "Chi tiết"
        };

        model = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        attendanceTable = new JTable(model);
        attendanceTable.getColumn("Chi tiết").setCellRenderer(new ButtonRenderer());
        attendanceTable.getColumn("Chi tiết").setCellEditor(new ButtonEditor(new JCheckBox()));
        attendanceTable.setRowHeight(50);
        sorter = new TableRowSorter<>(model);
        attendanceTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (keyword.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
            }
        });
        loadData();
    }

    private void loadData() {
        model.setRowCount(0); // Xóa dữ liệu cũ trên bảng
        AttendanceDAO attDao = new AttendanceDAO();
        LeaveRequestDAO leaveDao = new LeaveRequestDAO();
        EmployeeDAO empDao = new EmployeeDAO();
        attendanceList = attDao.getAllAttendanceSummary(); // Lấy danh sách tóm tắt chấm công

        for (AttendanceSummary att : attendanceList) {
            // 1. Tính số ngày nghỉ phép đã duyệt từ LeaveRequest
            int leaveDays = 0;
            List<LeaveRequest> leaves = leaveDao.getApprovedLeavesByEmployee(att.getEmployeeId());
            for (LeaveRequest lr : leaves) {
                leaveDays += (int) (lr.getToDate().toEpochDay() - lr.getFromDate().toEpochDay()) + 1;
            }
            att.setLeaveDays(leaveDays);

            // 2. Tính số ngày làm việc
            int workDays = att.getWorkDays(); // Số ngày làm việc hiện tại từ Attendance
            int overtime = att.getTotalOvertime(); // Số giờ tăng ca

            String employmentType = empDao.getEmploymentTypeById(att.getEmployeeId());
            int workingHoursPerDay = 8; // Mặc định fulltime
            if (employmentType != null && employmentType.equalsIgnoreCase("Part Time")) {
                workingHoursPerDay = 4;
            }

            totalWorkHours = workDays * workingHoursPerDay + overtime - leaveDays * workingHoursPerDay;
            att.setTotalWorkHours(Math.max(totalWorkHours, 0));

            attDao.updateAttendance(att.getEmployeeId(), workDays, leaveDays, overtime);

            // 4. Hiển thị dữ liệu lên bảng
            model.addRow(new Object[]{
                    att.getEmployeeName(),
                    att.getDepartmentName(),
                    att.getWorkDays(),
                    att.getLeaveDays(),
                    att.getTotalOvertime(),
                    att.getTotalWorkHours(),
                    "Xem"
            });
        }
    }

    // Renderer nút "Xem"
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setText("Xem");
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }

    // Editor nút "Xem"
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean clicked;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Xem");
            button.setOpaque(true);
            button.setBackground(new Color(30, 85, 225));
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            clicked = true;
            currentRow = row;
            return button;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                showLeaveDialog(currentRow);
            }
            clicked = false;
            return "Xem";
        }

        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        private void showLeaveDialog(int row) {
            AttendanceSummary att = attendanceList.get(attendanceTable.convertRowIndexToModel(row));
            LeaveRequestDAO dao = new LeaveRequestDAO();
            List<LeaveRequest> leaveList = dao.getApprovedLeavesByEmployee(att.getEmployeeId());

            StringBuilder sb = new StringBuilder();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            if (leaveList.isEmpty()) {
                sb.append("Không có đơn nghỉ phép được duyệt.");
            } else {
                for (LeaveRequest lr : leaveList) {
                    sb.append("Từ: ").append(lr.getFromDate().format(fmt))
                            .append(" đến ").append(lr.getToDate().format(fmt))
                            .append(" - Lý do: ").append(lr.getReason())
                            .append("\n");
                }
            }
            JTextArea area = new JTextArea(sb.toString());
            area.setEditable(false);
            area.setRows(10);
            area.setColumns(35);
            JOptionPane.showMessageDialog(button, new JScrollPane(area), "Đơn nghỉ phép của " + att.getEmployeeName(), JOptionPane.INFORMATION_MESSAGE);
        }
    }
}