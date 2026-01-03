
package View.BanHang;

import Constructor.*;
import View.Giao_Dien_Chinh_UI;
import database.HoaDon_Query;
import database.HoaDon_Query.ChiTietHoaDonTAM;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.*;
import javax.swing.table.*;


public class GiaoDienOder extends JFrame {
        private JComboBox<String> cbKhuVuc;
        public JComboBox cbOptionSelect;

        public DefaultComboBoxModel<String> modelComboBox;
        public DefaultComboBoxModel<String> cbOption;

        public DefaultListModel<String> listLoaiSPModel;
        private DefaultListModel<Discount> modelKhuyenMai;

        public JList<String> listLoaiSP;
        private JList<Discount> listKhuyenMai;

        public DefaultTableModel modelHoaDon;
        public DefaultTableModel modelDonTam;

        private JTable tableHoaDon;
        private JTable tableDonTam;

        private JPanel panelMonAn;
        private JScrollPane scrollSanPham;
        public JLabel lblTongTien = new JLabel();

        private JButton btnXacNhan;
        private JButton btnXoa;
        private JButton btnHuy;
        private JButton btnThanhToan;
        private JButton btnXacNhanTT;
        private JButton btnHuyTT;

        private Giao_Dien_Chinh_UI mainUI;
        private HoaDon_Query sqlHoaDon = new HoaDon_Query();

        private LocalDateTime thoiDiemXacNhan;

        private int maHD = -1;         
        private int maBan;
        private int maDSHD;

        private Integer maKMApDung = null;
        private Integer maKM = null;

        private String SttBan;
        private String MaCa;
        private String MaNv;

        private double tongTienGoc = 0;
        private double tongTienSauGiam = 0;
        private int tyLeGiamDangApDung = 0;


    public static void main(String[] args) {
    }
    public GiaoDienOder(int maBan, String MaNv,  String SttBan, String MaCa, Giao_Dien_Chinh_UI mainUI){
        this.MaNv = MaNv;
        this.maBan = maBan;
        this.SttBan = SttBan;
        this.MaCa = MaCa;
        this.mainUI = mainUI;
       setTitle("Giao Diện Oder");
       setSize(1200, 750);
       setLocationRelativeTo(null);
       setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       setLayout(new BorderLayout());
       
       taoGiaoDien();
       loadHoaDonDangMoTheoBan();
       setVisible(true);
    }
    
    private void taoGiaoDien() {
        
        JPanel main = new JPanel(new BorderLayout());

        main.add(taoPanelLoaiSP(), BorderLayout.WEST);
        main.add(taoPanelSanPham(), BorderLayout.CENTER);
        main.add(taoPanelHoaDon(), BorderLayout.EAST);

        add(taoTopBar(), BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);
        if (!listLoaiSPModel.isEmpty()) {
            listLoaiSP.setSelectedIndex(0);
        }
        
        loadSanPham(panelMonAn, 1);
        initEvent(); // ⭐ QUAN TRỌNG
    }
    
    private JPanel taoTopBar() {
    JPanel top = new JPanel(new BorderLayout());
    JLabel tenBan = new JLabel("BÀN " + SttBan, SwingConstants.CENTER);
    tenBan.setFont(new Font("Segoe UI", Font.BOLD, 20));
    top.add(tenBan, BorderLayout.CENTER);
    return top;
  }
    //LFFT
    private JPanel taoPanelLoaiSP() {

    JPanel panelMenu = new JPanel(new BorderLayout());
    panelMenu.setPreferredSize(new Dimension(180, 0));
    panelMenu.setBackground(new Color(245, 245, 245));

    listLoaiSPModel = new DefaultListModel<>();
    listLoaiSP = new JList<>(listLoaiSPModel);

    for (LoaiSP loai : sqlHoaDon.getAllLoaiSP()) {
        listLoaiSPModel.addElement(loai.getInfo());
    }

    listLoaiSP.setFont(new Font("Segoe UI", Font.BOLD, 16));
    listLoaiSP.setFixedCellHeight(45);
    listLoaiSP.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    listLoaiSP.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

    listLoaiSP.setCellRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            JLabel lbl = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);

            lbl.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
            lbl.setOpaque(true);

