package database;

import Constructor.CTHoaDon;
import Constructor.Ca;
import Constructor.DoanhThu;
import Constructor.HoaDon;
import Constructor.MonDaBan;
import Constructor.PhieuThuChi;
import Constructor.CTHoaDon;
import Constructor.Ca;
import Constructor.DoanhThu;
import Constructor.MonDaBan;
import Constructor.PhieuThuChi;

import java.sql.Connection;
import java.sql.PreparedStatement; // Dùng cái này an toàn hơn Statement
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class doanh_thu_sql {

    // --- LẤY DANH SÁCH HÓA ĐƠN ---
    public ArrayList<HoaDon> getAllHoaDon() {
        ArrayList<HoaDon> list = new ArrayList<>();
        System.out.println("SQL_DATA: Bat dau lay du lieu HOADON tu SQL");

        String query = "SELECT hd.MaHD, hd.MaBan, " +
                       "       FORMAT(CAST(hd.GioVao AS DATETIME), 'HH:mm') AS GioVao, " +
                       "       FORMAT(CAST(hd.GioRa AS DATETIME), 'HH:mm') AS GioRa, " +
                       "       hd.TongTien, hd.PhuongThuc, c.MaCa " +
                       "FROM HOADON hd " +
                       "JOIN DANHSACHHOADON ds ON hd.MaDSHD = ds.MaDSHD " +
                       "JOIN CA c ON ds.MaCa = c.MaCa " +
                       "WHERE c.TrangThai = N'Mở Ca'";

        try (Connection con = MyConnection.connect();
             Statement stmt = (con != null) ? con.createStatement() : null;
             ResultSet rs = (stmt != null) ? stmt.executeQuery(query) : null) {

            if (con == null) {
                System.err.println("SQL_DATA: Kết nối SQL thất bại");
                return list;
            }

            while (rs.next()) {
                String maHD = rs.getString("MaHD");
                String maBan = rs.getString("MaBan");
                String gioVao = rs.getString("GioVao");
                String gioRa = rs.getString("GioRa");
                int tongTien = rs.getInt("TongTien");
                String phuongThuc = rs.getString("PhuongThuc");
                String maca = rs.getString("MaCa"); 

                HoaDon hoaDon = new HoaDon(maHD, maBan, gioVao, gioRa, tongTien, phuongThuc, maca);
                list.add(hoaDon);
            }

        } catch (Exception e) {
            System.err.println("SQL_ERROR: Lỗi lấy dữ liệu HOADON");
            e.printStackTrace();
        }

        System.out.println("SQL_DATA: Tong so hoa don lay dc: " + list.size());
        return list;
    }

    // --- LẤY DANH SÁCH CA ---
    public ArrayList<Ca> getAllCa() {
    ArrayList<Ca> list = new ArrayList<>();
    System.out.println("SQL_DATA: Bắt đầu lấy dữ liệu CA từ SQL");

    String query = "SELECT MaCa, Ngay, Ca, TrangThai FROM CA WHERE TrangThai IS NOT NULL " +
               "ORDER BY Ngay DESC, " + 
               "CASE WHEN TrangThai = N'Mở Ca' THEN 0 ELSE 1 END, " +
               "MaCa DESC";

    try (Connection con = MyConnection.connect();
         Statement stmt = (con != null) ? con.createStatement() : null;
         ResultSet rs = (stmt != null) ? stmt.executeQuery(query) : null) {

        if (con == null) return list;

        while (rs.next()) {
            list.add(new Ca(
                rs.getString("MaCa"),
                rs.getString("Ca"), 
                rs.getString("Ngay"),              
                rs.getString("TrangThai")
            ));
        }

    } catch (Exception e) {
        System.err.println("SQL_ERROR: Lỗi lấy dữ liệu CA");
        e.printStackTrace();
    }

    System.out.println("SQL_DATA: Tổng số Ca lấy được: " + list.size());
    return list;
}

    // --- LẤY CHI TIẾT HÓA ĐƠN (Dùng PreparedStatement) ---
    public ArrayList<CTHoaDon> getChiTietHoaDonByMaHD(String maHoaDon) {
    ArrayList<CTHoaDon> list = new ArrayList<>();
    
    if (maHoaDon != null) maHoaDon = maHoaDon.trim();
    
    // --- SỬA CÂU TRUY VẤN: Thay MaMon thành MaSP ---
    // Kiểm tra kỹ xem trong SQL bảng SANPHAM cột khóa chính là MaMon hay MaSP
    // Thường là MaSP.
    
    String query = "SELECT " +
            "    HD.MaHD, HD.MaBan, " +
            "    FORMAT(CAST(HD.GioVao AS DATETIME), 'dd/MM/yyyy HH:mm') AS GioVao, " +
            "    FORMAT(CAST(HD.GioRa AS DATETIME), 'HH:mm') AS GioRa, " +
            "    HD.TongTien, HD.PhuongThuc, " +
            "    SP.TenSP, CTHD.SoLuong, SP.DonGia " + 
            "FROM HOADON AS HD " +
            "JOIN CHITIETHOADON AS CTHD ON HD.MaHD = CTHD.MaHD " +
            "JOIN SANPHAM AS SP ON CTHD.MaSP = SP.MaSP " +             
            "WHERE HD.MaHD = ?";

    try (Connection con = MyConnection.connect();
         PreparedStatement pstmt = (con != null) ? con.prepareStatement(query) : null) {

        if (con == null || pstmt == null) return list;

        pstmt.setString(1, maHoaDon);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int maHD = rs.getInt("MaHD");
                int maBan = rs.getInt("MaBan");
                String gioVao = rs.getString("GioVao");
                String gioRa = rs.getString("GioRa");
                int tongTien = rs.getInt("TongTien");
                String phuongThuc = rs.getString("PhuongThuc");
                
                String tenMon = rs.getString("TenSP");
                int soLuong = rs.getInt("SoLuong");
                int donGiaMon = rs.getInt("DonGia");
                
                int thanhTien = soLuong * donGiaMon;

                CTHoaDon ct = new CTHoaDon(
                    maHD, maBan, gioVao, gioRa, phuongThuc, tongTien, 
                    tenMon, soLuong, donGiaMon, thanhTien
                );
                
                list.add(ct);
            }
        }

    } catch (Exception e) {
        System.err.println("SQL_ERROR: Lỗi lấy chi tiết hóa đơn: " + e.getMessage());
        e.printStackTrace();
    }

    return list;
}

    // --- LẤY DOANH THU THEO CA ---
    public ArrayList<DoanhThu> getDoanhThuByMaCa(String maCa) {
        ArrayList<DoanhThu> list = new ArrayList<>();
        System.out.println("SQL_DATA: Lấy doanh thu MaCa: " + maCa);

        String query = "SELECT dt.MaCa, (dt.TienMat + dt.ChuyenKhoan) AS TongDoanhThu, " +
                       "dt.TienMat, dt.ChuyenKhoan, c.MoKet, c.Ngay " +
                       "FROM DOANHTHU dt " +
                       "JOIN CA c ON dt.MaCa = c.MaCa " +
                       "WHERE dt.MaCa = ?";

        try (Connection con = MyConnection.connect();
             PreparedStatement pstmt = (con != null) ? con.prepareStatement(query) : null) {

            if (con == null) return list;

            pstmt.setString(1, maCa);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new DoanhThu(
                        rs.getString("MaCa"),
                        rs.getString("Ngay"),
                        rs.getInt("TienMat"),
                        rs.getInt("ChuyenKhoan"),
                        rs.getInt("MoKet")
                    ));
                }
            }
        } catch (Exception e) {
            System.err.println("SQL_ERROR: Lỗi lấy doanh thu");
            e.printStackTrace();
        }
        return list;
    }

    // --- CẬP NHẬT PHIẾU THU CHI (QUAN TRỌNG: Dùng PreparedStatement) ---
    public boolean updatePhieuThuChi(String maCa, int soTien, String loai, String lyDo) {
        boolean isThu = loai.trim().equalsIgnoreCase("Thu") || loai.contains("Thu");

        String prefix = isThu ? "THU" : "CHI";
        String loaiValue = isThu ? "Thu" : "Chi";

        try (Connection con = MyConnection.connect()) {
            if (con == null) return false;

            // 1. Kiểm tra trạng thái Ca
            String checkCaQuery = "SELECT TrangThai FROM CA WHERE MaCa = ?";
            try (PreparedStatement pstCheck = con.prepareStatement(checkCaQuery)) {
                pstCheck.setString(1, maCa);
                try (ResultSet rs = pstCheck.executeQuery()) {
                    if (!rs.next() || !rs.getString("TrangThai").trim().equalsIgnoreCase("Mở Ca")) {
                        System.err.println("SQL_UPDATE: Ca không ở trạng thái Mở");
                        return false;
                    }
                }
            }

            // 2. Tạo mã phiếu tự động
            String countQuery = "SELECT COUNT(*) AS SoLuong FROM PhieuThuChi WHERE Loai = ?";
            int count = 0;
            try (PreparedStatement pstCount = con.prepareStatement(countQuery)) {
                pstCount.setString(1, loaiValue);
                try (ResultSet rs = pstCount.executeQuery()) {
                    if (rs.next()) count = rs.getInt("SoLuong");
                }
            }
            String maPhieu = prefix + String.format("%03d", count + 1);

            // 3. Insert vào bảng ThuChi
            String insertQuery = "INSERT INTO PhieuThuChi (MaPhieu, MaCa, SoTien, GhiChu, Loai) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstInsert = con.prepareStatement(insertQuery)) {
                pstInsert.setString(1, maPhieu);
                pstInsert.setString(2, maCa);
                pstInsert.setInt(3, soTien);
                pstInsert.setString(4, lyDo); // PreparedStatement tự xử lý N'...' cho tiếng Việt
                pstInsert.setString(5, loaiValue);
                pstInsert.executeUpdate();
            }

            // 4. Cập nhật MoKet
            String updateKetQuery = loaiValue.equals("Thu")
                    ? "UPDATE CA SET MoKet = MoKet + ? WHERE MaCa = ?"
                    : "UPDATE CA SET MoKet = MoKet - ? WHERE MaCa = ?";
            try (PreparedStatement pstUpdate = con.prepareStatement(updateKetQuery)) {
                pstUpdate.setInt(1, soTien);
                pstUpdate.setString(2, maCa);
                pstUpdate.executeUpdate();
            }

            return true;

        } catch (Exception e) {
            System.err.println("SQL_UPDATE_ERROR: Lỗi cập nhật phiếu thu/chi");
            e.printStackTrace();
            return false;
        }
    }

    // --- LẤY PHIẾU THU CHI ---
    public ArrayList<PhieuThuChi> getPhieuByMaCa(String maCa, String loai) {
        ArrayList<PhieuThuChi> list = new ArrayList<>();
        String query = "SELECT MaPhieu, MaCa, SoTien, GhiChu, Loai FROM PhieuThuChi WHERE MaCa = ? AND Loai = ?";

        try (Connection con = MyConnection.connect();
             PreparedStatement pstmt = (con != null) ? con.prepareStatement(query) : null) {

            if (con == null) return list;

            pstmt.setString(1, maCa);
            pstmt.setString(2, loai);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new PhieuThuChi(
                        rs.getString("MaPhieu"),
                        rs.getString("GhiChu"),
                        rs.getString("MaCa"),
                        rs.getInt("SoTien"),
                        rs.getString("Loai")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- LẤY DANH SÁCH MÓN ĐÃ BÁN ---
    public ArrayList<MonDaBan> getDanhSachMonDaBanTrongCa(String maCa) {
        ArrayList<MonDaBan> list = new ArrayList<>();
        String query = "SELECT sp.TenSP, SUM(ct.SoLuong) AS TongSoLuong " +
                "FROM CA c " +
                "JOIN DANHSACHHOADON dshd ON c.MaCa = dshd.MaCa " +
                "JOIN HOADON hd ON dshd.MaDSHD = hd.MaDSHD " +
                "JOIN CHITIETHOADON ct ON hd.MaHD = ct.MaHD " +
                "JOIN SANPHAM sp ON ct.MaSP = sp.MaSP " +
                "WHERE c.MaCa = ? " +
                "GROUP BY sp.TenSP";

        try (Connection con = MyConnection.connect();
             PreparedStatement pstmt = (con != null) ? con.prepareStatement(query) : null) {

            if (con == null) return list;

            pstmt.setString(1, maCa);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new MonDaBan(rs.getString("TenSP"), rs.getInt("TongSoLuong")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- ĐÓNG CA ---
    public boolean dongCa(String maCa) {
        try (Connection con = MyConnection.connect();
             PreparedStatement pstmt = (con != null) ? con.prepareStatement("UPDATE CA SET TrangThai = N'Đóng' WHERE MaCa = ?") : null) {

            if (con == null) return false;
            
            pstmt.setString(1, maCa);
            return pstmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // Hàm lấy mã ca đang mở bất kể giờ giấc (Ưu tiên hiển thị)
    public String getMaCaDangMo() {
    String query = "SELECT MaCa FROM CA WHERE TrangThai = N'Mở Ca'";
    try (java.sql.Connection con = MyConnection.connect();
         java.sql.PreparedStatement pstmt = con.prepareStatement(query);
         java.sql.ResultSet rs = pstmt.executeQuery()) {
        
        if (rs.next()) {
            return rs.getString("MaCa");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "Chưa mở ca";
}
    // --- LẤY TRẠNG THÁI CA ---
    public String getTrangThaiCa(String maCa) {
        String trangThai = "";
        try (Connection con = MyConnection.connect();
             PreparedStatement pstmt = (con != null) ? con.prepareStatement("SELECT TrangThai FROM CA WHERE MaCa = ?") : null) {

            if (con == null) return "";

            pstmt.setString(1, maCa);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    trangThai = rs.getString("TrangThai").trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trangThai;
    }
}