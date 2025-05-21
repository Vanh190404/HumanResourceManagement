package Controller;

import DAO.EmployeeDAO;
import Model.Employee;
import View.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainController {
    private MainFrame mainFrame;
    private EmployeeManagementPanel empPanel;

    public MainController() {}

    public MainController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        this.mainFrame.getBtnEMP().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanel(new EmployeeManagementPanel(MainController.this));
            }
        });

        this.mainFrame.getBtnAP().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanel(new AttendancePanel());
            }
        });

        this.mainFrame.getBtnLMP().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanel(new LeaveManagementPanel());
            }
        });

        this.mainFrame.getBtnPayroll().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanel(new PayrollPanel());
            }
        });

        this.mainFrame.getBtnReport().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanel(new ReportPanel());
            }
        });

        // Load mặc định panel đầu tiên
        switchPanel(new EmployeeManagementPanel(this));
    }

    private void switchPanel(JPanel newPanel) {
        JPanel contentPanel = mainFrame.getContentPanel();
        contentPanel.removeAll();
        contentPanel.add(newPanel);
        contentPanel.revalidate();
        contentPanel.repaint();
        if (newPanel instanceof EmployeeManagementPanel) {
            empPanel = (EmployeeManagementPanel) newPanel;
        }
    }

    public void showAddEForm(Runnable onAddSuccess) {
        AddEmployeeForm addForm = new AddEmployeeForm(this, onAddSuccess);
        addForm.setVisible(true);
    }

    public void handleViewEmployee(int row) {
        if (empPanel == null) return;
        Employee emp = empPanel.getEmployeeAtRow(row);
        if (emp != null) {
            new ViewEmployeeDialog(emp);
        }
    }

    public void handleEditEmployee(int row) {
        if (empPanel == null) return;
        Employee emp = empPanel.getEmployeeAtRow(row);
        if (emp != null) {
            new EditEmployeeForm(emp, () -> empPanel.refreshTable());
        }
    }

    public void handleDeleteEmployee(int row) {
        if (empPanel == null) return;
        Employee emp = empPanel.getEmployeeAtRow(row);
        if (emp != null) {
            int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean ok = new EmployeeDAO().deleteEmployee(emp.getId());
                if (ok) {
                    JOptionPane.showMessageDialog(null, "Xóa thành công");
                    empPanel.refreshTable();
                } else {
                    JOptionPane.showMessageDialog(null, "Xóa thất bại");
                }
            }
        }
    }
}