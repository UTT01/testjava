/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

/**
 *
 * @author khoin
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MyConnection {

    private static final String IP = "127.0.0.1";       // Địa chỉ máy cài SQL Server
    private static final String PORT = "1433";          // Cổng mặc định SQL Server
    private static final String DB = "QUANLYCAFE";      // Tên database
    private static final String USER = "sa";            // Tài khoản SQL Server
    private static final String PASSWORD = "123456";    // Mật khẩu SQL Server
    public static Connection connect() {
        Connection conn = null;
        try {
            // TAI DRIVER
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Dinh nghia URL
            String connectionUrl = "jdbc:sqlserver://" + IP + ":" + PORT + 
                                   ";databaseName=" + DB + 
                                   ";encrypt=true;trustServerCertificate=true;";

            // Kết nối với user/password
            conn = DriverManager.getConnection(connectionUrl, USER, PASSWORD);
           
        } catch (Exception e) {
           
            e.printStackTrace();
        }
        return conn;
    }

    public static void testQuery() {
        try (Connection conn = connect()) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT TOP 5 TenSP, DonGia FROM SANPHAM");

                while (rs.next()) {
                    String tenMon = rs.getString("TenSP");
                    int gia = rs.getInt("DonGia");
                    System.out.println(tenMon + " - " + gia);
                }

            } else {
                System.out.println("Kết nối thất bại (conn == null)");
            }
        } catch (Exception e) {
            System.out.println("Lỗi truy vấn: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
        public static void main(String[] args) {
        testQuery();
    }
}
