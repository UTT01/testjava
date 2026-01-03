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
public class HoaDon implements Serializable{
    String MaHD;
    String MaBan;
    String GioVao;
    String GioRa;
    int TongTien;
    String PhuongThuc;
    String maca;
    public HoaDon(String MaHD, String MaBan, String GioVao, String GioRa, int TongTien, String PhuongThuc, String maca){
        this.MaHD = MaHD;
        this.MaBan = MaBan;
        this.GioVao = GioVao;
        this.GioRa = GioRa;
        this.TongTien = TongTien;
        this.PhuongThuc = PhuongThuc;
        this.maca = maca;
    }

    public String getMaca() {
        return maca;
    }

    public String getMaHD() {
        return MaHD;
    }

    public String getMaBan() {
        return MaBan;
    }

    public String getGioVao() {
        return GioVao;
    }

    public String getGioRa() {
        return GioRa;
    }

    public int getTongTien() {
        return TongTien;
    }

    public String getPhuongThuc() {
        return PhuongThuc;
    }
}
