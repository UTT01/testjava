package View.Folder_Pages_Card.hoa_don_UI;

import Constructor.CTHoaDon;
import database.doanh_thu_sql;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ChiTietHoaDon extends JPanel {

    private JLabel lblMaHD, lblBan, lblGioVao, lblGioRa, lblTongTien, lblPhuongThuc;
    private JTable tableChiTiet;
    private DefaultTableModel modelChiTiet;
    private JButton btnBack; // Nút quay lại

    public ChiTietHoaDon() {
        // --- 1. SETUP LAYOUT CHÍNH (BorderLayout) ---
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // =========================================================================
        // PHẦN ĐẦU (NORTH): Nút Back + Tiêu đề + Thông tin Hóa đơn
        // =========================================================================
        JPanel pNorth = new JPanel();
        pNorth.setLayout(new BoxLayout(pNorth, BoxLayout.Y_AXIS));
        pNorth.setBackground(Color.WHITE);
        pNorth.setBorder(new EmptyBorder(10, 16, 10, 16)); // Padding 2 bên

        // -- Nút Back --
        JPanel pTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pTop.setBackground(Color.WHITE);
        btnBack = new JButton("<< Quay lại");
        pTop.add(btnBack);
        pNorth.add(pTop);
        
        // -- Tiêu đề --
        JLabel lblTitle = new JLabel("CHI TIẾT HÓA ĐƠN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22)); 
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pNorth.add(Box.createVerticalStrut(10));
        pNorth.add(lblTitle);
        pNorth.add(Box.createVerticalStrut(20));

        // -- Thông tin chung --
        lblMaHD = createInfoRow(pNorth, "Mã hóa đơn:", Color.BLACK);
        lblBan = createInfoRow(pNorth, "Bàn:", new Color(211, 47, 47)); // Đỏ
        lblGioVao = createInfoRow(pNorth, "Giờ vào:", Color.BLACK);
        lblGioRa = createInfoRow(pNorth, "Giờ ra:", Color.BLACK);

        // Đường kẻ ngăn cách
        pNorth.add(Box.createVerticalStrut(10));
        pNorth.add(new JSeparator());
        
        // Add pNorth vào vùng trên cùng
        add(pNorth, BorderLayout.NORTH);

        // =========================================================================
        // PHẦN GIỮA (CENTER): Bảng món ăn (Sẽ tự động SCROLL)
        // =========================================================================
        String[] cols = {"STT", "Tên món", "SL", "Đơn giá", "Thành tiền"};
        
        // Cấm sửa dữ liệu trên bảng
        modelChiTiet = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableChiTiet = new JTable(modelChiTiet);
        
        // --- Style Bảng ---
        tableChiTiet.setRowHeight(35); // Cao hơn chút cho dễ nhìn
        tableChiTiet.setShowGrid(false); // Ẩn lưới dọc
        tableChiTiet.setIntercellSpacing(new Dimension(0, 0));
        tableChiTiet.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableChiTiet.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Căn lề phải cho Tiền
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tableChiTiet.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tableChiTiet.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        
        // Căn giữa cho STT và SL
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableChiTiet.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableChiTiet.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        // Chỉnh độ rộng cột
        tableChiTiet.getColumnModel().getColumn(0).setPreferredWidth(40);  // STT
        tableChiTiet.getColumnModel().getColumn(1).setPreferredWidth(200); // Tên món
        tableChiTiet.getColumnModel().getColumn(2).setPreferredWidth(50);  // SL

        // JScrollPane bao quanh bảng
        JScrollPane scrollPane = new JScrollPane(tableChiTiet);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16)); // Padding 2 bên cho bảng

        // QUAN TRỌNG: Add vào CENTER để tự co giãn
        add(scrollPane, BorderLayout.CENTER);

        // =========================================================================
        // PHẦN CUỐI (SOUTH): Tổng tiền & Phương thức
        // =========================================================================
        JPanel pSouth = new JPanel();
        pSouth.setLayout(new BoxLayout(pSouth, BoxLayout.Y_AXIS));
        pSouth.setBackground(Color.WHITE);
        pSouth.setBorder(new EmptyBorder(10, 16, 20, 16)); // Padding dưới đáy nhiều hơn

        pSouth.add(new JSeparator());
        pSouth.add(Box.createVerticalStrut(10));

        lblTongTien = createInfoRow(pSouth, "TỔNG TIỀN:", new Color(76, 175, 80)); // Xanh lá
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 18)); 
        
        lblPhuongThuc = createInfoRow(pSouth, "Phương thức TT:", Color.BLACK);

        // Add pSouth vào vùng dưới cùng
        add(pSouth, BorderLayout.SOUTH);
    }

    // --- Hàm tạo 1 dòng thông tin ---
    private JLabel createInfoRow(JPanel parent, String title, Color valueColor) {
        JPanel pRow = new JPanel(new BorderLayout());
        pRow.setBackground(Color.WHITE);
        pRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // Chiều cao dòng
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(new Color(51, 51, 51));

        JLabel lblValue = new JLabel("...");
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblValue.setForeground(valueColor);
        lblValue.setHorizontalAlignment(JLabel.RIGHT); 

        pRow.add(lblTitle, BorderLayout.WEST);
        pRow.add(lblValue, BorderLayout.CENTER); // Center sẽ đẩy text sang phải do setHorizontalAlignment
        
        parent.add(pRow);
        return lblValue;
    }
    
    // --- Hàm Set Dữ Liệu ---
    public void setChiTietData(String maHDInput) {
        modelChiTiet.setRowCount(0);
        
        doanh_thu_sql getdata = new doanh_thu_sql();
        ArrayList<CTHoaDon> list = getdata.getChiTietHoaDonByMaHD(maHDInput);
        
        if (list.isEmpty()) return;

        NumberFormat currency = NumberFormat.getNumberInstance(Locale.US);

        // 1. Set thông tin chung
        CTHoaDon firstItem = list.get(0);
        lblMaHD.setText("HD" + firstItem.getMahd());
        lblBan.setText("Bàn " + firstItem.getMaban());
        lblGioVao.setText(firstItem.getGiovao());
        lblGioRa.setText(firstItem.getGiora());
        lblTongTien.setText(currency.format(firstItem.getTongtien()) + " VNĐ");
        lblPhuongThuc.setText(firstItem.getPhuongthuc());

        // 2. Set danh sách món
        int stt = 1;
        for (CTHoaDon item : list) {
            modelChiTiet.addRow(new Object[]{
                stt++,
                item.getTenmon(),
                item.getSoluong(),
                currency.format(item.getDonGiaMon()),
                currency.format(item.getThanhTien())
            });
        }
    }
    
    public JButton getBtnBack() {
        return btnBack;
    }
}