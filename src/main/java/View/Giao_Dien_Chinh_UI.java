package View;

import View.Folder_Pages_Card.thong_tin_nguoi_dung_UI;
import View.Folder_Pages_Card.giao_dien_trang_chu_UI;
import View.BanHang.GiaoDienKhuVuc; // Import giao diện bán hàng
import View.Folder_Pages_Card.quan_ly_nhan_su_UI;
import View.Folder_Pages_Card.thong_tin_tai_khoan_UI;
// Import các màn hình mới (Hãy sửa lại package nếu file của bạn nằm chỗ khác)
import View.Folder_Pages_Card.hoa_don_UI.GiaoDienDonHang;
import View.Folder_Pages_Card.doanh_thu_UI.GiaoDienDoanhThu;
import View.Folder_Pages_Card.hoa_don_UI.ChiTietHoaDon;
import View.Folder_Pages_Card.doanh_thu_UI.ChiTietDoanhThu;
import View.Folder_Pages_Card.ManHinhDongCa;

import Constructor.item_nhanvien;
import View.Folder_Pages_Card.cham_cong_UI;
import database.ca_lam_query;
import database.doanh_thu_sql;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * Giao diện chính - Tích hợp Logic và UI trong cùng 1 file
 */
public class Giao_Dien_Chinh_UI extends JFrame {

    public String MaNv, MaCa;
    public String TenNV = "TuTu";
    public String ChucVu = "NhanVien";
    public String gmail = "abcxyz@gmail.com";
    public String MaThuongHieu = "khoinguyen";
    
    private JList<String> listMenu;
    public DefaultListModel<String> listMenuModel;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Khai báo các màn hình con
    private GiaoDienDonHang panelDonHang;
    private GiaoDienDoanhThu panelDoanhThu;
    private ChiTietHoaDon panelChiTiet;
    private ChiTietDoanhThu panelChiTietDoanhThu;
    private ManHinhDongCa panelDongCa;

    public Giao_Dien_Chinh_UI(String MaNv) {
        this.MaNv = MaNv;
       // this.MaCa = maCa;
        loadUserInfo();
        this.setTitle("Hệ Thống Quản Lý Cafe - Khu Vực: " + this.MaNv);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.setSize(1200, 750);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GiaoDien();
        khoiTaoDuLieuCaLam();
        
    }

    public void loadUserInfo() {
        if (this.MaNv != null && !this.MaNv.isEmpty()) {
            item_nhanvien info = database.nhan_vien_query.getThongTinNhanVien(MaNv);

            if (info != null) {
                try {
                    this.TenNV = (info.getTenNV() != null) ? (String) info.getTenNV() : "Chưa cập nhật";
                    this.ChucVu = (info.getChucVu() != null) ? (String) info.getChucVu() : "NhanVien";
                    this.gmail = (info.getEmail() != null) ? (String) info.getEmail() : "";
                    this.MaThuongHieu = (info.getMaThuongHieu() != null) ? (String) info.getMaThuongHieu() : "";
                } catch (Exception e) {
                    System.out.println("Lỗi khi ép kiểu dữ liệu nhân viên: " + e.getMessage());
                }
            } else {
                this.TenNV = "Không tìm thấy NV";
            }
        }
    }
    
    public void GiaoDien() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // --- MENU TRÁI ---
        JPanel panelMenu = new JPanel(new BorderLayout());
        panelMenu.setPreferredSize(new Dimension(180, 0));
        panelMenu.setBackground(new Color(245, 245, 245));

        thong_tin_nguoi_dung_UI pnlUser = new thong_tin_nguoi_dung_UI(this.TenNV, this.ChucVu, this.MaNv);
        panelMenu.add(pnlUser, BorderLayout.NORTH);

        listMenuModel = new DefaultListModel<>();
        listMenu = new JList<>(listMenuModel);

