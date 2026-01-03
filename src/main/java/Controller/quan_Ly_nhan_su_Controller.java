/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Constructor.item_nhanvien;
import com.toedter.calendar.JDateChooser;
import database.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author khoin
 */
public class quan_Ly_nhan_su_Controller {
     private item_nhanvien nvDangMoForm;
     public quan_Ly_nhan_su_Controller(item_nhanvien nvDangMoForm){
        this.nvDangMoForm = nvDangMoForm;
    }
    public void loadData(DefaultTableModel model) {
        new Thread(()->{
           model.setRowCount(0);
        ArrayList<item_nhanvien> list =
                nhan_vien_query.getAllNhanVienList(nvDangMoForm.getMaThuongHieu());

        for (item_nhanvien nv : list) {
            model.addRow(new Object[]{
                    nv.getMaNV(),
                    nv.getTenNV(),
                    nv.getNgaySinh(),
                    nv.getChucVu(),
                    nv.getLuong(),
                    nv.getEmail()
            });
        } 
        }).start();
    }

    // ================= VALIDATE =================
    public boolean validateInput(JTextField ten, JTextField luong, JDateChooser ngaySinh) {
        if (ten.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên không được để trống!");
            return false;
        }
        if (ngaySinh.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Chưa chọn ngày sinh!");
            return false;
        }
        try {
            Float.parseFloat(luong.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lương phải là số!");
            return false;
        }
        return true;
    }

    public boolean addNhanVien(
            JTextField ten,
            JTextField luong,
            JTextField email,
            JDateChooser ngaySinh,
            String chucVu) 
    {
        if (nhan_vien_query.KiemTraTonTai(email.getText(), "TenDangNhap", "TaiKhoan")) {
            JOptionPane.showMessageDialog(null, "Email đã tồn tại!");
            return false;
        }

        String maCV = getMaChucVu(chucVu);
        String maNV = database.tool_linh_tinh.taoMaNhanVien(maCV);

        return nhan_vien_query.addNhanVien(
                maNV,
                ten.getText(),
                chucVu,
                Float.parseFloat(luong.getText()),
                new java.sql.Date(System.currentTimeMillis()),
                nvDangMoForm.getMaThuongHieu(),
                new java.sql.Date(ngaySinh.getDate().getTime()),
                email.getText(),
                "123456",
                "",
                ""
        );
    }

    // ================= UPDATE =================
    public boolean updateNhanVien(
            JTextField ma,
            JTextField ten,
            JTextField luong,
            JTextField email,
            JDateChooser ngaySinh,
            String chucVu
    ) {
        return nhan_vien_query.capNhatNhanVien(
                ma.getText(),
                ten.getText(),
                new java.sql.Date(ngaySinh.getDate().getTime()),
                chucVu,
                Float.parseFloat(luong.getText()),
                email.getText()
        );
    }

    // ================= DELETE =================
    public boolean deleteNhanVien(String maNV) {
        return nhan_vien_query.xoaNhanVien(maNV);
    }

   
    public String getMaChucVu(String ten) {
        switch (ten) {
            case "Quản lý": return "QL";
            case "Nhân Viên Pha Chế": return "NV_PC";
            case "Nhân Viên Bàn": return "NV_BAN";
            default: return "NV_QUAY";
        }
    }
}
