/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Constructor;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class CTHoaDon implements Serializable{
    int mahd, maban, soluong;
     String tenmon;
     String giovao;
     String giora;
     int tongtien;
     String phuongthuc;
     int donGiaMon;
     int thanhTien = soluong * donGiaMon;

    public int getThanhTien() {
        return thanhTien;
    }

    public int getTongtien() {
        return tongtien;
    }

    public int getDonGiaMon() {
        return donGiaMon;
    }

    public String getPhuongthuc() {
        return phuongthuc;
    }

    public String getGiora() {
        return giora;
    }

    public String getGiovao() {
        return giovao;
    }

    public CTHoaDon(int mahd, int maban, String giovao, String giora, String phuongthuc, int tongtien, String tenmon, int soluong, int donGiaMon, int thanhTien){
        this.mahd = mahd;
        this.maban = maban;
        this.giovao = giovao;
        this.giora = giora;
        this.phuongthuc = phuongthuc;
        this.tongtien = tongtien;
        this.tenmon = tenmon;
        this.soluong = soluong;
        this.donGiaMon = donGiaMon;
        this.thanhTien = thanhTien;
    }

    public int getMahd() {
        return mahd;
    }

    public int getMaban() {
        return maban;
    }

    public int getSoluong() {
        return soluong;
    }

    public String getTenmon() {
        return tenmon;
    }
}