            if (isSelected) {
                lbl.setBackground(new Color(33, 150, 243));
                lbl.setForeground(Color.WHITE);
            } else {
                lbl.setBackground(new Color(245, 245, 245));
                lbl.setForeground(new Color(60, 60, 60));
            }
            return lbl;
        }
    });
    panelMenu.add(listLoaiSP, BorderLayout.CENTER);
    return panelMenu;
    }
    //CENTER
    private JPanel taoPanelSanPham() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        panelMonAn = new JPanel(new GridBagLayout());
        panelMonAn.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));

        scrollSanPham = new JScrollPane(panelMonAn);
        scrollSanPham.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollSanPham.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollSanPham.getVerticalScrollBar().setUnitIncrement(16);
        scrollSanPham.setBorder(BorderFactory.createEmptyBorder());

        panel.add(scrollSanPham, BorderLayout.CENTER);
        return panel;
    }
    //RIGHT
    private JPanel taoPanelHoaDon() {

        JPanel panelHoaDon = new JPanel(new BorderLayout());
        panelHoaDon.setPreferredSize(new Dimension(420, 0));
        panelHoaDon.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(220,220,220)));

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblTitle = new JLabel("HÓA ĐƠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

        cbOption = new DefaultComboBoxModel<>();
        cbOption.addElement("Không áp dụng");
        cbOption.addElement("Giảm giá");
        cbOption.addElement("Ghi chú");

        cbOptionSelect = new JComboBox<>(cbOption);
        cbOptionSelect.setPreferredSize(new Dimension(140, 30));
        
        JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        optionPanel.setOpaque(false);
        optionPanel.add(new JLabel());
        optionPanel.add(cbOptionSelect);

        // GẮN VÀO HEADER
        header.add(lblTitle, BorderLayout.WEST);
        header.add(optionPanel, BorderLayout.EAST);
        
        // ===== TABLE =====
        modelHoaDon = new DefaultTableModel(
            new String[]{"Món", "SL", "Đơn giá", "Ghi chú"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 1;
            }
        };

        tableHoaDon = new JTable(modelHoaDon);
        tableHoaDon.setRowHeight(28);
        tableHoaDon.removeColumn(tableHoaDon.getColumnModel().getColumn(3));
        tableHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tableHoaDon);

        // ===== TỔNG TIỀN =====
        JPanel panelTong = new JPanel(new BorderLayout());
        panelTong.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblTongText = new JLabel("Tổng tiền:"); 
        lblTongText.setFont(new Font("Segoe UI", Font.BOLD, 14));

        lblTongTien = new JLabel("0 đ", SwingConstants.RIGHT);
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTongTien.setForeground(new Color(220, 53, 69));
        
        JPanel tinhTien = new JPanel(new BorderLayout());
        tinhTien.add(lblTongText, BorderLayout.WEST);
        tinhTien.add(lblTongTien, BorderLayout.EAST);
        JPanel panelBtn = new JPanel(new GridLayout(1, 4, 10, 0)); 
        panelBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15)); 
        btnXacNhan = new JButton("Xác nhận"); 
        btnXoa = new JButton("Xóa"); 
        btnHuy = new JButton("Hủy"); 
        btnThanhToan = new JButton("Thanh toán"); 
        btnThanhToan.setBackground(new Color(40, 167, 69)); 
        btnThanhToan.setForeground(Color.WHITE); 
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 14));
         panelBtn.add(btnXacNhan); 
         panelBtn.add(btnXoa); 
         panelBtn.add(btnHuy); 
         panelBtn.add(btnThanhToan);
        JPanel Bottom = new JPanel(new BorderLayout());
        Bottom.add(tinhTien, BorderLayout.NORTH);
        Bottom.add(panelBtn, BorderLayout.SOUTH);
        
        
        panelTong.add(Bottom, BorderLayout.CENTER);

        
        
        panelHoaDon.add(header, BorderLayout.NORTH);
        panelHoaDon.add(scroll, BorderLayout.CENTER);
        panelHoaDon.add(panelTong, BorderLayout.SOUTH);

        return panelHoaDon;
    }
    //OPTION
    private void hienDialogGiamGia() {

     // MODEL + LIST
     DefaultListModel<Discount> modelKhuyenMai = new DefaultListModel<>();
     JList<Discount> listKhuyenMai = new JList<>(modelKhuyenMai);

     listKhuyenMai.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
     listKhuyenMai.setFixedCellHeight(40);
     listKhuyenMai.setFont(new Font("Segoe UI", Font.PLAIN, 13));

     // Renderer hiển thị đẹp
     listKhuyenMai.setCellRenderer(new DefaultListCellRenderer() {
         @Override
         public Component getListCellRendererComponent(
                 JList<?> list, Object value, int index,
                 boolean isSelected, boolean cellHasFocus) {

             Discount km = (Discount) value;

             JLabel lbl = (JLabel) super.getListCellRendererComponent(
                     list,
                     km.getTenKM() + " - Giảm " + km.getTyLeGiam() + "%",
                     index, isSelected, cellHasFocus
             );

             lbl.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
             return lbl;
         }
     });

     // LOAD DATA
     for (Discount km : sqlHoaDon.getAllKhuyenMai()) {
         modelKhuyenMai.addElement(km);
     }

     // SCROLL
     JScrollPane scrollKM = new JScrollPane(listKhuyenMai);
     scrollKM.setPreferredSize(new Dimension(380, 180));

     // PANEL CHA
     JPanel panel = new JPanel(new BorderLayout(10, 10));
     panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
     panel.add(new JLabel("Chọn khuyến mãi:"), BorderLayout.NORTH);
     panel.add(scrollKM, BorderLayout.CENTER);

     // DIALOG
     int result = JOptionPane.showConfirmDialog(
             this,
             panel,
             "Khuyến mãi",
             JOptionPane.OK_CANCEL_OPTION,
             JOptionPane.PLAIN_MESSAGE
     );

     // ⭐ XỬ LÝ KẾT QUẢ ĐÚNG NGHIỆP VỤ
     if (result == JOptionPane.OK_OPTION) {
         Discount kmChon = listKhuyenMai.getSelectedValue();
         if (kmChon != null) {

             // 1️⃣ Lưu lại để thanh toán
             maKMApDung = kmChon.getMaKM();
             tyLeGiamDangApDung = kmChon.getTyLeGiam();

             // 2️⃣ Tính lại tiền
             apDungGiamGia(kmChon);
         }
     }
 }

    private void apDungGiamGia(Discount kmChon) {
            int percent = kmChon.getTyLeGiam();
            int tong = 0;

            for (int i = 0; i < modelHoaDon.getRowCount(); i++) {
                tong += parseIntSafe(modelHoaDon.getValueAt(i, 1))
                      * parseIntSafe(modelHoaDon.getValueAt(i, 2));
            }

            int giam = tong * percent / 100;
            int sauGiam = tong - giam;

            lblTongTien.setText(
                String.format("%,d đ (-%d%%)", sauGiam, percent)
            );
        }
    //Button
    private void xoaMon(){
    
        int selectedRow = tableHoaDon.getSelectedRow();

        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Vui lòng chọn món cần xóa!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

       
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc muốn xóa món này?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        modelHoaDon.removeRow(selectedRow);

        tinhTongTien();
   

    }
    
    private void huyDon(){
        int row = tableHoaDon.getRowCount();
        boolean dongYHuy = false;
        if (row == 0){
            dongYHuy = true;
        } else {
            int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc muốn hủy hóa đơn này?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION );      
            if (confirm == JOptionPane.YES_OPTION){
                 dongYHuy = true;
            }
        }
        if(dongYHuy){
            try {
                mainUI.moGiaoDienBanHang(); // hoặc show KhuVuc
                this.dispose();
                
                this.dispose(); 
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
               
        
    }
    
    private void xacNhanDon(){
        thoiDiemXacNhan = LocalDateTime.now(); // ⭐ lấy thời điểm bấm
        createDialogHoaDon();     
    }
    //Giao diện đơn tạm
    private void createDialogHoaDon() {

    try {
        Connection conn = sqlHoaDon.getConnection();
        conn.setAutoCommit(false);

        // ================== 1️⃣ TẠO / LẤY HÓA ĐƠN ==================
        if (maHD <= 0) {
            maDSHD = sqlHoaDon.getMaDSHD(this.MaCa);

            maHD = sqlHoaDon.insertHoaDon(
                maBan,
                thoiDiemXacNhan,
                maDSHD
            );

            if (maHD <= 0) {
                throw new RuntimeException("Không tạo được hóa đơn tạm");
            }
        }

        // ================== 2️⃣ UPDATE TRẠNG THÁI BÀN ==================
        sqlHoaDon.updateTrangThaiBan(maBan, "full", tinhTongTien());

        // ================== 3️⃣ RESET + INSERT CHI TIẾT ==================
        sqlHoaDon.deleteChiTietHoaDon(maHD);

        for (int i = 0; i < modelHoaDon.getRowCount(); i++) {

            String tenSP = modelHoaDon.getValueAt(i, 0).toString();
            int maSP = sqlHoaDon.getMaSPbyName(tenSP);

            int soLuong = parseIntSafe(modelHoaDon.getValueAt(i, 1));
            int donGia  = parseIntSafe(modelHoaDon.getValueAt(i, 2));
            String ghiChu = modelHoaDon.getValueAt(i, 3).toString();

            sqlHoaDon.insertChiTietHoaDon(
                maHD,
                maSP,
                maBan,
                soLuong,
                donGia,
                ghiChu
            );
        }

        conn.commit();
        conn.setAutoCommit(true);

    } catch (Exception e) {
        try { sqlHoaDon.getConnection().rollback(); } catch (Exception ignored) {}
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tạo đơn tạm!");
        return;
    }

    // ================== UI ĐƠN TẠM (IN BẾP / QUẦY) ==================
    JDialog dialog = new JDialog(this, "Đơn tạm – In cho bếp", true);
    dialog.setSize(500, 420);
    dialog.setLocationRelativeTo(this);
    dialog.setLayout(new BorderLayout(10, 10));

    JPanel top = new JPanel();
    top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("ĐƠN TẠM");
    title.setFont(new Font("Arial", Font.BOLD, 18));
    title.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel lblBan  = new JLabel("Bàn: " + SttBan);
    JLabel lblGio  = new JLabel("Giờ vào: " + thoiDiemXacNhan.toLocalTime().withSecond(0));
    JLabel lblNgay = new JLabel("Ngày: " + thoiDiemXacNhan.toLocalDate());

    lblBan.setAlignmentX(Component.CENTER_ALIGNMENT);
    lblGio.setAlignmentX(Component.CENTER_ALIGNMENT);
    lblNgay.setAlignmentX(Component.CENTER_ALIGNMENT);

    top.add(title);
    top.add(Box.createVerticalStrut(5));
    top.add(lblBan);
    top.add(lblGio);
    top.add(lblNgay);

    // ===== TABLE =====
    modelDonTam = new DefaultTableModel(
        new String[]{"Món", "SL", "Ghi chú"}, 0
    );

    tableDonTam = new JTable(modelDonTam);
    tableDonTam.setRowHeight(28);

    loadHoaDon();

    // ===== BUTTON =====
    JButton btnInBep = new JButton("In cho bếp / Quầy");
    btnInBep.setFont(new Font("Segoe UI", Font.BOLD, 14));
    btnInBep.setBackground(new Color(40, 167, 69));
    btnInBep.setForeground(Color.WHITE);

    btnInBep.addActionListener(e -> {
        dialog.dispose();

        JOptionPane.showMessageDialog(
            this,
            "Đã gửi đơn cho bếp / quầy!",
            "Hoàn tất",
            JOptionPane.INFORMATION_MESSAGE
        );

        mainUI.moGiaoDienBanHang();
        this.dispose();
    });

    JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    bottom.add(btnInBep);

    dialog.add(top, BorderLayout.NORTH);
    dialog.add(new JScrollPane(tableDonTam), BorderLayout.CENTER);
    dialog.add(bottom, BorderLayout.SOUTH);

    dialog.setVisible(true);
}

    private void createDialogThanhToan() {

     if (maHD <= 0) {
         JOptionPane.showMessageDialog(
             this,
             "Chưa có đơn tạm!\nVui lòng xác nhận đơn trước khi thanh toán.",
             "Thiếu dữ liệu",
             JOptionPane.WARNING_MESSAGE
         );
         return;
     }

     // ===== DIALOG =====
     JDialog dialog = new JDialog(this, "Hóa đơn thanh toán", true);
     dialog.setSize(520, 560);
     dialog.setLocationRelativeTo(this);
     dialog.setLayout(new BorderLayout(10, 10));

     // ================= HEADER =================
     JPanel top = new JPanel();
     top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
     top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

     JLabel lblBrand = new JLabel("☕ COFFEE HOUSE");
     lblBrand.setFont(new Font("Arial", Font.BOLD, 20));
     lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);

     JLabel lblBan  = new JLabel("Bàn: " + SttBan);
     JLabel lblNgay = new JLabel("Ngày: " + LocalDateTime.now().toLocalDate());
     JLabel lblGio  = new JLabel("Giờ ra: " + LocalDateTime.now().toLocalTime().withSecond(0));

     lblBan.setAlignmentX(Component.CENTER_ALIGNMENT);
     lblNgay.setAlignmentX(Component.CENTER_ALIGNMENT);
     lblGio.setAlignmentX(Component.CENTER_ALIGNMENT);

     top.add(lblBrand);
     top.add(Box.createVerticalStrut(5));
     top.add(lblBan);
     top.add(lblNgay);
     top.add(lblGio);

     // ================= TABLE =================
     DefaultTableModel modelThanhToan = new DefaultTableModel(
         new String[]{"Món", "SL", "Đơn giá"}, 0
     ) {
         @Override
         public boolean isCellEditable(int r, int c) {
             return false;
         }
     };

     JTable tableThanhToan = new JTable(modelThanhToan);
     tableThanhToan.setRowHeight(26);

     JScrollPane scroll = new JScrollPane(tableThanhToan);
     scroll.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

     // ================= LOAD DATA TỪ MODEL HÓA ĐƠN =================
     double tamTinh = 0;

     for (int i = 0; i < modelHoaDon.getRowCount(); i++) {

         String tenMon = modelHoaDon.getValueAt(i, 0).toString();
         int soLuong = parseIntSafe(modelHoaDon.getValueAt(i, 1));
         int donGia  = parseIntSafe(modelHoaDon.getValueAt(i, 2));

         modelThanhToan.addRow(new Object[]{
             tenMon,
             soLuong,
             String.format("%,d đ", donGia)
         });

         tamTinh += soLuong * donGia;
     }

     // ================= FOOTER =================
     JPanel bottom = new JPanel();
     bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
     bottom.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

     JLabel lblTamTinh  = new JLabel("Tạm tính: " + String.format("%,.0f đ", tamTinh));
     JLabel lblGiamPt   = new JLabel("Giảm giá: " + tyLeGiamDangApDung + " %");

     double tienGiam = tamTinh * tyLeGiamDangApDung / 100.0;
     double tongThanhToan = tamTinh - tienGiam;

     JLabel lblGiamTien = new JLabel("Giảm giá: -" + String.format("%,.0f đ", tienGiam));

     JLabel lblTong = new JLabel(
         "TỔNG THANH TOÁN: " + String.format("%,.0f đ", tongThanhToan)
     );
     lblTong.setFont(new Font("Arial", Font.BOLD, 16));
     lblTong.setForeground(Color.RED);

     bottom.add(lblTamTinh);
     bottom.add(lblGiamPt);
     bottom.add(lblGiamTien);
     bottom.add(Box.createVerticalStrut(8));
     bottom.add(lblTong);

     // ================= BUTTON =================
     JButton btnXacNhanTT = new JButton("Xác nhận");
     JButton btnHuyTT     = new JButton("Hủy");

     btnXacNhanTT.setBackground(new Color(40, 167, 69));
     btnXacNhanTT.setForeground(Color.WHITE);
     btnXacNhanTT.setFont(new Font("Segoe UI", Font.BOLD, 14));

     JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
     panelBtn.add(btnHuyTT);
     panelBtn.add(btnXacNhanTT);

     btnXacNhanTT.addActionListener(e -> {
         dialog.dispose();
         thanhToanHoaDon(); // ⭐ CHỈ UPDATE
     });

     btnHuyTT.addActionListener(e -> dialog.dispose());

     JPanel south = new JPanel(new BorderLayout());
     south.add(bottom, BorderLayout.CENTER);
     south.add(panelBtn, BorderLayout.SOUTH);

     dialog.add(top, BorderLayout.NORTH);
     dialog.add(scroll, BorderLayout.CENTER);
     dialog.add(south, BorderLayout.SOUTH);

     dialog.setVisible(true);
 }

    private void thanhToanHoaDon() {
          
         if (maHD <= 0) {
             JOptionPane.showMessageDialog(
                 this,
                 "Chưa có đơn tạm để thanh toán!",
                 "Lỗi",
                 JOptionPane.ERROR_MESSAGE
             );
             return;
         }

         int confirm = JOptionPane.showConfirmDialog(
             this,
             "Xác nhận thanh toán hóa đơn này?",
             "Thanh toán",
             JOptionPane.YES_NO_OPTION
         );
         if (confirm != JOptionPane.YES_OPTION) return;

         try {
             Connection conn = sqlHoaDon.getConnection();
             conn.setAutoCommit(false); // ⭐ TRANSACTION

             LocalDateTime gioRa = LocalDateTime.now();
             double tongTien = tinhTongTien();
             String phuongThuc = "Tiền mặt";   // sau này cho chọn
             maKM = maKMApDung;
             sqlHoaDon.updateTrangThaiBan(maBan, "trong", 0);
             // ✅ CHỈ UPDATE – KHÔNG INSERT
            boolean ok = sqlHoaDon.updateHoaDonThanhToan(
                    maHD,
                    LocalDateTime.now(),
                    tongTien,
                    "Tiền mặt",
                    maKM
            );

             if (!ok) {
                 throw new RuntimeException("Không cập nhật được hóa đơn");
             }

             conn.commit(); // ✅ OK

             JOptionPane.showMessageDialog(
                 this,
                 "Thanh toán thành công!",
                 "Hoàn tất",
                 JOptionPane.INFORMATION_MESSAGE
             );

             // Quay về màn hình chính
                mainUI.moGiaoDienBanHang(); // hoặc show KhuVuc
                this.dispose();           

         } catch (Exception ex) {
             try {
                 sqlHoaDon.getConnection().rollback();
             } catch (Exception ignored) {}
             ex.printStackTrace();

             JOptionPane.showMessageDialog(
                 this,
                 "Lỗi khi thanh toán!",
                 "Lỗi",
                 JOptionPane.ERROR_MESSAGE
             );
         }
     }
    // Chuẩn hóa sửa số lượng
    private int parseIntSafe(Object value) {
    try {
        return Integer.parseInt(value.toString().trim());
    } catch (Exception e) {
        return 0;
    }
}
    //Event
    private void initEvent() {

    listLoaiSP.addListSelectionListener(e -> {
        if (e.getValueIsAdjusting()) return;
        int idLoai = sqlHoaDon.getidLoaiSP(listLoaiSP.getSelectedValue());
        loadSanPham(panelMonAn, idLoai);
    });

    modelHoaDon.addTableModelListener(e -> tinhTongTien());
    
    cbOptionSelect.addActionListener(e -> {
        if ("Giảm giá".equals(cbOptionSelect.getSelectedItem())) {
            hienDialogGiamGia();
        }
    });
    
    btnXoa.addActionListener(e -> xoaMon() );
    btnHuy.addActionListener(e -> huyDon());
    btnXacNhan.addActionListener(e -> xacNhanDon());
    btnThanhToan.addActionListener(e -> createDialogThanhToan());
    
    tableHoaDon.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                int row = tableHoaDon.getSelectedRow();
                if (row != -1) {
                    xuLyDoubleClick(row);
                }
            }
        }
    });
    

} 

    private double tinhTongTien() {

    tongTienGoc = 0;

    for (int i = 0; i < modelHoaDon.getRowCount(); i++) {
        int sl = parseIntSafe(modelHoaDon.getValueAt(i, 1));
        double gia = Double.parseDouble(
            modelHoaDon.getValueAt(i, 2).toString()
        );

        tongTienGoc += sl * gia;
    }

    tongTienSauGiam = tongTienGoc * (1 - tyLeGiamDangApDung / 100.0);

    lblTongTien.setText(
        String.format("%,.0f đ", tongTienSauGiam)
    );
    return tongTienSauGiam;
}

    private void loadSanPham(JPanel panelBan, int idLoaiSelect) {
    panelBan.removeAll();
   
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.anchor = GridBagConstraints.CENTER;

    int col = 0;
    int row = 0;
    int numCols = 4;

    java.util.List<SanPham> ds =
        (idLoaiSelect == 1)
        ? sqlHoaDon.getAllMonAn()
        : sqlHoaDon.getMonAn(idLoaiSelect);

    for (SanPham monBan : ds) {
        gbc.gridx = col;
        gbc.gridy = row;

        JPanel CardProduct = new JPanel(new BorderLayout());

        JButton btnProduct = new JButton( "<html><div style='text-align:center;'>" +  monBan.getTen()  + "</div></html>" );
               
               
        btnProduct.setPreferredSize(new Dimension(120, 90));
        btnProduct.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel giaBan = new JLabel(
            String.format("%,d đ", monBan.getGia()),
            SwingConstants.CENTER
        );

         btnProduct.addActionListener(e -> {
             
            String tenSP = monBan.getTen();
            int donGia = monBan.getGia();
            boolean daTonTai = false;
            for (int i = 0; i < modelHoaDon.getRowCount(); i++) {
             String tenTrongBang = modelHoaDon.getValueAt(i, 0).toString();

            if (tenTrongBang.equals(tenSP)) {
                     int soLuong = Integer.parseInt(
                         modelHoaDon.getValueAt(i, 1).toString()
                     );

                     soLuong++;
                     modelHoaDon.setValueAt(soLuong, i, 1);
                     
                     
                    
                     daTonTai = true;
                     break;
                 }
             }

            // Nếu chưa có trong bảng → thêm mới
            if (!daTonTai) {
                modelHoaDon.addRow(new Object[]{
                    tenSP,
                    1,
                    donGia,
                    ""
                    
                });
            }
            tinhTongTien();
        });
        
        
        CardProduct.add(btnProduct, BorderLayout.CENTER);
        CardProduct.add(giaBan, BorderLayout.SOUTH);

        panelBan.add(CardProduct, gbc);

        col++;
        if (col == numCols) {
            col = 0;
            row++;
        }
    }

    panelBan.revalidate();
    panelBan.repaint();
}
    
    private void loadHoaDon() {

        modelDonTam.setRowCount(0); // clear cũ nếu mở lại dialog

        for (int i = 0; i < modelHoaDon.getRowCount(); i++) {

            String ten = modelHoaDon.getValueAt(i, 0).toString();
            int soLuong = Integer.parseInt(
                    modelHoaDon.getValueAt(i, 1).toString()
            );
            String ghiChu = modelHoaDon.getValueAt(i, 3).toString();

            modelDonTam.addRow(new Object[]{
                ten,
                soLuong,
                ghiChu   // ghi chú
            });
        }
    }
 
    private void xuLyDoubleClick(int row) {

    int modelRow = tableHoaDon.convertRowIndexToModel(row);

    String tenMon = modelHoaDon.getValueAt(modelRow, 0).toString();

    JDialog dialog = new JDialog(this, "Thêm ghi chú", true);
    dialog.setSize(400, 220);
    dialog.setLocationRelativeTo(this);
    dialog.setLayout(new BorderLayout(10, 10));

    // ===== TÊN MÓN =====
    JLabel lblTenMon = new JLabel("Món: " + tenMon);
    lblTenMon.setFont(new Font("Arial", Font.BOLD, 14));
    lblTenMon.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
    dialog.add(lblTenMon, BorderLayout.NORTH);

    // ===== GHI CHÚ =====
    JTextArea txtGhiChu = new JTextArea(4, 30);
    txtGhiChu.setLineWrap(true);
    txtGhiChu.setWrapStyleWord(true);
    dialog.add(new JScrollPane(txtGhiChu), BorderLayout.CENTER);

    // ===== BUTTON =====
    JButton btnOK = new JButton("Lưu");
    JButton btnHuy = new JButton("Hủy");

    JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    bottom.add(btnHuy);
    bottom.add(btnOK);
    dialog.add(bottom, BorderLayout.SOUTH);

    btnOK.addActionListener(e -> {
        modelHoaDon.setValueAt(txtGhiChu.getText().trim(), modelRow, 3);
        dialog.dispose();
    });

    btnHuy.addActionListener(e -> dialog.dispose());

    dialog.setVisible(true);
}
    
    private void loadHoaDonDangMoTheoBan() {

        java.util.List<ChiTietHoaDonTAM> ds = 
            sqlHoaDon.getChiTietHoaDonDangMoTheoBan(maBan);

        if (ds.isEmpty()) return; // bàn chưa có hóa đơn

        modelHoaDon.setRowCount(0); // clear bảng

        for (ChiTietHoaDonTAM ct : ds) {

            // ⭐ GÁN maHD 1 LẦN
            this.maHD = ct.maHD;

            modelHoaDon.addRow(new Object[]{
                ct.tenSP,
                ct.soLuong,
                ct.donGia,
                ct.ghiChu
            });
        }
        tinhTongTien(); // cập nhật tổng tiền
}

}

