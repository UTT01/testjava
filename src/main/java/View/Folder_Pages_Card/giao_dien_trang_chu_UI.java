package View.Folder_Pages_Card;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class giao_dien_trang_chu_UI extends JPanel {

    // Các thành phần UI
    private JLabel lblLogo;
    private JLabel lblTenThuongHieu;
    private JButton btnEvents;
    private JPanel pnlContent; // Khu vực hiển thị nội dung chính/Event
    private JTextArea txtGioiThieu; // Khu vực mô tả
    private JLabel lblContact;

    // Biến kiểm tra quyền quản lý
    private boolean isManager;

    // Constructor
    public giao_dien_trang_chu_UI(boolean isManager) {
        this.isManager = isManager;
        initComponents();
        setupLayout();
        loadDataFromDB(); // Giả lập load dữ liệu từ SQL bạn cung cấp
    }

    private void initComponents() {
        // 1. Phần Header (Logo + Tên + Nút)
        lblLogo = new JLabel();
        // Placeholder cho Logo (Bạn thay đường dẫn ảnh thật vào đây)
        lblLogo.setIcon(new ImageIcon("src/images/logo_placeholder.png")); 
        lblLogo.setPreferredSize(new Dimension(80, 80));
        lblLogo.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Viền tạm để dễ nhìn
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setText("LOGO");

        lblTenThuongHieu = new JLabel("TÊN THƯƠNG HIỆU");
        lblTenThuongHieu.setFont(new Font("Arial", Font.BOLD, 24));
        lblTenThuongHieu.setForeground(new Color(101, 67, 33)); // Màu nâu cà phê

        btnEvents = new JButton("Quản Lý Events");
        btnEvents.setFont(new Font("Arial", Font.BOLD, 14));
        btnEvents.setBackground(new Color(255, 153, 51));
        btnEvents.setForeground(Color.WHITE);
        btnEvents.setFocusPainted(false);
        btnEvents.setVisible(isManager); // Chỉ hiện nếu là quản lý (như yêu cầu)
        
        // Sự kiện nút Event
        btnEvents.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Mở chức năng quản lý khuyến mãi (Bảng KHUYENMAI)");
        });

        // 2. Phần Content (Sự kiện / Ảnh chính)
        pnlContent = new JPanel();
        pnlContent.setBackground(Color.WHITE);
        pnlContent.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), 
                "Chương Trình Khuyến Mãi Nổi Bật", 
                TitledBorder.CENTER, 
                TitledBorder.TOP, 
                new Font("Arial", Font.ITALIC, 16), 
                Color.RED
        ));
        pnlContent.setLayout(new BorderLayout());
        
        // Label hiển thị nội dung chính
        JLabel lblMainImage = new JLabel("<html><center><h1>GIẢM GIÁ 40% CHO NHÂN VIÊN</h1><br><h2>TRI ÂN KHÁCH HÀNG VIP GIẢM 30%</h2></center></html>");
        lblMainImage.setHorizontalAlignment(SwingConstants.CENTER);
        pnlContent.add(lblMainImage, BorderLayout.CENTER);

        // 3. Phần Mô tả (Description)
        txtGioiThieu = new JTextArea();
        txtGioiThieu.setText("Chào mừng đến với hệ thống quản lý.\nKhông gian thoáng mát, đồ uống đa dạng từ Cà phê, Trà sữa đến Sinh tố.");
        txtGioiThieu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtGioiThieu.setEditable(false);
        txtGioiThieu.setLineWrap(true);
        txtGioiThieu.setBackground(new Color(245, 245, 220)); // Màu kem nhạt
        txtGioiThieu.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 4. Phần Contact
        lblContact = new JLabel("Liên hệ: khonguyen@gmail.com | Hotline: 1900 xxxx");
        lblContact.setFont(new Font("Arial", Font.ITALIC, 12));
        lblContact.setForeground(Color.BLUE);
        lblContact.setBorder(new EmptyBorder(5, 10, 5, 10));
    }

    private void setupLayout() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Color.WHITE);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- HEADER PANEL (NORTH) ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        
        JPanel pnlBrand = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlBrand.setBackground(Color.WHITE);
        pnlBrand.add(lblLogo);
        pnlBrand.add(lblTenThuongHieu);
        
        JPanel pnlHeaderRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlHeaderRight.setBackground(Color.WHITE);
        pnlHeaderRight.add(btnEvents);

        pnlHeader.add(pnlBrand, BorderLayout.WEST);
        pnlHeader.add(pnlHeaderRight, BorderLayout.EAST);
        pnlHeader.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.SOUTH);

        // --- CENTER PANEL ---
        // Sử dụng GridBagLayout hoặc BorderLayout cho phần thân
        // Theo sketch: Content ở giữa to nhất, description ở dưới nó một chút
        JPanel pnlBody = new JPanel(new BorderLayout(0, 10));
        pnlBody.setBackground(Color.WHITE);
        pnlBody.add(pnlContent, BorderLayout.CENTER);
        
        // Phần description nằm ngay dưới ảnh sự kiện
        pnlBody.add(txtGioiThieu, BorderLayout.SOUTH);

        // --- FOOTER PANEL (SOUTH) ---
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(new Color(230, 230, 230));
        pnlFooter.add(lblContact, BorderLayout.WEST);

        // --- ADD TO MAIN PANEL ---
        this.add(pnlHeader, BorderLayout.NORTH);
        this.add(pnlBody, BorderLayout.CENTER);
        this.add(pnlFooter, BorderLayout.SOUTH);
    }

    // Hàm giả lập lấy dữ liệu từ DB (Ánh xạ với SQL bạn đưa)
    private void loadDataFromDB() {
        // Dữ liệu lấy từ bảng [THUONGHIEU] WHERE MaThuongHieu = 'TH0004'
        String dbTenThuongHieu = "Trà Đá Khôi Nguyên"; 
        String dbDiaChi = "Tây sơn Tiền hải Thái Bình";
        
        // Dữ liệu lấy từ bảng [KHUYENMAI]
        // Ví dụ: Lấy tên KM giảm giá cao nhất
        String dbKhuyenMaiHot = "<html><div style='text-align: center; color: #333;'>"
                + "<h2>SỰ KIỆN NỔI BẬT</h2>"
                + "<p style='color: red; font-size: 16px;'>Giảm 40% cho Nhân Viên</p>"
                + "<p style='color: green;'>Giảm 30% cho Khách Hàng VIP</p>"
                + "</div></html>";

        // Set Text
        lblTenThuongHieu.setText(dbTenThuongHieu.toUpperCase());
        ((JLabel)pnlContent.getComponent(0)).setText(dbKhuyenMaiHot);
        lblContact.setText("Địa chỉ: " + dbDiaChi + " | Email: contact@cafe.com");
    }
    
    // Hàm main để test giao diện độc lập
    public static void main(String[] args) {
        JFrame frame = new JFrame("Demo Giao Diện Trang Chủ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Giả sử đăng nhập là Quản lý (true) -> Hiện nút Event
        giao_dien_trang_chu_UI mainUI = new giao_dien_trang_chu_UI(true);
        
        frame.add(mainUI);
        frame.setVisible(true);
    }
}