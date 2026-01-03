package View.Folder_Pages_Card;

import Controller.thong_tin_tai_khoan_Controller;
import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class thong_tin_tai_khoan_UI extends JPanel {

    // Khai báo các component
    private JLabel lblAvatar;
    private JLabel lblTen, lblNgaySinh, lblChucVu, lblCoSo, lblEmail;
    private JButton btnDoiMatKhau;
    private JTable tblLichSu;
    private DefaultTableModel modelLichSu;
    private JLabel lblTongLuong;
    private JComboBox<String> cboThang;
    private JSpinner spnNam;

    // Controller xử lý logic cho View này
    private thong_tin_tai_khoan_Controller controller;

    public thong_tin_tai_khoan_UI(String MaNv) {
        initComponents();
        controller = new thong_tin_tai_khoan_Controller(this, MaNv);
        controller.loadData();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(0, 20));
        this.setBackground(new Color(240, 242, 245));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- PHẦN TOP: THÔNG TIN CÁ NHÂN ---
        JPanel pnlTop = new JPanel(new GridBagLayout());
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(20, 30, 20, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblAvatar = new JLabel("IMG");
        lblAvatar.setPreferredSize(new Dimension(120, 120));
        lblAvatar.setOpaque(true);
        lblAvatar.setBackground(new Color(230, 230, 230));
        lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridheight = 5;
        pnlTop.add(lblAvatar, gbc);
        gbc.gridheight = 1;

        lblTen = new JLabel("Đang tải...");
        lblNgaySinh = new JLabel("...");
        lblChucVu = new JLabel("...");
        lblCoSo = new JLabel("...");
        lblEmail = new JLabel("...");

        addProfileRow(pnlTop, "Họ và tên:", lblTen, 1, 0, true);
        addProfileRow(pnlTop, "Ngày sinh:", lblNgaySinh, 1, 1, false);
        addProfileRow(pnlTop, "Chức vụ:", lblChucVu, 1, 2, false);
        addProfileRow(pnlTop, "Cơ sở:", lblCoSo, 1, 3, false);
        addProfileRow(pnlTop, "Email:", lblEmail, 1, 4, false);

        btnDoiMatKhau = new JButton("Đổi mật khẩu");
        btnDoiMatKhau.setBackground(new Color(0, 123, 255));
        btnDoiMatKhau.setForeground(Color.WHITE);
        btnDoiMatKhau.setFocusPainted(false);
        btnDoiMatKhau.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 2; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(15, 0, 0, 0);
        pnlTop.add(btnDoiMatKhau, gbc);

        this.add(pnlTop, BorderLayout.NORTH);

        // --- PHẦN BOTTOM: LỊCH SỬ LÀM VIỆC ---
        JPanel pnlBottom = new JPanel(new BorderLayout(0, 10));
        pnlBottom.setBackground(new Color(240, 242, 245));

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlFilter.setBackground(new Color(240, 242, 245));

        JLabel lblTitle = new JLabel("Lịch sử làm việc:");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(80, 80, 80));

        String[] months = new String[12];
        for (int i = 0; i < 12; i++) months[i] = "Tháng " + (i + 1);
        cboThang = new JComboBox<>(months);
        cboThang.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboThang.setBackground(Color.WHITE);
        int currentMonth = LocalDate.now().getMonthValue();
        cboThang.setSelectedIndex(currentMonth - 1);

        int currentYear = LocalDate.now().getYear();
        spnNam = new JSpinner(new SpinnerNumberModel(currentYear, 2020, 2100, 1));
        spnNam.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spnNam, "#");
        spnNam.setEditor(editor);

        pnlFilter.add(lblTitle);
        pnlFilter.add(cboThang);
        pnlFilter.add(new JLabel("Năm:"));
        pnlFilter.add(spnNam);

        pnlBottom.add(pnlFilter, BorderLayout.NORTH);

        String[] columns = {"STT", "Ngày", "Ca", "Giờ vào", "Giờ ra", "Số giờ", "Thành tiền (VNĐ)"};
        
        modelLichSu = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa ô nào cả
            }
        };
        tblLichSu = new JTable(modelLichSu);
        tblLichSu.setRowHeight(30);
        tblLichSu.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblLichSu.getTableHeader().setBackground(new Color(230, 230, 230));

        TableColumnModel colModel = tblLichSu.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(40);  // STT
        colModel.getColumn(1).setPreferredWidth(100); // Ngày
        colModel.getColumn(2).setPreferredWidth(50);  // Ca
        colModel.getColumn(3).setPreferredWidth(80);  // Vào
        colModel.getColumn(4).setPreferredWidth(80);  // Ra
        colModel.getColumn(5).setPreferredWidth(60);  // Số giờ
        colModel.getColumn(6).setPreferredWidth(120); // Thành tiền

        JScrollPane scrollPane = new JScrollPane(tblLichSu);
        scrollPane.getViewport().setBackground(Color.WHITE);
        pnlBottom.add(scrollPane, BorderLayout.CENTER);

        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlFooter.setBackground(new Color(255, 248, 225));
        pnlFooter.setBorder(BorderFactory.createLineBorder(new Color(255, 193, 7)));

        lblTongLuong = new JLabel("Đang tính toán...");
        lblTongLuong.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTongLuong.setForeground(new Color(211, 47, 47));

        pnlFooter.add(lblTongLuong);
        pnlBottom.add(pnlFooter, BorderLayout.SOUTH);
        this.add(pnlBottom, BorderLayout.CENTER);

    
        cboThang.addItemListener(e -> controller.handleFilterChange(e));
        spnNam.addChangeListener(e -> controller.handleFilterChange(e));


        btnDoiMatKhau.addActionListener(e -> controller.xuLyDoiMatKhau());

    }

    private void addProfileRow(JPanel p, String title, JLabel lblComp, int colX, int rowY, boolean isName) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitle.setForeground(Color.GRAY);
        gbc.gridx = colX; gbc.gridy = rowY;
        p.add(lblTitle, gbc);

        if (isName) {
            lblComp.setFont(new Font("Segoe UI", Font.BOLD, 20));
            lblComp.setForeground(new Color(0, 102, 204));
        } else {
            lblComp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblComp.setForeground(Color.BLACK);
        }
        gbc.gridx = colX + 1;
        gbc.weightx = 1.0;
        p.add(lblComp, gbc);
    }

    public JLabel getLblTen() { return lblTen; }
    public JLabel getLblNgaySinh() { return lblNgaySinh; }
    public JLabel getLblChucVu() { return lblChucVu; }
    public JLabel getLblCoSo() { return lblCoSo; }
    public JLabel getLblEmail() { return lblEmail; }
    public JLabel getLblTongLuong() { return lblTongLuong; }
    public DefaultTableModel getModelLichSu() { return modelLichSu; }
    public JComboBox<String> getCboThang() { return cboThang; }
    public JSpinner getSpnNam() { return spnNam; }
}