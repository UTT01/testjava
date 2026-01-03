/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 *//*
 * Package: database
 */
package database;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class cham_cong_query {
    
    // Hàm xác định ca hiện tại dựa trên giờ hệ thống để tạo MaCa
    // Quy ước: Sáng (6h-12h), Chiều (12h-18h), Tối (18h-23h)
    public static String getMaCaHienTai() {
        LocalTime now = LocalTime.now();
        String prefix = "";
        if (now.isAfter(LocalTime.of(5, 59)) && now.isBefore(LocalTime.of(12, 0))) prefix = "S";
        else if (now.isAfter(LocalTime.of(11, 59)) && now.isBefore(LocalTime.of(18, 0))) prefix = "C";
        else if (now.isAfter(LocalTime.of(17, 59))) prefix = "T";
        else return null;
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
        return prefix + sdf.format(new Date());
    }
    public static int layMaCCDangLam(String maNV, String maCa) {
        String sql = "SELECT MaCC FROM CHAMCONG WHERE MaNV = ? AND MaCa = ? AND ThoiGianKetThuc IS NULL";
        try (Connection conn = MyConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            ps.setString(2, maCa);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("MaCC");
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }
    // Kiểm tra xem nhân viên đã check-in ca hiện tại chưa
    // Trả về: Object[] {MaCC, ThoiGianBatDau, TrangThai} nếu tìm thấy, null nếu chưa
    public static Timestamp layThoiGianBatDau(int maCC) {
        String sql = "SELECT ThoiGianBatDau FROM CHAMCONG WHERE MaCC = ?";
        try (Connection conn = MyConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maCC);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getTimestamp("ThoiGianBatDau");
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // Thực hiện Check-in (Bắt đầu làm)
    public static boolean checkIn(String maNV, String maCa) {
        String sql = "INSERT INTO CHAMCONG (MaNV, MaCa, ThoiGianBatDau, TrangThai) VALUES (?, ?, GETDATE(), N'Đang làm')";
        try (Connection conn = MyConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            ps.setString(2, maCa);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // Thực hiện Check-out (Tan làm)
    public static boolean checkOut(int maCC) {
        String sql = "UPDATE CHAMCONG SET ThoiGianKetThuc = GETDATE(), " +
                     "SoGioLam = DATEDIFF(MINUTE, ThoiGianBatDau, GETDATE()) / 60.0, " +
                     "TrangThai = N'Hoàn thành' WHERE MaCC = ?";
        try (Connection conn = MyConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maCC);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
    public static ArrayList<Object[]> getLichSuChamCongTheoThang(String maNV, int thang, int nam) {
    ArrayList<Object[]> list = new ArrayList<>();
    // Join bảng CHAMCONG với CA để lấy ngày, Join với NHANVIEN để lấy Lương cơ bản (nếu cần hiển thị lại)
    String sql = "SELECT c.Ngay, c.Ca, cc.ThoiGianBatDau, cc.ThoiGianKetThuc, cc.SoGioLam, cc.TrangThai " +
                 "FROM CHAMCONG cc " +
                 "JOIN CA c ON cc.MaCa = c.MaCa " +
                 "WHERE cc.MaNV = ? AND MONTH(c.Ngay) = ? AND YEAR(c.Ngay) = ? " +
                 "ORDER BY c.Ngay DESC";
    
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    try (Connection conn = MyConnection.connect();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, maNV);
        ps.setInt(2, thang);
        ps.setInt(3, nam);
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            Timestamp start = rs.getTimestamp("ThoiGianBatDau");
            Timestamp end = rs.getTimestamp("ThoiGianKetThuc");
            float soGio = rs.getObject("SoGioLam") != null ? rs.getFloat("SoGioLam") : 0;

            list.add(new Object[]{
                dateFormat.format(rs.getDate("Ngay")),    // 0: Ngày
                rs.getString("Ca"),                       // 1: Ca
                timeFormat.format(start),                 // 2: Giờ vào
                (end != null ? timeFormat.format(end) : "--:--"), // 3: Giờ ra
                soGio,                                    // 4: Số giờ làm (float)
                rs.getString("TrangThai")                 // 5: Trạng thái
            });
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

    // Lấy lịch sử làm việc trong tháng hiện tại để hiển thị lên bảng
    public static ArrayList<Object[]> getLichSuChamCongThang(String maNV) {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT c.Ngay, c.Ca, cc.ThoiGianBatDau, cc.ThoiGianKetThuc, cc.SoGioLam, cc.TrangThai " +
                     "FROM CHAMCONG cc " +
                     "JOIN CA c ON cc.MaCa = c.MaCa " +
                     "WHERE cc.MaNV = ? AND MONTH(c.Ngay) = MONTH(GETDATE()) AND YEAR(c.Ngay) = YEAR(GETDATE()) " +
                     "ORDER BY c.Ngay DESC";
        
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try (Connection conn = MyConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            ResultSet rs = ps.executeQuery();
            int stt = 1;
            while (rs.next()) {
                Timestamp start = rs.getTimestamp("ThoiGianBatDau");
                Timestamp end = rs.getTimestamp("ThoiGianKetThuc");
                
                list.add(new Object[]{
                    stt++,
                    dateFormat.format(rs.getDate("Ngay")),
                    rs.getString("Ca"),
                    timeFormat.format(start),
                    (end != null ? timeFormat.format(end) : "--:--:--"),
                    (rs.getObject("SoGioLam") != null ? rs.getFloat("SoGioLam") + " giờ" : "---"),
                    rs.getString("TrangThai")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static Set<Integer> getNgayCoDuLieuChamCong(String maNV, int thang, int nam) {
        Set<Integer> days = new HashSet<>();
        String sql = "SELECT DAY(c.Ngay) as Ngay FROM CHAMCONG cc JOIN CA c ON cc.MaCa = c.MaCa " +
                     "WHERE cc.MaNV = ? AND MONTH(c.Ngay) = ? AND YEAR(c.Ngay) = ?";
        try (Connection conn = MyConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            ps.setInt(2, thang);
            ps.setInt(3, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                days.add(rs.getInt("Ngay"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return days;
    }
}
