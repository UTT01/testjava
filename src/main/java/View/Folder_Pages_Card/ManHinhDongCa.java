/*
 * File: ManHinhDongCa.java
 */
package View.Folder_Pages_Card;

import Constructor.DoanhThu;
import Constructor.MonDaBan;
import Constructor.PhieuThuChi;
import database.doanh_thu_sql;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ManHinhDongCa extends JPanel {

    private JLabel lblNgay, lblMaCa, lblTongDoanhThu, lblTienMat, lblChuyenKhoan, lblTienTrongKet;
    private JPanel pListPhieuChi, pListPhieuThu;
    private JTable tableMonDaBan;
    private DefaultTableModel modelMon;
    
    // Nút bấm
    private JButton btnXacNhanDongCa, btnHuy;
    private JButton btnBackTop; // Nút quay lại ở góc trên
    private JPanel pSouth; // Panel chứa 2 nút dưới
    
    private String currentMaCa;

    public ManHinhDongCa() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- 1. HEADER (Thêm nút Quay lại ở đây) ---
        JPanel pNorth = new JPanel();
        pNorth.setLayout(new BoxLayout(pNorth, BoxLayout.Y_AXIS)); // Xếp dọc
        pNorth.setBackground(Color.WHITE);
        pNorth.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Dòng chứa nút Back
        JPanel pTopBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pTopBar.setBackground(Color.WHITE);
        btnBackTop = new JButton("<< Quay lại");
        pTopBar.add(btnBackTop);
        pNorth.add(pTopBar);

        // Tiêu đề
        JLabel lblTitle = new JLabel("BÁO CÁO TỔNG KẾT CA");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pNorth.add(Box.createVerticalStrut(10));
        pNorth.add(lblTitle);
        
        add(pNorth, BorderLayout.NORTH);

        // --- 2. CENTER (Nội dung cuộn - Giữ nguyên) ---
        JPanel pContent = new JPanel();
        pContent.setLayout(new BoxLayout(pContent, BoxLayout.Y_AXIS));
        pContent.setBackground(Color.WHITE);
        pContent.setBorder(new EmptyBorder(10, 20, 10, 20));

        lblNgay = createInfoRow(pContent, "Ngày:");
        lblMaCa = createInfoRow(pContent, "Ca:");
        lblTongDoanhThu = createInfoRow(pContent, "Tổng doanh thu:");
        lblTienMat = createInfoRow(pContent, "Tiền mặt:");
        lblChuyenKhoan = createInfoRow(pContent, "Chuyển khoản:");
        lblTienTrongKet = createInfoRow(pContent, "Tiền trong két:");

        pContent.add(Box.createVerticalStrut(20));

        pListPhieuChi = createSectionBlock(pContent, "Phiếu Chi", new Color(211, 47, 47), new Color(251, 233, 231));
        pContent.add(Box.createVerticalStrut(15));

        pListPhieuThu = createSectionBlock(pContent, "Phiếu Thu", new Color(56, 142, 60), new Color(232, 245, 233));
        pContent.add(Box.createVerticalStrut(20));
        
        JLabel lblMon = new JLabel("Danh sách món đã bán");
        lblMon.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblMon.setAlignmentX(Component.LEFT_ALIGNMENT);
        pContent.add(lblMon);
        pContent.add(Box.createVerticalStrut(10));

        String[] cols = {"Tên món", "Số lượng"};
        modelMon = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableMonDaBan = new JTable(modelMon);
        styleTable(tableMonDaBan);
        
        JScrollPane scrollTable = new JScrollPane(tableMonDaBan);
        scrollTable.setPreferredSize(new Dimension(0, 200));
        scrollTable.setAlignmentX(Component.LEFT_ALIGNMENT);
        pContent.add(scrollTable);

        JScrollPane scrollMain = new JScrollPane(pContent);
        scrollMain.setBorder(null);
        scrollMain.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollMain, BorderLayout.CENTER);

        // --- 3. FOOTER (2 Nút Đóng Ca & Hủy) ---
        pSouth = new JPanel(new GridLayout(1, 2, 10, 0));
        pSouth.setBorder(new EmptyBorder(10, 20, 10, 20));
        pSouth.setBackground(Color.WHITE);

        btnHuy = new JButton("Hủy bỏ");
        btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        btnXacNhanDongCa = new JButton("Xác nhận Đóng Ca");
        btnXacNhanDongCa.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnXacNhanDongCa.setBackground(new Color(216, 27, 96));
        btnXacNhanDongCa.setForeground(Color.WHITE);
        btnXacNhanDongCa.setPreferredSize(new Dimension(0, 50));

        pSouth.add(btnHuy);
        pSouth.add(btnXacNhanDongCa);
        add(pSouth, BorderLayout.SOUTH);
    }

    // --- HÀM ẨN/HIỆN NÚT ---
    public void setCheDoXem(boolean isChiXem) {
        pSouth.setVisible(!isChiXem);
    }

    // --- CÁC HÀM KHÁC GIỮ NGUYÊN ---
    private JLabel createInfoRow(JPanel parent, String title) {
        JPanel pRow = new JPanel(new BorderLayout());
        pRow.setBackground(Color.WHITE);
        pRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        pRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JLabel lblValue = new JLabel("...");
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblValue.setHorizontalAlignment(SwingConstants.RIGHT);
        pRow.add(lblTitle, BorderLayout.WEST);
        pRow.add(lblValue, BorderLayout.CENTER);
        parent.add(pRow);
        return lblValue;
    }

    private JPanel createSectionBlock(JPanel parent, String title, Color titleColor, Color bgColor) {
        JPanel pBlock = new JPanel();
        pBlock.setLayout(new BoxLayout(pBlock, BoxLayout.Y_AXIS));
        pBlock.setAlignmentX(Component.LEFT_ALIGNMENT);
        pBlock.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(titleColor);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        pBlock.add(lblTitle);
        pBlock.add(Box.createVerticalStrut(5));
        
        JPanel pContainer = new JPanel();
        
        pContainer.setLayout(new BoxLayout(pContainer, BoxLayout.Y_AXIS));
        pContainer.setBackground(bgColor);
        pContainer.setBorder(new EmptyBorder(8, 8, 8, 8));
        pContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        pContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        
        pBlock.add(pContainer);
        pBlock.setBackground(Color.WHITE);
        parent.add(pBlock);
        return pContainer;
    }
    
    private void styleTable(JTable table) {
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setMaxWidth(100);
    }

    public void setDuLieuDongCa(String maCaInput) {
        this.currentMaCa = maCaInput;
        doanh_thu_sql data = new doanh_thu_sql();
        NumberFormat currency = NumberFormat.getNumberInstance(Locale.US);

        ArrayList<DoanhThu> listDT = data.getDoanhThuByMaCa(maCaInput);
        if (!listDT.isEmpty()) {
            DoanhThu dt = listDT.get(0);
            lblNgay.setText(dt.getNgay());
            lblMaCa.setText(dt.getMaca());
            int tong = dt.getTienmat() + dt.getChuyenkhoan();
            lblTongDoanhThu.setText(currency.format(tong) + " VNĐ");
            lblTienMat.setText(currency.format(dt.getTienmat()) + " VNĐ");
            lblChuyenKhoan.setText(currency.format(dt.getChuyenkhoan()) + " VNĐ");
            lblTienTrongKet.setText(currency.format(dt.getMoket()) + " VNĐ");
        }
        loadPhieuToPanel(pListPhieuChi, maCaInput, "Chi", currency);
        loadPhieuToPanel(pListPhieuThu, maCaInput, "Thu", currency);

        modelMon.setRowCount(0);
        ArrayList<MonDaBan> listMon = data.getDanhSachMonDaBanTrongCa(maCaInput);
        for (MonDaBan m : listMon) {
            modelMon.addRow(new Object[]{m.getTenmon(), m.getSoluong()});
        }
    }

    private void loadPhieuToPanel(JPanel panel, String maCa, String loai, NumberFormat curr) {
        panel.removeAll();
        doanh_thu_sql data = new doanh_thu_sql();
        ArrayList<PhieuThuChi> list = data.getPhieuByMaCa(maCa, loai);
        if (list.isEmpty()) {
            JLabel lblEmpty = new JLabel("Không có phiếu " + loai.toLowerCase());
            lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            panel.add(lblEmpty);
        } else {
            for (PhieuThuChi p : list) {
                String text = curr.format(p.getSotien()) + " - " + p.getGhichu();
                JLabel lbl = new JLabel(text);
                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                lbl.setBorder(new EmptyBorder(2, 0, 2, 0));
                panel.add(lbl);
            }
        }
    }

    public JButton getBtnXacNhanDongCa() { return btnXacNhanDongCa; }
    public JButton getBtnHuy() { return btnHuy; }
    public JButton getBtnBackTop() { return btnBackTop; } // Getter cho nút mới
    public String getCurrentMaCa() { return currentMaCa; }
}