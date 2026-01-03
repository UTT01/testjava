/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.Folder_Login_UI;

import Controller.SignIn_Controller;
import java.awt.*;
import java.awt.Font;
import java.awt.Label;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.util.Arrays;
/**
 *
 * @author khoin
 */
public class sign_in_UI extends JDialog{
    public JPanel FormNhapThongTin;
    public JTextField tennv,tenthuonghieu,diachi,email;
    public JDateChooser ngaysinh;
    public JPasswordField matkhau,checkmatkhau;
    public JToggleButton btnShow1,btnShow2;
    public JButton btnadd, btncancel;
    boolean isSaved = false; 
    public sign_in_UI(JFrame parent){
        super(parent,true);
        this.setTitle("Đăng Ký");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(400,600);
        this.setLocationRelativeTo(parent);
        CreateUI();
        new SignIn_Controller(this);
        this.add(FormNhapThongTin,BorderLayout.CENTER);   
    }
    public void CreateUI(){
        JLabel tieude = new JLabel("Đăng Ký Thương Hiệu");
        tieude.setFont(new Font("Arial",Font.BOLD,23));
        tieude.setHorizontalAlignment(JLabel.CENTER);
        
        
        FormNhapThongTin = new JPanel();
        FormNhapThongTin.setLayout(new BoxLayout(FormNhapThongTin, BoxLayout.Y_AXIS));
        
        JPanel nhapdulieu = new JPanel(new GridLayout(8,2,5,5));
        JLabel nhapten = new JLabel("Họ Tên: ");
        tennv = new JTextField(15);
        
        JLabel nhapngaysinh = new JLabel("Ngày Sinh: ");
        ngaysinh = new JDateChooser();
        ngaysinh.setDateFormatString("dd/MM/yyyy");
        
        JLabel nhapdiachi = new JLabel("Địa Chỉ: ");
        diachi = new JTextField(15);
        
        JLabel nhapthuonghieu = new JLabel("Tên Thương Hiệu: ");
        tenthuonghieu = new JTextField(15);
        
        
        JLabel nhapEmail = new JLabel("Email: ");
        email = new JTextField(15);
        //mắt 1
        btnShow1 = new JToggleButton("👁");
        btnShow1.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        btnShow1.setContentAreaFilled(false);
        btnShow1.setFocusPainted(false);
        btnShow1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        //mắt 2
        btnShow2 = new JToggleButton("👁");
        btnShow2.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        btnShow2.setContentAreaFilled(false);
        btnShow2.setFocusPainted(false);
        btnShow2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JLabel nhapmatkhau = new JLabel("Mật Khẩu: ");
        matkhau = new JPasswordField(15);
        matkhau.setBorder(null);
        matkhau.setEchoChar('*');
        
        JPanel NhapMatKhau = new JPanel();
        NhapMatKhau.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        NhapMatKhau.setBackground(Color.white);
        NhapMatKhau.add(matkhau,BorderLayout.CENTER);
        NhapMatKhau.add(btnShow1,BorderLayout.EAST);
        
        JLabel nhaplaimatkhau = new JLabel("Nhập Lại Mật Khẩu: ");
        checkmatkhau = new JPasswordField(15);
        checkmatkhau.setBorder(null);
        checkmatkhau.setEchoChar('*');
        
        JPanel NhapLaiMatKhau = new JPanel();
        NhapLaiMatKhau.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        NhapLaiMatKhau.setBackground(Color.white);
        NhapLaiMatKhau.add(checkmatkhau,BorderLayout.CENTER);
        NhapLaiMatKhau.add(btnShow2,BorderLayout.EAST);
        
        nhapdulieu.add(nhapthuonghieu);nhapdulieu.add(tenthuonghieu);
        nhapdulieu.add(nhapdiachi);nhapdulieu.add(diachi);
        nhapdulieu.add(nhapten);nhapdulieu.add(tennv);
        nhapdulieu.add(nhapngaysinh);nhapdulieu.add(ngaysinh);
        nhapdulieu.add(nhapEmail);nhapdulieu.add(email);
        nhapdulieu.add(nhapmatkhau);nhapdulieu.add(NhapMatKhau);
        nhapdulieu.add(nhaplaimatkhau);nhapdulieu.add(NhapLaiMatKhau);

        JPanel ChuyenDoiNhapDuLieu = new JPanel();
        ChuyenDoiNhapDuLieu.add(nhapdulieu,BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel();
        Dimension custombtn = new Dimension(80,25);
        btnadd = new JButton("Thêm");
        btnadd.setPreferredSize(custombtn);
        btncancel = new JButton("Hủy");
        btncancel.setPreferredSize(custombtn); 
        btnPanel.add(btnadd);
        btnPanel.add(btncancel);
        
        FormNhapThongTin.add(tieude);
        FormNhapThongTin.add(Box.createHorizontalStrut(10));
        FormNhapThongTin.add(ChuyenDoiNhapDuLieu);
        FormNhapThongTin.add(Box.createHorizontalStrut(10));
        FormNhapThongTin.add(btnPanel);
    }

    public boolean isIsSaved() {
        return isSaved;
    }
    public void setSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }
}
