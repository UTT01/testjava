/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Constructor;

import java.util.Date;

/**
 *
 * @author khoin
 */
public class item_nhanvien {
    private String maNV;
    private String tenNV;
    private Date ngaySinh;
    private Date ngayVaoLam;
    private String chucVu;
    private float luong;
    private String email;
    private String maThuongHieu;

    // 2. Constructor (Hàm khởi tạo)
    public item_nhanvien() {
    }

    public item_nhanvien(String maNV, String tenNV, String chucVu, float luong, Date ngayVaoLam, Date ngaySinh,  String email,String maThuongHieu) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.ngaySinh = ngaySinh;
        this.chucVu = chucVu;
        this.luong = luong;
        this.email = email;
        this.ngayVaoLam = ngayVaoLam;
        this.maThuongHieu = maThuongHieu;
    }

    public String getMaThuongHieu() {
        return maThuongHieu;
    }

    // 3. Getter và Setter (Để lấy và gán dữ liệu)
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }

    public float getLuong() { return luong; }
    public void setLuong(float luong) { this.luong = luong; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    

    public Date getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(Date ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }
    
    // toString để debug nếu cần
    @Override
    public String toString() {
        return tenNV + " - " + chucVu;
    }
}