        // Thêm item vào Menu
        listMenuModel.addElement("Trang chủ");
        if (this.MaNv.startsWith("QL") || this.ChucVu.equalsIgnoreCase("Quản Lý")) {
            listMenuModel.addElement("Quản Lý Nhân Sự");
        }
        listMenuModel.addElement("Lịch làm");
        listMenuModel.addElement("Báo cáo");
        listMenuModel.addElement("Sản phẩm");
        listMenuModel.addElement("Bán hàng");
        listMenuModel.addElement("Đơn hàng"); // Mới
        listMenuModel.addElement("Doanh thu"); // Mới
        listMenuModel.addElement("Tài khoản");
        listMenuModel.addElement("Đăng xuất");

        setupListMenuDesign(); // Cài đặt giao diện List Menu
        panelMenu.add(listMenu, BorderLayout.CENTER);
        add(panelMenu, BorderLayout.WEST);

        // --- KHỞI TẠO CÁC MÀN HÌNH CON & LOGIC SỰ KIỆN ---
        initSubPanelsAndEvents();

        // --- ADD CÁC MÀN HÌNH VÀO CARDLAYOUT ---
        mainPanel.add(createDummyPanel("Giao diện Trang Chủ"), "Trang chủ");
        mainPanel.add(createDummyPanel("Quản lý Sản Phẩm"), "Sản phẩm");
        mainPanel.add(createDummyPanel("Xem Báo Cáo"), "Báo cáo");
        
        // Add các màn hình chức năng đã khởi tạo
        mainPanel.add(panelDonHang, "Đơn hàng");
        mainPanel.add(panelDoanhThu, "Doanh thu");
        mainPanel.add(panelChiTiet, "ChiTiet");
        mainPanel.add(panelChiTietDoanhThu, "ChiTietDoanhThu");
        mainPanel.add(panelDongCa, "ManHinhDongCa");
        
        // Tài khoản mặc định (dummy), khi click sẽ load cái thật
        mainPanel.add(createDummyPanel("Tài khoản loading..."), "Tài khoản dummy");

        this.add(mainPanel, BorderLayout.CENTER);

