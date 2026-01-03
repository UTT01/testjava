/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import Constructor.item_nhanvien;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author khoin
 */
public class nhan_vien_query {
    public static boolean KiemTraTonTai(String dulieuktra, String cotktra, String bangktra) {
        String sql = "SELECT 1 FROM " + bangktra + " WHERE LOWER(" + cotktra + ") = LOWER(?)";
        try (Connection conn = database.MyConnection.connect();
             PreparedStatement check = conn.prepareStatement(sql)) {
            
            check.setString(1, dulieuktra);
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; 
    }

    public static boolean addNhanVien(String MaNV, String TenNv, String ChucVu, float Luong, Date NgayVaoLam, String MaThuongHieu, Date NgaySinh, String email, String MatKhau, String TenThuongHieu, String diachi) {
        
        String addNv = "INSERT INTO NhanVien (MaNV, TenNV, ChucVu, Luong, NgayVaoLam, MaThuongHieu, NgaySinh) VALUES (?,?,?,?,?,?,?)";
        String addThuongHieu = "INSERT INTO ThuongHieu (MaThuongHieu, TenThuongHieu, DiaChi) VALUES (?,?,?)";
        String addTaiKhoan = "INSERT INTO TaiKhoan (MaTK, MaNV, TenDangNhap, MatKhau, LoaiTaiKhoan) VALUES (?,?,?,?,?)";

        Connection conn = null;
        PreparedStatement sql_add_nv = null;
        PreparedStatement sql_add_th = null;
        PreparedStatement sql_add_tk = null;

        try {
            conn = database.MyConnection.connect();
            conn.setAutoCommit(false); 

            // 1. Thương hiệu
            if (TenThuongHieu != null && !TenThuongHieu.trim().isEmpty()) {
                sql_add_th = conn.prepareStatement(addThuongHieu);
                sql_add_th.setString(1, MaThuongHieu);
                sql_add_th.setString(2, TenThuongHieu);
                sql_add_th.setString(3, diachi);
                sql_add_th.executeUpdate();
            }

            // 2. Nhân Viên
            sql_add_nv = conn.prepareStatement(addNv);
            sql_add_nv.setString(1, MaNV);
            sql_add_nv.setString(2, TenNv);
            sql_add_nv.setString(3, ChucVu);
            sql_add_nv.setFloat(4, Luong);

            if (NgayVaoLam != null) {
                sql_add_nv.setDate(5, new java.sql.Date(NgayVaoLam.getTime()));
            } else {
                sql_add_nv.setNull(5, java.sql.Types.DATE);
            }

            sql_add_nv.setString(6, MaThuongHieu);

            if (NgaySinh != null) {
                sql_add_nv.setDate(7, new java.sql.Date(NgaySinh.getTime()));
            } else {
                sql_add_nv.setNull(7, java.sql.Types.DATE);
            }
            int check_them_nv = sql_add_nv.executeUpdate();

            // 3. Tài Khoản
            sql_add_tk = conn.prepareStatement(addTaiKhoan);
            
            sql_add_tk.setString(1, database.tool_linh_tinh.taoMaTaiKhoan()); 
            sql_add_tk.setString(2, MaNV);
            sql_add_tk.setString(3, email);
            String matKhauDaMaHoa = database.tool_linh_tinh.hashPassword(MatKhau);
            sql_add_tk.setString(4, matKhauDaMaHoa);
            String quyen = MaNV.startsWith("QL") ? "QuanLy" : "NhanVien";
            sql_add_tk.setString(5, quyen);
            sql_add_tk.executeUpdate();

            conn.commit();
            return (check_them_nv > 0);

        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            try {
                if (sql_add_nv != null) sql_add_nv.close();
                if (sql_add_th != null) sql_add_th.close();
                if (sql_add_tk != null) sql_add_tk.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }

    public static ArrayList<item_nhanvien> getAllNhanVienList(String MaThuongHieu) {
        ArrayList<item_nhanvien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien nv Join TaiKhoan tk on nv.MaNV = tk.MaNV where nv.MaThuongHieu = ?";
        try (Connection conn = database.MyConnection.connect();
            PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1,MaThuongHieu);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new item_nhanvien(
                    rs.getString("MaNV"),
                    rs.getString("TenNV"),
                    rs.getString("ChucVu"),
                    rs.getFloat("Luong"),
                    rs.getDate("NgayVaoLam"),
                    rs.getDate("NgaySinh"),
                    rs.getString("TenDangNhap"),
                    rs.getString("MaThuongHieu")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public static item_nhanvien getThongTinNhanVien(String MaNV){
        String sql = "Select * from NhanVien nv join TaiKhoan tk on nv.MaNv = tk.MaNv where nv.MaNV = ?";
        try(Connection conn = database.MyConnection.connect();
            PreparedStatement load_info = conn.prepareStatement(sql)){
            
            load_info.setString(1,MaNV);
            ResultSet rs = load_info.executeQuery();
            if (rs.next()) { 
                return new item_nhanvien(
                    rs.getString("MaNV"),
                    rs.getString("TenNV"),
                    rs.getString("ChucVu"),
                    rs.getFloat("Luong"),
                    rs.getDate("NgayVaoLam"),
                    rs.getDate("NgaySinh"),
                    rs.getString("TenDangNhap"),
                    rs.getString("MaThuongHieu")
                );
            }
        }catch(Exception e){ e.printStackTrace(); }
        return null;
    }

    public static boolean capNhatNhanVien(String ma, String ten, java.sql.Date ngaySinh, String chucVu, float luong, String email) {
        Connection conn = null;
        try{
            conn = database.MyConnection.connect();
            conn.setAutoCommit(false);
            
            String sqlNv = "Update NhanVien set TenNV = ? , NgaySinh = ? , ChucVu = ? ,Luong = ? where MaNv = ? ";
            try (PreparedStatement uptNv = conn.prepareStatement(sqlNv)) {
                uptNv.setString(1,ten);
                uptNv.setDate(2,ngaySinh);
                uptNv.setString(3,chucVu);
                uptNv.setFloat(4,luong);
                uptNv.setString(5,ma);
                uptNv.executeUpdate();
            }

            String sqlTk = "Update TaiKhoan set TenDangNhap = ? where MaNv = ?";
            try (PreparedStatement uptTk = conn.prepareStatement(sqlTk)) {
                uptTk.setString(1,email);
                uptTk.setString(2,ma);
                uptTk.executeUpdate();
            }
            
            conn.commit();
            return true;
        }catch(Exception e){
            try{ if(conn != null) conn.rollback(); }catch(Exception ex){ ex.printStackTrace(); }
            return false;
        }finally{
            try{ if(conn != null) conn.close(); }catch(Exception exi){ exi.printStackTrace(); }
        }
    }

    public static boolean xoaNhanVien(String ma) {
        Connection conn = null;
        try {
            conn = database.MyConnection.connect();
            conn.setAutoCommit(false);
            String maThuongHieu = "";
            String sqlGetBrand = "SELECT MaThuongHieu FROM NhanVien WHERE MaNV = ?";
            try (PreparedStatement pst = conn.prepareStatement(sqlGetBrand)) {
                pst.setString(1, ma);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) maThuongHieu = rs.getString("MaThuongHieu");
            }
            try (PreparedStatement dltTk = conn.prepareStatement("DELETE FROM TaiKhoan where MaNv=?")) {
                dltTk.setString(1, ma);
                dltTk.executeUpdate();
            }
            try (PreparedStatement dltNv = conn.prepareStatement("DELETE FROM NhanVien WHERE MaNV=?")) {
                dltNv.setString(1, ma);
                dltNv.executeUpdate();
            }

            if (!maThuongHieu.isEmpty()) {
                String sqlCheck = "SELECT COUNT(*) FROM NhanVien WHERE MaThuongHieu = ?";
                try (PreparedStatement pstCheck = conn.prepareStatement(sqlCheck)) {
                    pstCheck.setString(1, maThuongHieu);
                    ResultSet rsCheck = pstCheck.executeQuery();
                    if (rsCheck.next() && rsCheck.getInt(1) == 0) {
                        try (PreparedStatement dltBrand = conn.prepareStatement("DELETE FROM ThuongHieu WHERE MaThuongHieu = ?")) {
                            dltBrand.setString(1, maThuongHieu);
                            dltBrand.executeUpdate();
                            System.out.println("Đã tự động xóa thương hiệu rỗng: " + maThuongHieu);
                        } catch (SQLException e) {
                            System.out.println("Không thể xóa thương hiệu do còn ràng buộc dữ liệu khác (Sản phẩm/Hóa đơn...)");
                        }
                    }
                }
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            try{ if(conn != null) conn.rollback(); }catch(Exception ex){ ex.printStackTrace(); }
            return false;
        }finally{
            try{ if(conn != null) conn.close(); }catch(Exception exi){ exi.printStackTrace(); }
        }
    }

    public static String getTenThuongHieu(String maThuongHieu) {
        String ten = "";
        String sql = "SELECT TenThuongHieu FROM ThuongHieu WHERE MaThuongHieu = ?";
        try (Connection conn = database.MyConnection.connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, maThuongHieu);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                ten = rs.getString("TenThuongHieu");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return ten.isEmpty() ? "Chưa cập nhật" : ten;
    }
}
