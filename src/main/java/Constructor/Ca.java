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
public class Ca implements Serializable{
    public String maca, ca, ngay, trangthai;
    public int soluongdangky;

    public Ca(String maca, String ca, String ngay, String trangthai) {
        this.maca = maca;
        this.ca = ca;
        this.ngay = ngay;
        this.trangthai = trangthai;
    }

    public String getMaca() {
        return maca;
    }

    public String getCa() {
        return ca;
    }

    public String getNgay() {
        return ngay;
    }

    public String getTrangthai() {
        return trangthai;
    }
}
