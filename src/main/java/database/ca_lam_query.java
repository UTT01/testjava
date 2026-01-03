/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import Constructor.item_nhanvien;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
/**
 *
 * @author khoin
 */
public class ca_lam_query {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final String[] TEN_CA = {"Sáng", "Chiều", "Tối"};
    
    private static String convertCaToString(int ca) {
        if (ca >= 1 && ca <= 3) return TEN_CA[ca - 1];
        return "Sáng";
    }
    
    public static String taoMaCa(java.util.Date ngay, int ca) {
        String prefix = "S";
        if (ca == 2) prefix = "C";
        if (ca == 3) prefix = "T";
        java.text.SimpleDateFormat sdfMa = new java.text.SimpleDateFormat("ddMMyy");
        return prefix + sdfMa.format(ngay);
    }
        public static String getMaCaDangMo(Date ngay, String tenCa) {
        String maCa = null;
        String sql = """
            SELECT TOP 1 MaCa
            FROM CA
            WHERE Ngay = ?
              AND Ca = ?
              AND TrangThai = N'Mở Ca'
        """;

        try (Connection conn = MyConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(ngay.getTime()));
            ps.setString(2, tenCa);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                maCa = rs.getString("MaCa");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return maCa; // null nếu chưa mở ca
    }

    public static String taoLichCaMacDinhChoTuan(java.time.LocalDate monday) {
        String maCa="";
        String sql = "IF NOT EXISTS (SELECT 1 FROM Ca WHERE MaCa = ?) INSERT INTO Ca (MaCa, Ngay, Ca) VALUES (?, ?, ?)";
        Connection conn = null;
        try {
            conn = database.MyConnection.connect();
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                java.text.SimpleDateFormat sdfSQL = new java.text.SimpleDateFormat("yyyy-MM-dd");
                for (int i = 0; i < 7; i++) {
                    java.time.LocalDate currentDay = monday.plusDays(i);
                    java.util.Date utilDate = java.sql.Date.valueOf(currentDay);
                    String ngayStr = sdfSQL.format(utilDate);
                    for (int ca = 1; ca <= 3; ca++) {
                        maCa = taoMaCa(utilDate, ca); 
                        String tenCa = (ca == 1) ? "Sáng" : (ca == 2 ? "Chiều" : "Tối");
                        pstmt.setString(1, maCa);
                        pstmt.setString(2, maCa);
                        pstmt.setString(3, ngayStr);
                        pstmt.setString(4, tenCa);
                        pstmt.addBatch();
                    }
                    
                }
                pstmt.executeBatch();
                conn.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return maCa;
    }

    // Thêm vào class ca_lam_query
public static String getTrangThaiCa(java.util.Date ngay, String tenCa) {
    String sql = "SELECT TrangThai FROM Ca WHERE Ngay = ? AND Ca = ?";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try (Connection conn = MyConnection.connect();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, sdf.format(ngay));
        ps.setString(2, tenCa);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String tt = rs.getString("TrangThai");
            return (tt == null) ? "" : tt;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return ""; // Trả về rỗng nếu chưa có dữ liệu hoặc lỗi
}
    public static String layNguoiMoCa(java.util.Date ngay, String tenCa) {
        String ngayStr = sdf.format(ngay);
        String sql = "SELECT MaNV FROM Ca WHERE Ngay = ? AND Ca = ?";
        
        try (Connection conn = database.MyConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ngayStr);
            ps.setString(2, tenCa);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("MaNV");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkNhanVienCoLichLam(String maNV, java.util.Date ngay, String tenCa) {
        String ngayStr = sdf.format(ngay);
        String sql = "SELECT dk.MaNV FROM DangKyCaLam dk " +
                     "JOIN Ca c ON dk.MaCa = c.MaCa " +
                     "WHERE dk.MaNV = ? AND c.Ngay = ? AND c.Ca = ?";
        
        try (Connection conn = database.MyConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            ps.setString(2, ngayStr);
            ps.setString(3, tenCa);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Trả về true nếu tìm thấy lịch đăng ký
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Thực hiện Mở Ca (Update MaNV mở ca và Tiền Két vào bảng Ca, Insert DOANHTHU và DANHSACHHOADON)
    public static boolean thucHienMoCa(String maNV, java.util.Date ngay, String tenCa, int tienKet) {
    String ngayStr = sdf.format(ngay);
    String maCa = taoMaCa(ngay, tenCa.equals("Sáng") ? 1 : (tenCa.equals("Chiều") ? 2 : 3));
    
    Connection conn = null;
    try {
        conn = database.MyConnection.connect();
        conn.setAutoCommit(false); 

        // --- BƯỚC 1: KIỂM TRA & TỰ ĐỘNG TẠO LỊCH NẾU THIẾU ---
        boolean caDaTonTai = false;
        String checkSql = "SELECT COUNT(*) FROM CA WHERE MaCa = ?";
        try(PreparedStatement psCheck = conn.prepareStatement(checkSql)){
             psCheck.setString(1, maCa);
             ResultSet rsCheck = psCheck.executeQuery();
             if(rsCheck.next()) caDaTonTai = (rsCheck.getInt(1) > 0);
        }

        if (!caDaTonTai) {
            // Chưa có lịch -> INSERT mới luôn
            System.out.println("Phát hiện thiếu lịch -> Đang tự động tạo mới...");
            String sqlInsertCa = "INSERT INTO CA (MaCa, Ngay, Ca, TrangThai, MoKet, MaNV) VALUES (?, ?, ?, N'Mở Ca', ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlInsertCa)) {
                ps.setString(1, maCa);
                ps.setString(2, ngayStr);
                ps.setString(3, tenCa);
                ps.setInt(4, tienKet);
                ps.setString(5, maNV);
                ps.executeUpdate();
            }
        } else {
            // Đã có lịch -> UPDATE trạng thái
            String sqlUpdateCa = "UPDATE Ca SET MaNV = ?, MoKet = ?, TrangThai = N'Mở Ca' WHERE MaCa = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateCa)) {
                ps.setString(1, maNV);
                ps.setInt(2, tienKet);
                ps.setString(3, maCa);
                int rows = ps.executeUpdate();
                if (rows == 0) { conn.rollback(); return false; }
            }
        }
        
        // --- BƯỚC 2: TẠO DOANH THU & DANH SÁCH HÓA ĐƠN ---
        String maDT = taoMaDoanhThu(); // Hàm bạn đã thêm trước đó
        
        String sqlInsertDT = "INSERT INTO DOANHTHU (MaDT, MaCa, TienMat, ChuyenKhoan) VALUES (?, ?, ?, 0)";
        try (PreparedStatement ps = conn.prepareStatement(sqlInsertDT)) {
            ps.setString(1, maDT);
            ps.setString(2, maCa);
            ps.setInt(3, tienKet);
            ps.executeUpdate();
        }
        
        String sqlInsertDSHD = "INSERT INTO DANHSACHHOADON (MaCa) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sqlInsertDSHD)) {
            ps.setString(1, maCa);
            ps.executeUpdate();
        }
        
        conn.commit();
        return true;
        
    } catch (Exception e) {
        e.printStackTrace();
        try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
        return false;
    } finally {
        try { if (conn != null) conn.close(); } catch (SQLException ex) {}
    }
}
     private static String xacDinhCaHienTai() {
    int hour = LocalDateTime.now().getHour();

    if (hour >= 0 && hour < 12) return "Sáng";
    if (hour >= 12 && hour < 18) return "Chiều";
    if (hour >= 18 && hour <= 23) return "Tối";

    return null; // ngoài giờ làm
}
public static String taoMaDoanhThu() {
    String prefix = "DT";
    // Lấy mã DT lớn nhất hiện tại để tăng lên 1
    String sql = "SELECT TOP 1 MaDT FROM DOANHTHU ORDER BY MaDT DESC";
    
    try (java.sql.Connection conn = database.MyConnection.connect();
         java.sql.PreparedStatement pst = conn.prepareStatement(sql)) {
        
        java.sql.ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            String maCu = rs.getString("MaDT");
            // Giả sử mã có dạng DT0001, DT0002...
            // Cắt chuỗi lấy phần số
            String phanSo = maCu.substring(2); 
            int soMoi = Integer.parseInt(phanSo) + 1;
            // Format lại thành chuỗi, ví dụ DT0006
            return String.format("%s%04d", prefix, soMoi);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    // Nếu bảng chưa có dữ liệu, trả về mã đầu tiên
    return "DT0001";
}
    public static String getMaCaDangMo() {

    String caHienTai = xacDinhCaHienTai();
    if (caHienTai == null) return null;

    LocalDate today = LocalDate.now();

    String sql = """
        SELECT TOP 1 MaCa
        FROM CA
        WHERE Ngay = ?
          AND Ca = ?
          AND TrangThai = N'Mở Ca'
    """;

    try (Connection conn = MyConnection.connect();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setDate(1, java.sql.Date.valueOf(today));
        ps.setString(2, caHienTai);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("MaCa");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null; // chưa mở ca
}

}
