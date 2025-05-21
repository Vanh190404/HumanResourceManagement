package View;

import Model.Employee;
import Model.Department;
import Model.Position;
import DAO.EmployeeDAO;
import DAO.DepartmentDAO;
import DAO.PositionDAO;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EditEmployeeForm extends JFrame {
    private JTextField txtEmployeeId, txtName, txtPhone, txtBirthDate;
    private JComboBox<String> cbGender;
    private JComboBox<Department> cbDepartment;
    private JComboBox<Position> cbPosition;
    private JComboBox<String> cbEmploymentType;
    private JTextField txtStartDate;
    private JTextField txtBankName, txtBankNumber, txtBankBranch;
    private JButton btnSave, btnCancel;
    private Employee employee;
    private Runnable onSaved;

    private List<Department> departments;
    private List<Position> positions;

    public EditEmployeeForm(Employee emp, Runnable onSaved) {
        this.employee = emp;
        this.onSaved = onSaved;

        setTitle("Chỉnh sửa thông tin nhân viên");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Lấy danh sách phòng ban, vị trí từ DB
        departments = new DepartmentDAO().getAllDepartments();
        positions = new PositionDAO().getAllPositions();

        JPanel panelMain = new JPanel();
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));
        panelMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ========== Thông tin cá nhân ==========
        JPanel personalPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        personalPanel.setBorder(BorderFactory.createTitledBorder("Thông tin cá nhân"));

        txtEmployeeId = new JTextField();
        txtEmployeeId.setEditable(false);

        txtName = new JTextField();
        cbGender = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        txtBirthDate = new JTextField();
        txtPhone = new JTextField();

        personalPanel.add(new JLabel("Mã nhân viên:"));
        personalPanel.add(txtEmployeeId);
        personalPanel.add(new JLabel("Họ tên:"));
        personalPanel.add(txtName);
        personalPanel.add(new JLabel("Giới tính:"));
        personalPanel.add(cbGender);
        personalPanel.add(new JLabel("Ngày sinh (dd/MM/yyyy):"));
        personalPanel.add(txtBirthDate);
        personalPanel.add(new JLabel("Số điện thoại:"));
        personalPanel.add(txtPhone);

        // ========== Thông tin làm việc ==========
        JPanel workPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        workPanel.setBorder(BorderFactory.createTitledBorder("Thông tin làm việc"));

        cbDepartment = new JComboBox<>(departments.toArray(new Department[0]));
        cbPosition = new JComboBox<>(positions.toArray(new Position[0]));
        txtStartDate = new JTextField();
        cbEmploymentType = new JComboBox<>(new String[]{"Full time", "Part time", "Remote"});

        workPanel.add(new JLabel("Phòng ban:"));
        workPanel.add(cbDepartment);
        workPanel.add(new JLabel("Vị trí:"));
        workPanel.add(cbPosition);
        workPanel.add(new JLabel("Ngày bắt đầu (dd/MM/yyyy):"));
        workPanel.add(txtStartDate);
        workPanel.add(new JLabel("Hình thức làm việc:"));
        workPanel.add(cbEmploymentType);

        // ========== Thông tin ngân hàng ==========
        JPanel bankPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        bankPanel.setBorder(BorderFactory.createTitledBorder("Thông tin ngân hàng"));

        txtBankName = new JTextField();
        txtBankNumber = new JTextField();
        txtBankBranch = new JTextField();

        bankPanel.add(new JLabel("Tên ngân hàng:"));
        bankPanel.add(txtBankName);
        bankPanel.add(new JLabel("Số tài khoản:"));
        bankPanel.add(txtBankNumber);
        bankPanel.add(new JLabel("Chi nhánh:"));
        bankPanel.add(txtBankBranch);

        // ========== Buttons ==========
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        // Sự kiện nút Lưu
        btnSave.addActionListener(e -> handleSave());

        btnCancel.addActionListener(e -> this.dispose());

        // Add tất cả panel
        panelMain.add(personalPanel);
        panelMain.add(Box.createVerticalStrut(10));
        panelMain.add(workPanel);
        panelMain.add(Box.createVerticalStrut(10));
        panelMain.add(bankPanel);
        panelMain.add(Box.createVerticalStrut(10));
        panelMain.add(buttonPanel);

        add(panelMain);

        // Đổ dữ liệu nhân viên lên form
        fillEmployeeData();

        setVisible(true);
    }

    private void fillEmployeeData() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        txtEmployeeId.setText(String.valueOf(employee.getId()));
        txtName.setText(employee.getFullName());
        txtPhone.setText(employee.getPhoneNumber());
        txtBirthDate.setText(employee.getDateOfBirth() != null ? employee.getDateOfBirth().format(fmt) : "");
        cbGender.setSelectedItem(employee.getGender());

        // Chọn đúng phòng ban, vị trí
        if (employee.getDepartment() != null) {
            for (int i = 0; i < cbDepartment.getItemCount(); i++) {
                Department dep = cbDepartment.getItemAt(i);
                if (dep.getId() == employee.getDepartment().getId()) {
                    cbDepartment.setSelectedIndex(i);
                    break;
                }
            }
        }
        if (employee.getPosition() != null) {
            for (int i = 0; i < cbPosition.getItemCount(); i++) {
                Position pos = cbPosition.getItemAt(i);
                if (pos.getId() == employee.getPosition().getId()) {
                    cbPosition.setSelectedIndex(i);
                    break;
                }
            }
        }

        txtStartDate.setText(employee.getStartDate() != null ? employee.getStartDate().format(fmt) : "");
        cbEmploymentType.setSelectedItem(employee.getEmploymentType());

        txtBankName.setText(employee.getBankName());
        txtBankNumber.setText(employee.getAccountNumber());
        txtBankBranch.setText(employee.getBranch());
    }

    private void handleSave() {
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            employee.setFullName(txtName.getText());
            employee.setGender((String) cbGender.getSelectedItem());
            employee.setDateOfBirth(LocalDate.parse(txtBirthDate.getText(), fmt));
            employee.setPhoneNumber(txtPhone.getText());
            Department dep = (Department) cbDepartment.getSelectedItem();
            Position pos = (Position) cbPosition.getSelectedItem();
            employee.setDepartment(dep);
            employee.setPosition(pos);

            employee.setStartDate(LocalDate.parse(txtStartDate.getText(), fmt));
            employee.setEmploymentType((String) cbEmploymentType.getSelectedItem());

            employee.setBankName(txtBankName.getText());
            employee.setAccountNumber(txtBankNumber.getText());
            employee.setBranch(txtBankBranch.getText());

            // Gọi DAO để update
            boolean ok = new EmployeeDAO().updateEmployee(employee);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Lưu thành công!");
                if (onSaved != null) onSaved.run();
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }
        public JTextField getTxtName() {
            return txtName;
        }
        public JTextField getTxtPhone() {
            return txtPhone;
        }
        public JTextField getTxtBirthDate() {
            return txtBirthDate;
        }
        public JComboBox<String> getCbGender() {
            return cbGender;
        }
        public JComboBox<Department> getCbDepartment() {
            return cbDepartment;
        }
        public JComboBox<Position> getCbPosition() {
            return cbPosition;
        }
        public JTextField getTxtStartDate() {
            return txtStartDate;
        }
        public JComboBox<String> getCbEmploymentType() {
            return cbEmploymentType;
        }
        public JTextField getTxtBankName() {
            return txtBankName;
        }
        public JTextField getTxtBankNumber() {
            return txtBankNumber;
        }
        public JTextField getTxtBankBranch() {
            return txtBankBranch;
        }
        public JButton getBtnSave() {
            return btnSave;
        }
        public JButton getBtnCancel() {
            return btnCancel;
        }
}
