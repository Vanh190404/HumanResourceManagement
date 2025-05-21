package View;

import DAO.LeaveRequestDAO;
import Model.LeaveRequest;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LeaveManagementPanel extends JPanel {

    private final JTextField txtSearch;
    private DefaultTableModel model;
    private JTable leaveTable;
    private TableRowSorter<DefaultTableModel> sorter;
    private List<LeaveRequest> leaveRequests;
    private LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();

    public LeaveManagementPanel() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Quản lý nghỉ phép");
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

        String[] columns = {
                "#", "Họ và tên", "Bộ phận", "Từ ngày", "Đến ngày", "Lý do nghỉ phép", "Số ngày", "Duyệt"
        };

        model = new DefaultTableModel(null, columns) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 7) return Boolean.class;
                if (columnIndex == 0 || columnIndex == 6) return Integer.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                // Chỉ cột "Duyệt" được chỉnh sửa
                return column == 7;
            }
        };

        leaveTable = new JTable(model);
        leaveTable.setRowHeight(40);
        sorter = new TableRowSorter<>(model);
        leaveTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(leaveTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (keyword.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword, 1)); // search theo tên
            }
        });

        // Lắng nghe sự kiện chỉnh sửa bảng để xử lý duyệt đơn
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 7) {
                    int row = e.getFirstRow();
                    Boolean approved = (Boolean) model.getValueAt(row, 7);
                    // Lấy LeaveRequest ID theo thứ tự trong danh sách leaveRequests
                    int leaveId = leaveRequests.get(leaveTable.convertRowIndexToModel(row)).getId();
                    // Cập nhật cơ sở dữ liệu
                    boolean success = leaveRequestDAO.updateLeaveStatus(leaveId, approved);
                    if (!success) {
                        JOptionPane.showMessageDialog(LeaveManagementPanel.this, "Cập nhật trạng thái thất bại!");
                        // Nếu thất bại, rollback lại checkbox
                        reloadRow(row);
                    } else {
                        // Cập nhật lại trạng thái đơn nghỉ trong model leaveRequests
                        leaveRequests.get(leaveTable.convertRowIndexToModel(row)).setStatus(approved ? "Approved" : "Rejected");
                    }
                }
            }
        });

        loadData();

        setVisible(true);
    }

    private void loadData() {
        model.setRowCount(0);
        leaveRequests = leaveRequestDAO.getAllLeaveRequests();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int idx = 1;
        for (LeaveRequest lr : leaveRequests) {
            String employeeName = leaveRequestDAO.getEmployeeNameById(lr.getEmployeeId());
            String department = leaveRequestDAO.getDepartmentNameByEmployeeId(lr.getEmployeeId());
            int numberOfDays = (int) (lr.getToDate().toEpochDay() - lr.getFromDate().toEpochDay()) + 1;
            boolean approved = "Approved".equalsIgnoreCase(lr.getStatus());
            model.addRow(new Object[]{
                    idx++,
                    employeeName,
                    department,
                    lr.getFromDate().format(fmt),
                    lr.getToDate().format(fmt),
                    lr.getReason(),
                    numberOfDays,
                    approved
            });
        }
    }

    // Nếu cập nhật thất bại, rollback lại trạng thái checkbox
    private void reloadRow(int row) {
        int modelRow = leaveTable.convertRowIndexToModel(row);
        boolean approved = "Approved".equalsIgnoreCase(leaveRequests.get(modelRow).getStatus());
        model.setValueAt(approved, row, 7);
    }
}