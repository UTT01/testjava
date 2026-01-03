/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author khoin
 */
public class tool_linh_tinh {
    //mã hóa
    public static String hashPassword(String password) {
        try {
            
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0'); 
                }
                hexString.append(hex);
            }
            return hexString.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //tạo mã nhân nhân viên
    public static String taoMaNhanVien(String loaiNhanVien) {
        String prefix = "QL";
        if(loaiNhanVien.equalsIgnoreCase("QL")){
            prefix = "QL";
        }else if(loaiNhanVien.equalsIgnoreCase("NV_PC")){
            prefix = "NV_PC";
        }else if(loaiNhanVien.equalsIgnoreCase("NV_BAN")){
            prefix = "NV_BAN";
        }else{
            prefix = "NV_QUAY";
        }
        String sql = "SELECT TOP 1 MaNV FROM NhanVien WHERE MaNV LIKE ? ORDER BY MaNV DESC";
        
        try (Connection conn = database.MyConnection.connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, prefix + "%"); 
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String maHienTai = rs.getString("MaNV");
                String phanSo = maHienTai.substring(prefix.length());
                int soTiepTheo = Integer.parseInt(phanSo) + 1;
                return String.format("%s%03d", prefix, soTiepTheo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prefix + "0001";
    }
    //tạo mã thương hiệu
    public static String taoMaThuongHieu() {
        String prefix = "TH";
        String sql = "SELECT TOP 1 MaThuongHieu FROM ThuongHieu " +
                 "ORDER BY CAST(SUBSTRING(MaThuongHieu, 3, LEN(MaThuongHieu)) AS INT) DESC";
        try (Connection conn = database.MyConnection.connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int soTiepTheo = Integer.parseInt(rs.getString("MaThuongHieu").substring(2)) + 1;
                return String.format("%s%04d", prefix, soTiepTheo);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return prefix + "0001";
    }
    //tạo mã tài khoản
    public static String taoMaTaiKhoan() {
        String prefix = "TK";
        String sql = "SELECT TOP 1 MaTK FROM TaiKhoan WHERE MaTK LIKE 'TK%' ORDER BY LEN(MaTK) DESC, MaTK DESC";
    
        try (Connection conn = database.MyConnection.connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String maHienTai = rs.getString("MaTK");
                String phanSo = maHienTai.substring(prefix.length()); 
                long soTiepTheo = Long.parseLong(phanSo) + 1;

                return String.format("%s%04d", prefix, soTiepTheo);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo mã tài khoản: ");
            e.printStackTrace();
        }
        return prefix + "0001";
    }
}
