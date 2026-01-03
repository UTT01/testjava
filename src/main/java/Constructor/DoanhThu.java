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
public class DoanhThu implements Serializable{
    String madt, maca, ngay;
    int tienmat, chuyenkhoan, moket;

    public String getNgay() {
        return ngay;
    }

    public DoanhThu(String maca, String ngay, int tienmat, int chuyenkhoan, int moket) {
        this.maca = maca;
        this.ngay = ngay;
        this.tienmat = tienmat;
        this.chuyenkhoan = chuyenkhoan;
        this.moket = moket;
    }

    public int getMoket() {
        return moket;
    }

    public String getMadt() {
        return madt;
    }

    public String getMaca() {
        return maca;
    }

    public int getTienmat() {
        return tienmat;
    }

    public int getChuyenkhoan() {
        return chuyenkhoan;
    }
}
