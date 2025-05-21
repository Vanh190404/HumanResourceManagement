package View;

import DAO.ReportDAO;
import Model.ReportData;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReportPanel extends JPanel {

    private final JTextField txtSearch;
    private final DefaultTableModel model;

    public ReportPanel() {
        setLayout(new BorderLayout());

        // ===== Panel chính =====
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Báo cáo");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));

        // ===== Search Panel =====
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(30);
        JButton btnSearch = new JButton("Tìm kiếm");
        searchPanel.add(new JLabel("Tìm nhân viên: "));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        JButton btnExport = new JButton("Xuất báo cáo");

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(lblTitle, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(btnExport, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

        // ===== Dữ liệu bảng =====
        String[] columns = {
                "Tên nhân viên", "Phòng ban", "Ngày làm việc", "Ngày nghỉ", "Tăng ca (giờ)", "Hoàn thành KPI"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setFillsViewportHeight(true);

        // Renderer cho KPI
        table.getColumnModel().getColumn(5).setCellRenderer(new KPIRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Sự kiện tìm kiếm (lọc theo tên nhân viên)
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (keyword.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword, 0)); // cột Tên nhân viên
            }
        });

        // Sự kiện xuất báo cáo
        btnExport.addActionListener(e -> exportReport());

        // Tải dữ liệu từ cơ sở dữ liệu
        loadReportData();

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    // Renderer đổi màu theo mức KPI
    static class KPIRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            int res = (int) value;
            JLabel label = new JLabel("Đạt", SwingConstants.CENTER);
            label.setOpaque(true);
            if (res == 1) {
                label.setBackground(new Color(144, 200, 144)); // xanh lá
            } else {
                label.setText("Ko Đạt");
                label.setBackground(new Color(255, 100, 100)); // đỏ
            }
            return label;
        }
    }

    // Tải dữ liệu báo cáo từ DAO
    private void loadReportData() {
        ReportDAO reportDAO = new ReportDAO();
        List<ReportData> reportDataList = reportDAO.getEmployeeReportData();

        model.setRowCount(0); // Xóa dữ liệu cũ
        for (ReportData data : reportDataList) {
            model.addRow(new Object[]{
                    data.getEmployeeName(),
                    data.getDepartment(),
                    data.getWorkDays(),
                    data.getAbsentDays(),
                    data.getOvertimeHours(),
                    data.getKpi()
            });
        }
    }

    // Xuất báo cáo thành file Excel
    private void exportReport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file báo cáo");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xlsx"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx"; // Thêm đuôi file nếu người dùng chưa thêm
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Report");

                // Tạo tiêu đề cột
                Row headerRow = sheet.createRow(0);
                String[] columnNames = {
                        "Tên nhân viên", "Phòng ban", "Ngày làm việc", "Ngày nghỉ", "Tăng ca (giờ)", "Hoàn thành KPI"
                };

                CellStyle headerStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font font = workbook.createFont(); // Font của Apache POI
                font.setBold(true); // Đặt font in đậm
                headerStyle.setFont(font);

                for (int i = 0; i < columnNames.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnNames[i]);
                    cell.setCellStyle(headerStyle);
                }

                // Lấy dữ liệu từ bảng và ghi vào file Excel
                int rowCount = model.getRowCount();
                if (rowCount == 0) {
                    JOptionPane.showMessageDialog(this, "Không có dữ liệu để xuất!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                for (int i = 0; i < rowCount; i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Cell cell = row.createCell(j);
                        Object value = model.getValueAt(i, j);
                        if (value instanceof Integer) {
                            cell.setCellValue((Integer) value);
                        } else if (value instanceof Double) {
                            cell.setCellValue((Double) value);
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                }

                // Tự động chỉnh độ rộng cột
                for (int i = 0; i < columnNames.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                // Ghi file
                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    workbook.write(fos);
                    JOptionPane.showMessageDialog(this, "Xuất báo cáo thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất báo cáo: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}