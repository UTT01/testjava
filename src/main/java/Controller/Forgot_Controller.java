/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import View.Folder_Login_UI.forgot_UI;

public class Forgot_Controller {
    
    private final forgot_UI ui;
    private String currentEmail = ""; 
    private Timer timer;
    private int secondsLeft = 60;

    public Forgot_Controller(forgot_UI ui) {
        this.ui = ui;
        initEvents();
    }

    private void initEvents() {

        ui.btnSendOTP.addActionListener(e -> handleSendOTP());

        ui.btnVerifyOTP.addActionListener(e -> handleVerifyOTP());

        ui.btnSavePass.addActionListener(e -> handleSavePassword());
    }


    private void handleSendOTP() {
        String email = ui.txtEmail.getText().trim().toLowerCase();
        
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(ui, "Vui lòng nhập Email!");
            return;
        }

        if (database.nhan_vien_query.KiemTraTonTai(email, "TenDangNhap", "TaiKhoan")) {
            this.currentEmail = email; 
            

            Random rand = new Random();
            int otp = 100000 + rand.nextInt(900000); 

            database.tai_khoan_query.updateOTP(currentEmail, otp);
            System.out.println("MÃ OTP CỦA BẠN LÀ: " + otp);
            JOptionPane.showMessageDialog(ui, "Mã OTP đã được gửi!");

            ui.showCard("cardOTP");
            startCountDown();
        } else {
            JOptionPane.showMessageDialog(ui, "Email này chưa đăng ký tài khoản!");
        }
    }

    private void handleVerifyOTP() {
        try {
            String otpText = ui.txtOTP.getText().trim();
            if (otpText.isEmpty()) {
                JOptionPane.showMessageDialog(ui, "Vui lòng nhập mã OTP!");
                return;
            }
            
            int inputOTP = Integer.parseInt(otpText);

            // Check DB
            if (database.tai_khoan_query.checkOTP(currentEmail, inputOTP)) {

                if (timer != null) timer.stop();
                ui.showCard("cardReset");
            } else {
                JOptionPane.showMessageDialog(ui, "Mã OTP không đúng!");
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(ui, "Mã OTP phải là số!");
        }
    }

    // --- XỬ LÝ BƯỚC 3 ---
    private void handleSavePassword() {
        String p1 = new String(ui.txtNewPass.getPassword());
        String p2 = new String(ui.txtConfirmPass.getPassword());

        if (p1.length() < 6) {
            JOptionPane.showMessageDialog(ui, "Mật khẩu phải từ 6 ký tự!");
            return;
        }
        if (!p1.equals(p2)) {
            JOptionPane.showMessageDialog(ui, "Mật khẩu xác nhận không khớp!");
            return;
        }

        // Cập nhật DB
        boolean ok = database.tai_khoan_query.updatePassword(currentEmail, p1);
        if (ok) {
            JOptionPane.showMessageDialog(ui, "Đổi mật khẩu thành công! Hãy đăng nhập lại.");
            ui.dispose(); // Đóng form
        } else {
            JOptionPane.showMessageDialog(ui, "Lỗi hệ thống! Vui lòng thử lại.");
        }
    }

    // --- HÀM ĐẾM NGƯỢC ---
    private void startCountDown() {
        secondsLeft = 60;
        ui.lblCountdown.setText("Thời Gian Hiệu Lưc: " + secondsLeft + "s");
        
        if (timer != null && timer.isRunning()) timer.stop();

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsLeft--;
                ui.lblCountdown.setText("Hiệu lực: " + secondsLeft + "s");

                if (secondsLeft <= 0) {
                    timer.stop();
                    ui.lblCountdown.setText("Mã đã hết hạn!");
                    database.tai_khoan_query.clearOTP(currentEmail);
                    JOptionPane.showMessageDialog(ui, "Hết thời gian! Vui lòng thử lại.");

                    ui.showCard("cardEmail");
                }
            }
        });
        timer.start();
    }
}
