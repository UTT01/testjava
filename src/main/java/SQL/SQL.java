/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SQL;

import SQL.TestJDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.naming.spi.DirStateFactory;

/**
 *
 * @author ducha
 */
public class SQL {
    public TestJDBC testJDBC = new TestJDBC();
    public static void main(String[] args) {
     
  }
    public void InsertSanPham(String username, String email, int phone){
        int k = 0;
        ArrayList<Client> client = new ArrayList<>();
        try {
            Connection con = testJDBC.connect();
            String sql = "INSERT INTO CLIENT (username, email, phone) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setInt(3, phone);
            k = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
             if (k > 0) {
                System.out.println("Thêm thành công: ");
                if (rs.next()) {
                k = rs.getInt(1);
            }
            System.out.println("Inserted record's ID: " + k);
            }else{
                System.out.println("Thêm thất bại");
            }
            
        } catch (Exception e) {
            System.err.println("Lỗi: "+e);
    }        
  }
    public void getTH(){   
        try {
            Connection con = testJDBC.connect();
            String sql = "SELECT * FROM CLIENT";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs != null && rs.next()){
                int maTH = rs.getInt("id");
                String tenTH = rs.getString("username");
                String diaChi = rs.getString("email");
                int phone = rs.getInt("phone");
                System.out.println("Mã TH: "+ maTH + "Tên TH: "+tenTH +"Địa chỉ Email: "+diaChi + "SDT: "+ phone);
            }     
        } catch (Exception e) {
        }
    }
    
    public void UpdateClient(int id, String email){
        try {
             Connection con = testJDBC.connect();
             String sql = "UPDATE CLIENT SET EMAIL = ? WHERE ID = ?";
             PreparedStatement ps = con.prepareStatement(sql);
             ps.setString(1, email);
             ps.setInt(2, id);
             ps.executeUpdate();
             System.out.println("Update thanh cong");
             
        } catch (Exception e) {
            System.out.println("Loi update: "+e);
        }
    }
    
    public void DeleteClient(int id){
        try {
             Connection con = testJDBC.connect();
             String sql = "DELETE CLIENT WHERE ID = ?";
             PreparedStatement ps = con.prepareStatement(sql);
             ps.setInt(1, id);
             ps.executeUpdate();
             System.out.println("Delete thanh cong");
             
        } catch (Exception e) {
            System.out.println("Loi Delete: "+e);
        }
    }
   

    
}
