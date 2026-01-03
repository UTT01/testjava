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
public class Mon implements Serializable{
    private String MaMon;
    private String TenMon;
    private int DonGia;
    private String AnhSP;
    private String LoaiSP;


    public Mon(String MaMon, String TenMon, int DonGia, String AnhSP, String LoaiSP) {
        this.MaMon = MaMon;
        this.TenMon = TenMon;
        this.DonGia = DonGia;
        this.AnhSP = AnhSP;
        this.LoaiSP = LoaiSP;

    }

    public String getTenMon() { return TenMon; }
    public int getDonGia() { return DonGia; }
    public String getMaMon() { return MaMon; }
    public String getLoaiSP() { return LoaiSP; }
}
