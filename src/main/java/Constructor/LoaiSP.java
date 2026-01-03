/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Constructor;


public class LoaiSP {
    private String imgRes;
    private String info;
    private int maLoai;

    public LoaiSP(String imgRes, String info, int maLoai) {
        this.imgRes = imgRes;
        this.info = info;
        this.maLoai = maLoai;
    }

    public String getInfo() { return info; }
    public String getImgRes() { return imgRes; }
    public int getMaLoai() { return maLoai; }
}
