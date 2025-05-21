package View;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final JButton btnEMP;
    private final JButton btnAP;
    private final JButton btnLMP;
    private JPanel contentPanel;
    private JButton btnPayroll;
    private JButton btnReport;

    public MainFrame() {
        setTitle("PTIT HRMS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel(new GridLayout(5, 1));
        sidebar.setPreferredSize(new Dimension(200, 0));

        btnEMP = new JButton("1. Quản lý nhân viên");
        btnAP = new JButton("2. Chấm công và điểm danh");
        btnLMP = new JButton("3. Quản lý nghỉ phép");
        btnPayroll = new JButton("4. Bảng lương");
        btnReport = new JButton("5. Xem báo cáo");

        sidebar.add(btnEMP);
        sidebar.add(btnAP);
        sidebar.add(btnLMP);
        sidebar.add(btnPayroll);
        sidebar.add(btnReport);

        add(sidebar, BorderLayout.WEST);

        // Content panel để chứa các panel chức năng
        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public JButton getBtnPayroll() {
        return btnPayroll;
    }

    public JButton getBtnReport() {
        return btnReport;
    }
    public JButton getBtnEMP() {
        return btnEMP;
    }

    public JButton getBtnAP() {
        return btnAP;
    }
    public JButton getBtnLMP() {
        return btnLMP;
    }
}
