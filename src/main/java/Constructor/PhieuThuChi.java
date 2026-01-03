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
public class PhieuThuChi implements Serializable{
    String mathuchi, ghichu, maca, loai;
    int sotien;

    public PhieuThuChi(String mathuchi, String ghichu, String maca, int sotien, String loai) {
        this.mathuchi = mathuchi;
        this.ghichu = ghichu;
        this.maca = maca;
        this.sotien = sotien;
        this.loai = loai;
    }

    public String getLoai() {
        return loai;
    }

    public String getMathuchi() {
        return mathuchi;
    }

    public String getGhichu() {
        return ghichu;
    }

    public String getMaca() {
        return maca;
    }

    public int getSotien() {
        return sotien;
    }
}