        // --- XỬ LÝ SỰ KIỆN CLICK MENU ---
        listMenu.addListSelectionListener(((e) -> {
            if (!e.getValueIsAdjusting()) {
                String valueSelect = (String) listMenu.getSelectedValue();
                if (valueSelect == null) return;

                if (valueSelect.equals("Đăng xuất")) {
                    xuLyDangXuat();
                    return;
                }
                if (valueSelect.equals("Bán hàng")) {
                    xuLyVaoBanHang();
                    return;
                }
                if (valueSelect.equals("Doanh thu")) {
                    panelDoanhThu.loadDoanhThu(); 
                }
                if (valueSelect.equals("Quản Lý Nhân Sự")) {
                    mainPanel.add(new quan_ly_nhan_su_UI(this.MaNv), "Quản Lý Nhân Sự");
                }
                if (valueSelect.equals("Tài khoản")) {
                    mainPanel.add(new thong_tin_tai_khoan_UI(this.MaNv), "Tài khoản");
                }
                if (valueSelect.equals("Lịch làm")) {
                    mainPanel.add(new cham_cong_UI(this.MaNv), "Lịch làm");
                }
                if(valueSelect.equals("Trang chủ")){
                    mainPanel.add(new giao_dien_trang_chu_UI(true),"Trang chủ");
                }
                
                // Hiển thị card tương ứng
                cardLayout.show(mainPanel, valueSelect);
            }
        }));
        
     
        listMenu.setSelectedIndex(0);
        cardLayout.show(mainPanel, "Trang chủ");
    }
    private void khoiTaoDuLieuCaLam() {
        new Thread(() -> {
            try {
                // 1. Xác định thứ 2 của tuần hiện tại
                LocalDate today = LocalDate.now();
                LocalDate thisMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

                // 2. Tạo lịch cho tuần này
               database.ca_lam_query.taoLichCaMacDinhChoTuan(thisMonday);
               // MaCa = ca_lam_query.getMaCaDangMo(MaNv, ngay, caHienTai);
                // 3. Tạo sẵn lịch cho tuần sau (để nhân viên có thể đăng ký trước)
                LocalDate nextMonday = thisMonday.plusWeeks(1);
                database.ca_lam_query.taoLichCaMacDinhChoTuan(nextMonday);
                
                System.out.println("Đã kiểm tra và khởi tạo lịch ca làm việc.");
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    // --- HÀM KHỞI TẠO CÁC PANEL CON VÀ GẮN SỰ KIỆN ---
    private void initSubPanelsAndEvents() {
        panelDonHang = new GiaoDienDonHang();
        panelDoanhThu = new GiaoDienDoanhThu();
        panelChiTiet = new ChiTietHoaDon();
        panelChiTietDoanhThu = new ChiTietDoanhThu();
        panelDongCa = new ManHinhDongCa();

        // 1. Sự kiện màn hình Đóng Ca
        panelDongCa.getBtnHuy().addActionListener(e -> {
            showChiTietDoanhThu(panelDongCa.getCurrentMaCa(), "Mở"); // Quay lại
        });

        panelDongCa.getBtnBackTop().addActionListener(e -> {
            cardLayout.show(mainPanel, "Doanh thu");
        });

        panelDongCa.getBtnXacNhanDongCa().addActionListener(e -> {
            xuLyDongCa();
        });

        // 2. Sự kiện Chi Tiết Hóa Đơn
        panelChiTiet.getBtnBack().addActionListener(evt -> {
            cardLayout.show(mainPanel, "Đơn hàng");
        });

        // 3. Sự kiện Chi Tiết Doanh Thu
        panelChiTietDoanhThu.getBtnBack().addActionListener(e -> {
            cardLayout.show(mainPanel, "Doanh thu");
        });

        panelChiTietDoanhThu.getBtnDongCa().addActionListener(e -> {
            String maCa = panelChiTietDoanhThu.getMaCaHienTai();
            showManHinhDongCa(maCa, false); // Chuyển sang màn hình xác nhận
        });
    }

    // --- CÁC HÀM XỬ LÝ NGHIỆP VỤ ---

    private void xuLyDongCa() {
        String maCa = panelDongCa.getCurrentMaCa();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn đóng ca " + maCa + " không?\nHành động này không thể hoàn tác!",
                "Xác nhận đóng ca", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            doanh_thu_sql data = new doanh_thu_sql();
            if (data.dongCa(maCa)) {
                JOptionPane.showMessageDialog(this, "Đóng ca thành công!");
                
                // Load lại dữ liệu
                panelDoanhThu.loadDoanhThu();
                panelDonHang.loadDonHang();
                
                // Về trang chủ
                cardLayout.show(mainPanel, "Trang chủ");
                listMenu.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi đóng ca!");
            }
        }
    }

    public void showChiTietHoaDon(String maHD) {
        panelChiTiet.setChiTietData(maHD);
        cardLayout.show(mainPanel, "ChiTiet");
    }

    public void showChiTietDoanhThu(String maCa, String trangThai) {
        boolean isMo = trangThai.toLowerCase().contains("mở");
        if (isMo) {
            panelChiTietDoanhThu.setDoanhThuData(maCa, trangThai);
            cardLayout.show(mainPanel, "ChiTietDoanhThu");
        } else {
            showManHinhDongCa(maCa, true);
        }
    }

    public void showManHinhDongCa(String maCa, boolean cheDoXem) {
        panelDongCa.setDuLieuDongCa(maCa);
        panelDongCa.setCheDoXem(cheDoXem);
        cardLayout.show(mainPanel, "ManHinhDongCa");
    }

    private void xuLyDangXuat() {
        int check = JOptionPane.showConfirmDialog(this,
                "Bạn Có Muốn Đăng Xuất Không", "Xác Nhận",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (check == JOptionPane.OK_OPTION) {
            this.dispose();
            new View.Folder_Login_UI.loginUI().setVisible(true); // Sửa đường dẫn nếu cần
        }
    }

            private void xuLyVaoBanHang() {

            String maCaDangMo = ca_lam_query.getMaCaDangMo();

            if (maCaDangMo != null) {
                this.MaCa = maCaDangMo;   // ⭐ DÒNG QUAN TRỌNG NHẤT
                moGiaoDienBanHang();
                return;
            }

            // --- nếu chưa mở ca thì xử lý mở ca ---
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            String caHienTai = "";

            if (hour >= 0 && hour < 12) caHienTai = "Sáng";
            else if (hour >= 12 && hour < 18) caHienTai = "Chiều";
            else if (hour >= 18 && hour <= 23) caHienTai = "Tối";
            else {
                JOptionPane.showMessageDialog(this,
                    "Ngoài giờ làm việc!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            hienThiDialogMoCa(caHienTai, cal.getTime());
        }

    
    // --- 3. HIỂN THỊ DIALOG NHẬP TIỀN ĐẦU CA ---
    private void hienThiDialogMoCa(String ca, Date ngay) {
        JPanel pnlInput = new JPanel(new GridLayout(3, 1, 5, 5));
        JLabel lblTitle = new JLabel("<html><b>Ca làm việc hiện tại chưa mở!</b><br>Vui lòng nhập tiền đầu ca để bắt đầu:</html>");
        JTextField txtTienKet = new JTextField();
        
        pnlInput.add(lblTitle);
        pnlInput.add(new JLabel("Tiền két ban đầu (VNĐ):"));
        pnlInput.add(txtTienKet);

        int result = JOptionPane.showConfirmDialog(this, pnlInput, 
                "Mở Ca " + ca, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String strTien = txtTienKet.getText().trim();
            try {
                if (strTien.isEmpty()) throw new NumberFormatException();
                
     
                strTien = strTien.replace(",", "").replace(".", "");
                int tienKet = Integer.parseInt(strTien);

                if (tienKet < 0) {
                    JOptionPane.showMessageDialog(this, "Tiền không được âm!");
                    return;
                }

                // Gọi hàm update vào DB
                boolean success = database.ca_lam_query.thucHienMoCa(this.MaNv, ngay, ca, tienKet);

                if (success) {
                    this.MaCa = ca_lam_query.getMaCaDangMo(); // ⭐ lấy MaCa vừa mở
                    
                    JOptionPane.showMessageDialog(this, "Mở ca thành công! Đang vào bán hàng...");
                    moGiaoDienBanHang();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi hệ thống: Không thể mở ca.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền hợp lệ (Ví dụ: 1000000)!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
              
                hienThiDialogMoCa(ca, ngay);
            }
        }
    }

    public void moGiaoDienBanHang() {
        mainPanel.add(new GiaoDienKhuVuc(this.MaNv, this.MaCa, this), "Bán hàng");
        cardLayout.show(mainPanel, "Bán hàng");
        
        // Update selection Menu
        int index = listMenuModel.indexOf("Bán hàng");
        if (index != -1) listMenu.setSelectedIndex(index);
    }

    private JPanel createDummyPanel(String text) {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 30));
        p.add(lbl);
        return p;
    }
    
    // Setup giao diện List Menu đẹp
    private void setupListMenuDesign() {
        listMenu.setFont(new Font("Segoe UI", Font.BOLD, 16));
        listMenu.setFixedCellHeight(45);
        listMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listMenu.setBackground(new Color(245, 245, 245));
        listMenu.setForeground(new Color(50, 50, 50));
        listMenu.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        listMenu.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
                lbl.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

                if (isSelected) {
                    lbl.setBackground(new Color(33, 150, 243));
                    lbl.setForeground(Color.white);
                    lbl.setOpaque(true);
                } else {
                    lbl.setBackground(new Color(245, 245, 245));
                    lbl.setForeground(new Color(60, 60, 60));
                    lbl.setOpaque(true);
                }
                return lbl;
            }
        });
    }
   
    
}