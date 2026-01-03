package View.Folder_Pages_Card.hoa_don_UI;

import Constructor.Ca;
import Constructor.HoaDon;
import View.Giao_Dien_Chinh_UI;
import database.doanh_thu_sql;
import database.MyConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat; 
import java.util.ArrayList;
import java.util.Date; 
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class GiaoDienDonHang extends JPanel {
    
    private DefaultTableModel model;
    private JTable table;
    
    // Khai báo label toàn cục để có thể set text sau này
    private JLabel lblNgay;
    private JLabel lblMaCa;

    public GiaoDienDonHang() {
        // 1. Cài đặt Layout chính
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        
        // --- TẠO CONTAINER CHO PHẦN ĐẦU (HEADER) ---
        // Panel này sẽ chứa: Tiêu đề to (Ở trên) + Thông tin Ca (Ở dưới)
        JPanel pHeader = new JPanel(new BorderLayout());
        pHeader.setBackground(Color.WHITE);
        pHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Căn lề 4 phía

        // A. Tiêu đề lớn
        JLabel lblTitle = new JLabel("DANH SÁCH HÓA ĐƠN TRONG CA");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // Khoảng cách dưới tiêu đề
        
        // B. Panel Thông tin (Ngày & Mã Ca)
        JPanel pInfo = new JPanel(new BorderLayout());
        pInfo.setBackground(Color.WHITE);
        
        // B1. Label Ngày (Bên Trái)
        lblNgay = new JLabel("Ngày: " + getDate()); // Lấy ngày hiện tại
        lblNgay.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblNgay.setForeground(new Color(100, 100, 100)); // Màu xám nhẹ
        
        // B2. Label Mã Ca (Bên Phải)
        lblMaCa = new JLabel("Mã Ca: ---");
        lblMaCa.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMaCa.setForeground(new Color(200, 50, 50)); // Màu đỏ nhẹ cho nổi
        
        // Add 2 label vào pInfo
        pInfo.add(lblNgay, BorderLayout.WEST);
        pInfo.add(lblMaCa, BorderLayout.EAST);

        // Add Tiêu đề và pInfo vào pHeader
        pHeader.add(lblTitle, BorderLayout.NORTH);
        pHeader.add(pInfo, BorderLayout.CENTER);

        // Add pHeader vào giao diện chính
        this.add(pHeader, BorderLayout.NORTH);

        // --- PHẦN BẢNG (CENTER) ---
        String[] columnNames = {"Mã HĐ", "Mã Bàn", "Giờ Vào", "Giờ Ra", "Tổng Tiền", "Phương Thức"};
            model = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
    
    table = new JTable(model);
        
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(232, 232, 232));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String maHD = table.getValueAt(row, 0).toString();

                    // Tìm JFrame cha (GiaoDien) để gọi hàm chuyển cảnh
                    java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(GiaoDienDonHang.this);
                    if (window instanceof Giao_Dien_Chinh_UI) {
                        ((Giao_Dien_Chinh_UI) window).showChiTietHoaDon(maHD);
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        loadDonHang();
    }

    public void loadDonHang() {
    model.setRowCount(0);

    doanh_thu_sql getdata = new doanh_thu_sql();
    
    // 1. LẤY MÃ CA ĐANG MỞ TRỰC TIẾP TỪ DB (Không phụ thuộc vào hóa đơn)
    String maCaHienTai = getdata.getMaCaDangMo();
    
    // Cập nhật Label ngay lập tức
    lblMaCa.setText("Mã Ca: " + maCaHienTai);

    // 2. Lấy danh sách hóa đơn
    ArrayList<HoaDon> listHoaDon = getdata.getAllHoaDon();
    NumberFormat currencyFormat = NumberFormat.getNumberInstance(Locale.US);
    
    for (HoaDon hd : listHoaDon) {
        Object[] row = new Object[]{
            hd.getMaHD(),
            hd.getMaBan(),
            hd.getGioVao(),
            hd.getGioRa(),
            currencyFormat.format(hd.getTongTien()),
            hd.getPhuongThuc()
        };
        model.addRow(row);
    }
}
    
    // Hàm phụ trợ lấy ngày hiện tại format đẹp
    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
        String resultDate = sdf.format(new Date()); 

        // Câu truy vấn chỉ cần lấy cột Ngay
        String query = "SELECT Ngay FROM CA WHERE TrangThai = N'Mở Ca'";

        try (Connection con = MyConnection.connect();
             Statement stmt = (con != null) ? con.createStatement() : null;
             ResultSet rs = (stmt != null) ? stmt.executeQuery(query) : null) {

            if (con == null) {
                return resultDate; 
            }

            if (rs.next()) {
                // Lấy dữ liệu cột Ngay từ Database
                // Dùng getDate() an toàn hơn getString() để tránh lỗi định dạng SQL
                java.sql.Date sqlDate = rs.getDate("Ngay");
                if (sqlDate != null) {
                    resultDate = sdf.format(sqlDate); // Format lại thành dd/MM/yyyy
                }
            }

        } catch (Exception e) {
            System.err.println("SQL_ERROR: Lỗi lấy ngày của Ca đang mở");
            e.printStackTrace();
        }

        return resultDate; // Trả về kết quả
    }
}