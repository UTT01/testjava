/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SQL;
import SQL.SQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
/**
 *
 * @author ducha
 */
public class Main {
    public static void main(String[] args) {
        SQL sqlInsert = new SQL();
        String tenSP = "Phamj Khoi Nguyen";
        String email= "khoinguyen@gmail.com";     
        int sdt = 1111111111;
       // sqlInsert.InsertSanPham(tenSP, email, sdt);
        sqlInsert.getTH();
        sqlInsert.UpdateClient(7, "utt@gmail.");
       // sqlInsert.DeleteClient(4);
       // sqlInsert.getTH(); 
    }

}
