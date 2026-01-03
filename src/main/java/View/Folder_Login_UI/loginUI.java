
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.Folder_Login_UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.Label;

/**
 *
 * @author khoin
 */
public class loginUI extends JFrame{
    JPanel thongtinchinh;
    JTextField txtUser;
    JPasswordField txtPass;
    JButton login,signIn;
    JLabel lbForgotPass;
    public loginUI(){
        this.setTitle("Đăng Nhập");
        this.setLayout(new BorderLayout());
        this.setSize(800,600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        createUI();
        addEvents();
        this.add(thongtinchinh,BorderLayout.CENTER);
        this.setVisible(true);
    }
    public void createUI(){
        thongtinchinh = new JPanel(new GridBagLayout());
        thongtinchinh.setBackground(new Color(240, 240, 240)); 

        JPanel formLoginBox = new JPanel();
        formLoginBox.setLayout(new BoxLayout(formLoginBox, BoxLayout.Y_AXIS)); 
        formLoginBox.setBackground(Color.WHITE);    
      
        formLoginBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), 
                BorderFactory.createEmptyBorder(20, 30, 20, 30) 
        ));

       
        JLabel nhanchinh = new JLabel("ĐĂNG NHẬP");
        nhanchinh.setFont(new Font("Arial", Font.BOLD, 24));
        nhanchinh.setAlignmentX(Component.CENTER_ALIGNMENT); 

        JPanel nhapthongtin = new JPanel(new GridLayout(2, 2, 10, 15)); 
        nhapthongtin.setBackground(Color.WHITE);
        nhapthongtin.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); 
        JLabel nhapUser = new JLabel("Tài khoản đăng nhập:");
        nhapUser.setHorizontalAlignment(SwingConstants.RIGHT);
        txtUser = new JTextField(15); 

        JLabel nhapPass = new JLabel("Mật khẩu đăng nhập:");
        nhapPass.setHorizontalAlignment(SwingConstants.RIGHT);
        txtPass = new JPasswordField(15);
        txtPass.setBorder(null);
        txtPass.setEchoChar('*');

        JToggleButton btnShow = new JToggleButton("👁");
        btnShow.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        btnShow.setContentAreaFilled(false);
        btnShow.setFocusPainted(false);
        btnShow.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel PassWordWrapper = new JPanel(new BorderLayout());
        PassWordWrapper.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        PassWordWrapper.setBackground(Color.WHITE);
        PassWordWrapper.add(txtPass, BorderLayout.CENTER);
        PassWordWrapper.add(btnShow, BorderLayout.EAST);
        btnShow.addActionListener(e -> {
            if (btnShow.isSelected()) {
                txtPass.setEchoChar((char) 0);
            } else {
                txtPass.setEchoChar('*');
            }
        });

        nhapthongtin.add(nhapUser);
        nhapthongtin.add(txtUser);
        nhapthongtin.add(nhapPass);
        nhapthongtin.add(PassWordWrapper);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);
        Dimension customsBtn = new Dimension(100, 30);
        login = new JButton("Đăng Nhập");
        login.setPreferredSize(customsBtn);
        signIn = new JButton("Đăng Ký");
        signIn.setPreferredSize(customsBtn);
        btnPanel.add(login);
        btnPanel.add(signIn);

  
        String textLink = "<html><i><small>Bạn không nhớ mật khẩu? <font color='blue'><u>Quên mật khẩu</u></font></small></i></html>";
        lbForgotPass = new JLabel(textLink);
        lbForgotPass.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbForgotPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        linkPanel.setBackground(Color.WHITE);
        linkPanel.add(lbForgotPass);

  
        formLoginBox.add(nhanchinh);
        formLoginBox.add(Box.createVerticalStrut(10));
        formLoginBox.add(nhapthongtin);
        formLoginBox.add(btnPanel);
        formLoginBox.add(Box.createVerticalStrut(10));
        formLoginBox.add(linkPanel);

        thongtinchinh.add(formLoginBox);
    }
    public void addEvents(){
        signIn.addActionListener(l->{
            sign_in_UI formdangky = new sign_in_UI(loginUI.this);
            formdangky.setVisible(true);
            System.out.println("Mở Form Đăng Ký");
        });
        lbForgotPass.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new forgot_UI(loginUI.this).setVisible(true);            
                System.out.println("Mở Form Lấy Mật Khẩu");
            }
        });
        login.addActionListener(l->{
            String email = txtUser.getText().trim().toLowerCase();
            String pass = new String(txtPass.getPassword());

            String maNV = database.tai_khoan_query.checkLogin(email, pass);

            if (maNV != null) {
                new View.Giao_Dien_Chinh_UI(maNV).setVisible(true); 

                this.dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!");
            }
        });
    }
    public static void main(String[] args){
        new loginUI();
    }
}
