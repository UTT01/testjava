/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.Folder_Login_UI;

import Controller.Forgot_Controller;
import java.awt.*;
import javax.swing.*;

public class forgot_UI extends JDialog {

    public JPanel mainPanel;
    public CardLayout cardLayout;

    // Các biến public để Controller gọi
    public JTextField txtEmail;
    public JTextField txtOTP;
    public JPasswordField txtNewPass, txtConfirmPass;
    public JButton btnSendOTP, btnVerifyOTP, btnSavePass;
    public JLabel lblCountdown;
    public JButton btnShowPass, btnShowConfirm;
    public JToggleButton btnshow1,btnshow2;

    public forgot_UI(JFrame parent) {
        super(parent, true); // Modal
        this.setTitle("Quên Mật Khẩu");
        this.setSize(450, 350);
        this.setLocationRelativeTo(parent);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        createUI();
        
        new Forgot_Controller(this);
        
        this.add(mainPanel);
    }

    private void createUI() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        Font titleFont = new Font("Arial", Font.BOLD, 18);
        Font labelFont = new Font("Arial", Font.PLAIN, 13); 
        Dimension inputSize = new Dimension(250, 36);

        JPanel pnlEmail = new JPanel();
        pnlEmail.setLayout(new BoxLayout(pnlEmail, BoxLayout.Y_AXIS));
        pnlEmail.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel lbl1 = new JLabel("Nhập Email đã đăng ký");
        lbl1.setFont(titleFont);
        lbl1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(labelFont);

        txtEmail = new JTextField();
        txtEmail.setPreferredSize(inputSize);
        txtEmail.setMaximumSize(inputSize);

        btnSendOTP = new JButton("Gửi Mã OTP");
        btnSendOTP.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlEmail.add(lbl1);
        pnlEmail.add(Box.createVerticalStrut(25));
        pnlEmail.add(lblEmail);
        pnlEmail.add(txtEmail);
        pnlEmail.add(Box.createVerticalStrut(20));
        pnlEmail.add(btnSendOTP);

        JPanel pnlOTP = new JPanel();
        pnlOTP.setLayout(new BoxLayout(pnlOTP, BoxLayout.Y_AXIS));
        pnlOTP.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel lbl2 = new JLabel("Bước 2: Nhập Mã OTP");
        lbl2.setFont(titleFont);
        lbl2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblOTP = new JLabel("Mã OTP (6 số)");
        lblOTP.setFont(labelFont);
        lblOTP.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtOTP = new JTextField();
        txtOTP.setPreferredSize(inputSize);
        txtOTP.setMaximumSize(inputSize);
        txtOTP.setHorizontalAlignment(JTextField.CENTER);

        JPanel otpFieldPanel = new JPanel();
        otpFieldPanel.setOpaque(false);
        otpFieldPanel.add(txtOTP);

        // Countdown
        lblCountdown = new JLabel("Hiệu lực: 55s");
        lblCountdown.setForeground(Color.RED);
        lblCountdown.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button
        btnVerifyOTP = new JButton("Xác Nhận OTP");
        btnVerifyOTP.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add
        pnlOTP.add(lbl2);
        pnlOTP.add(Box.createVerticalStrut(20));
        pnlOTP.add(lblOTP);
        pnlOTP.add(Box.createVerticalStrut(5));
        pnlOTP.add(otpFieldPanel);
        pnlOTP.add(Box.createVerticalStrut(10));
        pnlOTP.add(lblCountdown);
        pnlOTP.add(Box.createVerticalStrut(15));
        pnlOTP.add(btnVerifyOTP);

        // ================= CARD 3: RESET PASS =================
        JPanel pnlReset = new JPanel();
        pnlReset.setLayout(new BoxLayout(pnlReset, BoxLayout.Y_AXIS));
        pnlReset.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel lbl3 = new JLabel("Bước 3: Đặt Lại Mật Khẩu");
        lbl3.setFont(titleFont);
        lbl3.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNewPass = new JLabel("Tiến Hành Nhập Mật khẩu mới");
        lblNewPass.setFont(labelFont);
        
        //mắt 1 
        btnshow1 = new JToggleButton("👁");
        btnshow1.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
        btnshow1.setContentAreaFilled(false);
        btnshow1.setFocusPainted(false);
        btnshow1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        //mắt 2
        btnshow2 = new JToggleButton("👁");
        btnshow2.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
        btnshow2.setContentAreaFilled(false);
        btnshow2.setFocusPainted(false);
        btnshow2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        
        txtNewPass = new JPasswordField();
        txtNewPass.setPreferredSize(inputSize);
        txtNewPass.setMaximumSize(inputSize);
        
        //JPanel chứa mật khẩu và mắt để xem mật khẩu 
        JPanel nhapmatkhau = new JPanel();
        nhapmatkhau.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        nhapmatkhau.setBackground(Color.white);
        nhapmatkhau.add(txtNewPass,BorderLayout.CENTER);
        nhapmatkhau.add(btnshow1,BorderLayout.EAST);
        
        JLabel lblConfirm = new JLabel("Tiến Hành Nhập lại mật khẩu");
        lblConfirm.setFont(labelFont);
        
        txtConfirmPass = new JPasswordField();
        txtConfirmPass.setPreferredSize(inputSize);
        txtConfirmPass.setMaximumSize(inputSize);
        
        JPanel xacnhanmatkhau = new JPanel();
        xacnhanmatkhau.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        xacnhanmatkhau.setBackground(Color.white);
        xacnhanmatkhau.add(txtConfirmPass,BorderLayout.CENTER);
        xacnhanmatkhau.add(btnshow2,BorderLayout.EAST);
        
        btnSavePass = new JButton("Lưu Mật Khẩu Mới");
        btnSavePass.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlReset.add(lbl3);
        pnlReset.add(Box.createVerticalStrut(25));
        pnlReset.add(lblNewPass);
        pnlReset.add(nhapmatkhau);
        pnlReset.add(Box.createVerticalStrut(10));
        pnlReset.add(lblConfirm);
        pnlReset.add(xacnhanmatkhau);
        pnlReset.add(Box.createVerticalStrut(20));
        pnlReset.add(btnSavePass);

        mainPanel.add(pnlEmail, "cardEmail");
        mainPanel.add(pnlOTP, "cardOTP");
        mainPanel.add(pnlReset, "cardReset");
    }
    
    public void showCard(String cardName) {
        cardLayout.show(mainPanel, cardName);
    }
}
