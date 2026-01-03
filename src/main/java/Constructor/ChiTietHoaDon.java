/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Constructor;
import java.io.Serializable;

public class ChiTietHoaDon implements Serializable {
    private int maSP;
    private int soLuong;
    private String tenSP; // đồng nhất với các class khác (SanPham, adapter, SQL...)

    // Constructor mặc định (cần cho khi đọc từ SQL)
    public ChiTietHoaDon() {}

    // Constructor đầy đủ
    public ChiTietHoaDon(int maSP, int soLuong, String tenSP) {
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.tenSP = tenSP;
    }

    // Getter - Setter
    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                "maSP=" + maSP +
                ", soLuong=" + soLuong +
                ", tenSP='" + tenSP + '\'' +
                '}';
    }
}

