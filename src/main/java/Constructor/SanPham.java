package Constructor;

import java.io.Serializable;

public class SanPham implements Serializable {
    private String ten;
    private int gia;
    private String hinh;
    private int MaSP;
    private int MaLoai;
    private int soLuong = 1;
    private String ghiChu = "";
    private boolean trangThai; // <== thêm

    public SanPham(String ten, int gia, String hinh, int MaLoai, int MaSP, boolean trangThai) {
        this.ten = ten;
        this.gia = gia;
        this.hinh = hinh;
        this.MaLoai = MaLoai;
        this.MaSP = MaSP;
        this.trangThai = trangThai;
    }

    public String getTen() { return ten; }
    public int getGia() { return gia; }
    public String getHinh() { return hinh; }
    public int getMaSP() { return MaSP; }
    public int getMaLoai() { return MaLoai; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public void tangSoLuong() { this.soLuong++; }

    public void setGhiChu(String GhiChu){ this.ghiChu = GhiChu; }
    public String getGhiChu() { return ghiChu; }

    public boolean isTrangThai() { return trangThai; }

    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}
