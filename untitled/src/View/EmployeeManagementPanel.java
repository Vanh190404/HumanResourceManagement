package View;

import Controller.MainController;
import DAO.EmployeeDAO;
import Model.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeManagementPanel extends JPanel {

    private JTable employeeTable;
    private JButton btnAdd;
    private MainController controller; // KHÔNG tạo mới controller ở đây!
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private EmployeeDAO employeeDAO;
    private List<Employee> employeeList = new ArrayList<>();

    public EmployeeManagementPanel(MainController controller) {
        this.controller = controller;
        this.employeeDAO = new EmployeeDAO();

        setLayout(new BorderLayout());

        // ===== Panel chính =====
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tiêu đề
        JLabel titleLabel = new JLabel("Quản lý nhân viên");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Tạo panel tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtSearch = new JTextField(30);
        JButton btnSearch = new JButton("Tìm kiếm");
        searchPanel.add(new JLabel("Tìm nhân viên: "));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // Header gồm tiêu đề và thanh tìm kiếm
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Khởi tạo model và bảng
        model = new DefaultTableModel(new String[]{
                "Họ tên", "Phòng ban", "Công việc", "Ngày BD",
                "Hình thức", "Giới tính", "Thao tác"
        }, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Chỉ cột "Thao tác" có thể chỉnh sửa
            }
        };

        employeeTable = new JTable(model);

        // Cài đặt sorter để hỗ trợ lọc tìm kiếm
        sorter = new TableRowSorter<>(model);
        employeeTable.setRowSorter(sorter);

        // Render và editor nút trong cột "Thao tác"
        employeeTable.getColumn("Thao tác").setCellRenderer(new ButtonRenderer());
        employeeTable.getColumn("Thao tác").setCellEditor(new ButtonEditor(new JCheckBox()));

        employeeTable.setRowHeight(50);
        employeeTable.setPreferredScrollableViewportSize(new Dimension(800, 700));
        employeeTable.setFillsViewportHeight(true);
        employeeTable.getColumnModel().getColumn(6).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Nút thêm nhân viên
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("➕ Thêm nhân viên");
        topRightPanel.add(btnAdd);
        mainPanel.add(topRightPanel, BorderLayout.SOUTH);

        // Thêm sự kiện cho nút Thêm
        btnAdd.addActionListener(e -> {
            controller.showAddEForm(() -> refreshTable());
        });
        // Thêm sự kiện tìm kiếm
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (keyword.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
            }
        });

        add(mainPanel, BorderLayout.CENTER);

        // Tải dữ liệu từ cơ sở dữ liệu
        refreshTable();
    }

    // ===== Load dữ liệu từ cơ sở dữ liệu vào bảng =====
    public void refreshTable() {
        employeeList = employeeDAO.getAllEmployees();
        model.setRowCount(0);
        for (Employee emp : employeeList) {
            model.addRow(new Object[]{
                    emp.getFullName(),
                    emp.getDepartment().getName(),
                    emp.getPosition().getName(),
                    emp.getStartDate(),
                    emp.getEmploymentType(),
                    emp.getGender(),
                    ""
            });
        }
    }

    public Employee getEmployeeAtRow(int row) {
        if (row >= 0 && row < employeeList.size()) return employeeList.get(employeeTable.convertRowIndexToModel(row));
        return null;
    }

    // ===== Button Renderer để hiển thị nút Xem/Sửa/Xóa =====
    class ButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            removeAll();
            add(new JButton("Xem"));
            add(new JButton("Sửa"));
            add(new JButton("Xóa"));
            return this;
        }
    }

    // ===== Button Editor để bắt sự kiện trên các nút =====
    class ButtonEditor extends DefaultCellEditor {
        protected JPanel panel;
        protected JButton btnView, btnEdit, btnDelete;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
            btnView = new JButton("Xem");
            btnEdit = new JButton("Sửa");
            btnDelete = new JButton("Xóa");

            panel.add(btnView);
            panel.add(btnEdit);
            panel.add(btnDelete);

            btnView.addActionListener(e -> controller.handleViewEmployee(row));
            btnEdit.addActionListener(e -> controller.handleEditEmployee(row));
            btnDelete.addActionListener(e -> {
                controller.handleDeleteEmployee(row);
                refreshTable(); // Cập nhật bảng khi xóa nhân viên
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            return panel;
        }

        public Object getCellEditorValue() {
            return "";
        }
    }
}