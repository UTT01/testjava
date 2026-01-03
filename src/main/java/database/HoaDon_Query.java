/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;


import Constructor.Discount;
import Constructor.KhuVuc;
import Constructor.LoaiSP;
import Constructor.SanPham;
import Constructor.Table;

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
import java.util.List;

/**
 *
 * @author ducha
 */
public class HoaDon_Query {
    
    public MyConnection testJDBC = new MyConnection();
    public static ArrayList<String> dsSinhVien = new ArrayList<>();
    private Connection conn = testJDBC.connect();
    public Connection getConnection() {
        return conn;
    }
    
    
        public class ChiTietHoaDonTAM {
        public int maHD;
        public String tenSP;
        public int soLuong;
        public int donGia;
        public String ghiChu;

        public ChiTietHoaDonTAM(
                int maHD,
                String tenSP,
                int soLuong,
                int donGia,
                String ghiChu
        ) {
            this.maHD = maHD;
            this.tenSP = tenSP;
            this.soLuong = soLuong;
            this.donGia = donGia;
            this.ghiChu = ghiChu;
        }
    }



//    public ArrayList<Table> getTableByTenKhuVuc(String tenKV) {
//            ArrayList<Table> dsBan = new ArrayList<>();
//
//            try {
//
//
//               String sql = """
//                SELECT b.SttBan, b.MaBan, kv.MaKV, kv.TenKV, b.TrThai
//                FROM TABLE_KV b
//                JOIN KhuVuc kv ON b.MaKV = kv.MaKV
//                WHERE kv.TenKV= ?
//                 """;
//
//
//                PreparedStatement pst = conn.prepareStatement(sql);
//                pst.setString(1, tenKV);
//
//                ResultSet rs = pst.executeQuery();
//                while (rs.next()) {
//                    String sttBan = rs.getString("SttBan");
//                    int maBan = rs.getInt("MaBan"); 
//                    String TrThai = rs.getString("TrThai");
//                    int maKV = rs.getInt("MaKV");
//                    String khuVuc = rs.getString("TenKV");
//                    dsBan.add(new Table(maBan, sttBan, TrThai, maKV,khuVuc, 0.0));
//                }
//
//                rs.close();
//                pst.close();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return dsBan;
//        }
    public List<Table> getTableByMaKV(int maKV) {

        List<Table> dsBan = new ArrayList<>();

        String sql = """
           SELECT 
                b.MaBan,
                b.SttBan,
                b.TrThai,
                ISNULL(SUM(hd.TongTien), 0) AS TongTien
            FROM TABLE_KV b
            LEFT JOIN HOADON hd 
                ON b.MaBan = hd.MaBan 
               AND hd.TrangThaiThanhToan = 0
            WHERE b.MaKV = ?
            GROUP BY b.MaBan, b.SttBan, b.TrThai
            ORDER BY b.SttBan;
        """;

        try (
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, maKV);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Table ban = new Table(
                        rs.getInt("MaBan"),
                        rs.getString("SttBan"),
                        rs.getString("TrThai"),
                        0,
                        "",
                        rs.getDouble("TongTien")
                    );
                    dsBan.add(ban);
                }
            }

        } catch (SQLException e) {
            // 🔴 LOG LỖI (không làm crash chương trình)
            System.err.println("Lỗi getTableByMaKV | MaKV = " + maKV);
            e.printStackTrace();
        }

        return dsBan; // ❗ luôn trả về List (có thể rỗng)
    }

    public ArrayList<Table> getTableSelected() {
            ArrayList<Table> dsBan = new ArrayList<>();

            String sql = """
                SELECT 
                    b.MaBan,
                    b.SttBan,
                    b.TrThai,
                    b.TongTien,
                    kv.MaKV,
                    kv.TenKV
                FROM TABLE_KV b
                JOIN KhuVuc kv ON kv.TenKV = N'Hóa Đơn'
                WHERE b.TrThai = ?
            """;

            try (PreparedStatement pst = conn.prepareStatement(sql)) {

                pst.setString(1, "full");

                try (ResultSet rs = pst.executeQuery()) {

                    while (rs.next()) {

                        int maBan = rs.getInt("MaBan");
                        String sttBan = rs.getString("SttBan");
                        String trThai = rs.getString("TrThai");
                        double tongTien = rs.getDouble("TongTien");

                        int maKV = rs.getInt("MaKV");
                        String tenKV = rs.getString("TenKV");

                        dsBan.add(new Table(
                            maBan,
                            sttBan,
                            trThai,
                            maKV,
                            tenKV,
                            tongTien
                        ));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return dsBan;
        }

    public List<KhuVuc> getAllKhuVuc() {
            List<KhuVuc> list = new ArrayList<>();
            
            try { 
                String sql = "SELECT MaKV, TenKV FROM KHUVUC";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql);

                while (rs.next()) {
                   int maKV = rs.getInt("MaKV");
                   String tenKV = rs.getString("tenKV");
                   list.add(new KhuVuc(maKV, tenKV));
                }
                rs.close();
                st.close();
            } catch (Exception e){
                e.printStackTrace();
            }
            return list;
        }

    public ArrayList<LoaiSP> getAllLoaiSP(){
        ArrayList<LoaiSP> dsLoaiSP = new ArrayList<>();
        try {
            String sql = "SELECT MaLoai, TenLoai, ImgRes FROM LOAISANPHAM";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                String ImgRes = rs.getString("ImgRes");
                int maLoai = rs.getInt("MaLoai");
                String TenLoai = rs.getString("TenLoai");
             dsLoaiSP.add(new LoaiSP(ImgRes, TenLoai, maLoai));
            }
            rs.close();
            st.close();
            
            
        } catch (Exception e) {
        e.printStackTrace();
        }
        return dsLoaiSP;
}
        
    
    public int getidLoaiSP(String tenLoai) {
    int id = 0;

    try {

        
       String sql = """
        SELECT MaLoai
        FROM LOAISANPHAM
        WHERE TenLoai= ?
         """;


        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, tenLoai);

        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
           id = rs.getInt("MaLoai");
        }

        rs.close();
        pst.close();

    } catch (Exception e) {
        e.printStackTrace();
    }

    return id;
}
    
    public ArrayList<SanPham> getMonAn(int maLoai) {
        ArrayList<SanPham> list = new ArrayList<>();

        String sql =
            "SELECT MaSP, TenSP, DonGia, Hinh, MaLoai " +
            "FROM SANPHAM " +
            "WHERE TrangThai = 1 AND MaLoai = ?";

        try (
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setInt(1, maLoai);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int maSP = rs.getInt("MaSP");
                    String tenSP = rs.getString("TenSP");
                    int donGia = rs.getInt("DonGia");
                    String hinh = rs.getString("Hinh");
                    int maLoaiSP = rs.getInt("MaLoai");

                    list.add(new SanPham(
                        tenSP,
                        donGia,
                        hinh,       
                        maLoaiSP,
                        maSP,
                        true
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public ArrayList<SanPham> getAllMonAn() {
        ArrayList<SanPham> list = new ArrayList<>();

        String sql =
            "SELECT MaSP, TenSP, DonGia, Hinh, MaLoai " +
            "FROM SANPHAM " +
            "WHERE TrangThai = 1 ";

        try (
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int maSP = rs.getInt("MaSP");
                    String tenSP = rs.getString("TenSP");
                    int donGia = rs.getInt("DonGia");
                    String hinh = rs.getString("Hinh");
                    int maLoaiSP = rs.getInt("MaLoai");

                    list.add(new SanPham(
                        tenSP,
                        donGia,
                        hinh,       
                        maLoaiSP,
                        maSP,
                        true
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public java.util.List<Discount> getAllKhuyenMai() {

    java.util.List<Discount> list = new ArrayList<>();

    String sql = "SELECT MaKM, TenKM, TyLeGiam FROM KhuyenMai";

    try (
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            list.add(new Discount(
                rs.getInt("MaKM"),
                rs.getString("TenKM"),
                rs.getInt("TyLeGiam")
            ));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
    
    
        public int insertHoaDon(
            int maBan,
            LocalDateTime gioVao,
            int maDSHD
        ) {
            String sql = """
                INSERT INTO HoaDon
                (MaBan, GioVao, MaDSHD)
                VALUES (?, ?, ?)
            """;

            try (
                PreparedStatement ps = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
                )
            ) {
                ps.setInt(1, maBan);
                ps.setTimestamp(2, Timestamp.valueOf(gioVao));
                ps.setInt(3, maDSHD); // ✅ ĐÚNG INDEX

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return -1;
        }

    
    
        public int getMaSPbyName(String tenSP) {
            int maSP = -1;

            String sql = """
                SELECT MaSP
                FROM SanPham
                WHERE TenSP = ?
            """;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, tenSP);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        maSP = rs.getInt("MaSP");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return maSP;
        }

public void insertChiTietHoaDon(
        int maHD,
        int maSP,
        int maBan,
        int soLuong,
        int donGia,
        String ghiChu
) {

    System.out.println("=== INSERT CHI TIẾT HÓA ĐƠN ===");
    System.out.println("MaHD   = " + maHD);
    System.out.println("MaSP   = " + maSP);
    System.out.println("MaBan  = " + maBan);
    System.out.println("SL     = " + soLuong);
    System.out.println("DonGia = " + donGia);
    System.out.println("GhiChu = " + ghiChu);

    if (maHD <= 0)
        throw new IllegalArgumentException("❌ MaHD không hợp lệ");

    if (maSP <= 0)
        throw new IllegalArgumentException("❌ MaSP không hợp lệ");

    if (soLuong <= 0)
        throw new IllegalArgumentException("❌ Số lượng <= 0");

    String sql = """
        INSERT INTO CHITIETHOADON
        (MaHD, MaSP, MaBan, SoLuong, DonGia, GhiChu)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, maHD);
        ps.setInt(2, maSP);
        ps.setInt(3, maBan);
        ps.setInt(4, soLuong);
        ps.setInt(5, donGia);

        if (ghiChu == null || ghiChu.isBlank())
            ps.setNull(6, Types.NVARCHAR);
        else
            ps.setString(6, ghiChu);

        int rows = ps.executeUpdate();

        if (rows == 0) {
            throw new SQLException("❌ Insert ChiTietHoaDon thất bại, rows = 0");
        }

        System.out.println("✅ Insert ChiTietHoaDon OK");

    } catch (SQLException e) {
        System.err.println("❌ SQL ERROR insertChiTietHoaDon");
        System.err.println("➡ SQLState: " + e.getSQLState());
        System.err.println("➡ ErrorCode: " + e.getErrorCode());
        e.printStackTrace();
        throw new RuntimeException("Lỗi SQL insertChiTietHoaDon");
    }
}


    public int InsertMaDSHD(String MaCa) {
            int rows = 0;
            int generatedId = -1;

            try {
                String sql = "INSERT INTO DANHSACHHOADON (MaCa) VALUES (?)";
                PreparedStatement ps = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
                );

                ps.setString(1, MaCa);

                rows = ps.executeUpdate();

                if (rows > 0) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                    System.out.println("Insert thành công, ID = " + generatedId);
                }

            } catch (Exception e) {
                System.err.println("Lỗi: " + e);
            }

            return generatedId; 
        }
    public int getMaDSHD(String maCa) {
        int maDSHD = -1;

        String sqlSelect = """
            SELECT MaDSHD
            FROM DANHSACHHOADON
            WHERE MaCa = ?
        """;

        String sqlInsert = """
            INSERT INTO DANHSACHHOADON (MaCa)
            VALUES (?)
        """;

        try {
            // 1️⃣ kiểm tra tồn tại
            try (PreparedStatement ps = conn.prepareStatement(sqlSelect)) {
                ps.setString(1, maCa);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("MaDSHD");
                }
            }

            // 2️⃣ chưa có → tạo mới
            try (PreparedStatement ps = conn.prepareStatement(
                    sqlInsert, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, maCa);
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    maDSHD = rs.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return maDSHD;
    }

    public void updateChiTietHoaDon(
                int maHD,
                int maSP,
                int maBan,
                int soLuong,
                int donGia,
                String ghiChu
        ) {
            String sqlCheck = """
                SELECT 1 FROM CHITIETHOADON
                WHERE MaHD = ? AND MaSP = ?
            """;

            String sqlUpdate = """
                UPDATE CHITIETHOADON
                SET SoLuong = ?, DonGia = ?, GhiChu = ?
                WHERE MaHD = ? AND MaSP = ?
            """;

            String sqlInsert = """
                INSERT INTO ChiTietHoaDon
                (MaHD, MaSP, MaBan, SoLuong, DonGia, GhiChu)
                VALUES (?, ?, ?, ?, ?, ?)
            """;

            try {
                // 1️⃣ Kiểm tra tồn tại
                try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                    psCheck.setInt(1, maHD);
                    psCheck.setInt(2, maSP);

                    ResultSet rs = psCheck.executeQuery();

                    if (rs.next()) {
                        // 2️⃣ Đã tồn tại → UPDATE
                        try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                            psUpdate.setInt(1, soLuong);
                            psUpdate.setInt(2, donGia);
                            psUpdate.setString(3, ghiChu);
                            psUpdate.setInt(4, maHD);
                            psUpdate.setInt(5, maSP);

                            psUpdate.executeUpdate();
                            System.out.println("🔄 UPDATE ChiTietHoaDon | MaHD=" + maHD + ", MaSP=" + maSP);
                        }
                    } else {
                        // 3️⃣ Chưa tồn tại → INSERT
                        try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                            psInsert.setInt(1, maHD);
                            psInsert.setInt(2, maSP);
                            psInsert.setInt(3, maBan);
                            psInsert.setInt(4, soLuong);
                            psInsert.setInt(5, donGia);
                            psInsert.setString(6, ghiChu);

                            psInsert.executeUpdate();
                            System.out.println("➕ INSERT ChiTietHoaDon | MaHD=" + maHD + ", MaSP=" + maSP);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("❌ Lỗi updateChiTietHoaDon");
            }
        }
   public boolean updateHoaDonThanhToan(
                int maHD,
                LocalDateTime gioRa,
                double tongTien,
                String phuongThuc,
                Integer maKM
        ) {
            String sql = """
                UPDATE HoaDon
                SET GioRa = ?,
                    TongTien = ?,
                    PhuongThuc = ?,
                    MaKM = ?,
                    TrangThaiThanhToan = 1
                WHERE MaHD = ?
            """;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setTimestamp(1, Timestamp.valueOf(gioRa));
                ps.setDouble(2, tongTien);
                ps.setString(3, phuongThuc);

                if (maKM == null) {
                    ps.setNull(4, Types.INTEGER);
                } else {
                    ps.setInt(4, maKM);
                }

                ps.setInt(5, maHD);

                return ps.executeUpdate() > 0;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

    public void deleteChiTietHoaDon(int maHD) {

            if (maHD <= 0) {
                throw new IllegalArgumentException("MaHD không hợp lệ: " + maHD);
            }

            String sql = "DELETE FROM CHITIETHOADON WHERE MaHD = ?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, maHD);
                int rows = ps.executeUpdate();

                System.out.println(
                    "🗑️ Đã xóa " + rows + " chi tiết hóa đơn | MaHD = " + maHD
                );

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Lỗi deleteChiTietHoaDon | MaHD = " + maHD);
            }
        }

   public boolean updateTrangThaiBan(
            
            int maBan,
            String trangThai,
            double tongTien) throws SQLException {

        String sql =
            "UPDATE TABLE_KV SET TrThai = ?, TongTien = ? WHERE MaBan = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setDouble(2, tongTien);
            ps.setInt(3, maBan);
            return ps.executeUpdate() > 0;
        }
    }
   
  public ArrayList<ChiTietHoaDonTAM> getChiTietHoaDonDangMoTheoBan(int maBan) {

    ArrayList<ChiTietHoaDonTAM> list = new ArrayList<>();

    String sql = """
        SELECT 
            hd.MaHD,
            sp.TenSP,
            cthd.SoLuong,
            cthd.DonGia,
            ISNULL(cthd.GhiChu, '') AS GhiChu
        FROM HoaDon hd
        JOIN CHITIETHOADON cthd ON hd.MaHD = cthd.MaHD
        JOIN SanPham sp ON cthd.MaSP = sp.MaSP
        WHERE hd.MaBan = ?
          AND hd.TrangThaiThanhToan = 0
    """;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, maBan);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(new ChiTietHoaDonTAM(
                rs.getInt("MaHD"),
                rs.getString("TenSP"),
                rs.getInt("SoLuong"),
                rs.getInt("DonGia"),
                rs.getString("GhiChu")
            ));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}



}

