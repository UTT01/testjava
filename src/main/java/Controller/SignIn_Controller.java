/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author khoin
 */
import javax.swing.*;
import java.util.Arrays;
import java.awt.event.ActionListener;
import View.Folder_Login_UI.sign_in_UI;

public class SignIn_Controller {
    private final sign_in_UI ui;

    public SignIn_Controller(sign_in_UI ui) {
        this.ui = ui;
        initEvents();
    }

    private void initEvents() {

        ui.btnShow1.addActionListener(e ->
            ui.matkhau.setEchoChar(ui.btnShow1.isSelected() ? (char) 0 : '*')
        );

        ui.btnShow2.addActionListener(e ->
            ui.checkmatkhau.setEchoChar(ui.btnShow2.isSelected() ? (char) 0 : '*')
        );

        ui.btncancel.addActionListener(e -> ui.dispose());

        ui.btnadd.addActionListener(e -> Register());
    }

    private void Register() {
        if (!checkInput()) return;

        String tenn = ui.tennv.getText();
        String thuonghieu = ui.tenthuonghieu.getText();
        String dc = ui.diachi.getText();
        String gmail = ui.email.getText().trim().toLowerCase();
        String pass = new String(ui.matkhau.getPassword());

        if (database.nhan_vien_query.KiemTraTonTai(gmail, "TenDangNhap", "TaiKhoan")) {
            JOptionPane.showMessageDialog(ui, "Email đã tồn tại!");
            return;
        }

        if (database.nhan_vien_query.KiemTraTonTai(thuonghieu, "TenThuongHieu", "ThuongHieu")) {
            JOptionPane.showMessageDialog(ui, "Thương hiệu đã tồn tại!");
            return;
        }

        boolean ok = database.nhan_vien_query.addNhanVien(
            database.tool_linh_tinh.taoMaNhanVien("QL"),
            tenn,
            "Quản Lý",
            0f,
            new java.sql.Date(System.currentTimeMillis()),
            database.tool_linh_tinh.taoMaThuongHieu(),
            new java.sql.Date(ui.ngaysinh.getDate().getTime()),
            gmail,
            pass,
            thuonghieu,
            dc
        );

        if (ok) {
            JOptionPane.showMessageDialog(ui, "Đăng ký thành công!");
            ui.setSaved(true);
            ui.dispose();
        } else {
            JOptionPane.showMessageDialog(ui, "Đăng ký thất bại!");
        }
    }

    private boolean checkInput() {
        char[] p1 = ui.matkhau.getPassword();
        char[] p2 = ui.checkmatkhau.getPassword();

        if (p1.length < 6) {
            JOptionPane.showMessageDialog(ui, "Mật khẩu ít nhất 6 ký tự");
            return false;
        }
        if (!Arrays.equals(p1, p2)) {
            JOptionPane.showMessageDialog(ui, "Mật khẩu không khớp");
            return false;
        }
        if (!ui.email.getText().endsWith("@gmail.com")) {
            JOptionPane.showMessageDialog(ui, "Email phải là @gmail.com");
            return false;
        }
        if (ui.ngaysinh.getDate() == null) {
            JOptionPane.showMessageDialog(ui, "Chọn ngày sinh");
            return false;
        }
        return true;
    }
}
