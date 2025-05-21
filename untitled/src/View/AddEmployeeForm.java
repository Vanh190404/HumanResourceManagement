package View;

import Model.Department;
import Model.Employee;
import Model.Position;
import Controller.MainController;
import DAO.DepartmentDAO;
import DAO.EmployeeDAO;
import DAO.PositionDAO;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AddEmployeeForm extends JFrame {
    private JTextField txtName, txtPhone, txtBirthDate, txtStartDate, txtBankName, txtAccountNumber, txtBranch;
    private JComboBox<String> cbGender;
    private JComboBox<Department> cbDepartment;
    private JComboBox<Position> cbPosition;
    private JComboBox<String> cbEmploymentType;
    private JButton btnAdd;
    private EmployeeDAO employeeDAO;
    private List<Department> departments;
    private List<Position> positions;
    private Runnable onAddSuccess;

    public AddEmployeeForm(MainController controller, Runnable onAddSuccess) {
        setTitle("Thêm nhân viên mới");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Gán callback
        this.onAddSuccess = onAddSuccess;

        // Khởi tạo DAO và các danh sách như cũ
        employeeDAO = new EmployeeDAO();
        departments = new DepartmentDAO().getAllDepartments();
        positions = new PositionDAO().getAllPositions();

        initializeUI();
    }

    private void initializeUI() {
        JPanel panelMain = new JPanel();
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));
        panelMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ========== Thông tin cá nhân ==========
        JPanel personalPanel = createPersonalPanel();
        // ========== Thông tin làm việc ==========
        JPanel workPanel = createWorkPanel();
        // ========== Thông tin ngân hàng ==========
        JPanel bankPanel = createBankPanel();
        // ========== Nút thêm ==========
        JPanel buttonPanel = createButtonPanel();

        // Thêm các panel vào giao diện
        panelMain.add(personalPanel);
        panelMain.add(Box.createVerticalStrut(10));
        panelMain.add(workPanel);
        panelMain.add(Box.createVerticalStrut(10));
        panelMain.add(bankPanel);
        panelMain.add(Box.createVerticalStrut(10));
        panelMain.add(buttonPanel);

        add(panelMain);
    }

    private JPanel createPersonalPanel() {
        JPanel personalPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        personalPanel.setBorder(BorderFactory.createTitledBorder("Thông tin cá nhân"));

        txtName = new JTextField();
        cbGender = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        txtBirthDate = new JTextField();
        txtPhone = new JTextField();

        personalPanel.add(new JLabel("Họ tên:"));
        personalPanel.add(txtName);
        personalPanel.add(new JLabel("Giới tính:"));
        personalPanel.add(cbGender);
        personalPanel.add(new JLabel("Ngày sinh (dd/MM/yyyy):"));
        personalPanel.add(txtBirthDate);
        personalPanel.add(new JLabel("Số điện thoại:"));
        personalPanel.add(txtPhone);

        return personalPanel;
    }

    private JPanel createWorkPanel() {
        JPanel workPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        workPanel.setBorder(BorderFactory.createTitledBorder("Thông tin làm việc"));

        // Load departments và positions từ DAO
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

        return workPanel;
    }

    private JPanel createBankPanel() {
        JPanel bankPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        bankPanel.setBorder(BorderFactory.createTitledBorder("Thông tin ngân hàng"));

        txtBankName = new JTextField();
        txtAccountNumber = new JTextField();
        txtBranch = new JTextField();

        bankPanel.add(new JLabel("Tên ngân hàng:"));
        bankPanel.add(txtBankName);
        bankPanel.add(new JLabel("Số tài khoản:"));
        bankPanel.add(txtAccountNumber);
        bankPanel.add(new JLabel("Chi nhánh:"));
        bankPanel.add(txtBranch);

        return bankPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("Thêm");
        btnAdd.addActionListener(e -> handleAddEmployee());
        buttonPanel.add(btnAdd);
        return buttonPanel;
    }

    private void handleAddEmployee() {
        try {
            // Kiểm tra dữ liệu
            if (txtName.getText().isEmpty() || txtBirthDate.getText().isEmpty() ||
                    txtStartDate.getText().isEmpty() || txtPhone.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!");
                return;
            }

            // Chuyển đổi ngày tháng
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dob = LocalDate.parse(txtBirthDate.getText(), formatter);
            LocalDate startDate = LocalDate.parse(txtStartDate.getText(), formatter);

            // Tạo đối tượng Employee
            Employee employee = new Employee();
            employee.setFullName(txtName.getText());
            employee.setGender((String) cbGender.getSelectedItem());
            employee.setDateOfBirth(dob);
            employee.setPhoneNumber(txtPhone.getText());
            employee.setDepartment((Department) cbDepartment.getSelectedItem());
            employee.setPosition((Position) cbPosition.getSelectedItem());
            employee.setStartDate(startDate);
            employee.setEmploymentType((String) cbEmploymentType.getSelectedItem());
            employee.setBankName(txtBankName.getText());
            employee.setAccountNumber(txtAccountNumber.getText());
            employee.setBranch(txtBranch.getText());

            // Gọi DAO để thêm vào CSDL
            boolean success = employeeDAO.addEmployee(employee);
            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                if (onAddSuccess != null) onAddSuccess.run(); // GỌI callback khi thành công
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thất bại!");
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ (dd/MM/yyyy)!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
}