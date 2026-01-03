/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

/**
 *
 * @author khoin
 */
import Constructor.item_nhanvien;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class tai_khoan_query {
    public static String checkLogin(String email, String passwordRaw) {
        String sql = "SELECT MaNV FROM TaiKhoan WHERE TenDangNhap = ? AND MatKhau = ?";
        try (Connection conn = MyConnection.connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            String hashedPass = database.tool_linh_tinh.hashPassword(passwordRaw);
            pst.setString(1, email);
            pst.setString(2, hashedPass);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getString("MaNV"); 
        } catch (Exception e) { e.printStackTrace(); }
        return null; 
    }

    public static boolean updatePassword(String email, String newPassRaw) {
        String sql = "UPDATE TaiKhoan SET MatKhau = ?, OTP = NULL WHERE TenDangNhap = ?";
        try (Connection conn = MyConnection.connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            String hashedPass = database.tool_linh_tinh.hashPassword(newPassRaw); 
            pst.setString(1, hashedPass);
            pst.setString(2, email);
            return pst.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public static boolean updateOTP(String email, int otp) {
        String sql = "UPDATE TaiKhoan SET OTP = ? WHERE TenDangNhap = ?"; 
        try (Connection conn = MyConnection.connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, otp);
            pst.setString(2, email);
            return pst.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public static boolean checkOTP(String email, int inputOtp) {
        String sql = "SELECT 1 FROM TaiKhoan WHERE TenDangNhap = ? AND OTP = ?";
        try (Connection conn = MyConnection.connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, email);
            pst.setInt(2, inputOtp);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public static void clearOTP(String email) {
        String sql = "UPDATE TaiKhoan SET OTP = NULL WHERE TenDangNhap = ?";
        try (Connection conn = MyConnection.connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, email);
            pst.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
