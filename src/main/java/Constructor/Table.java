package Constructor;

public class Table {
    private int maBan;
    private String sttBan;
    private String trangThai;
    private int maKV;
    private String khuVuc;
    double tongTien;

    public Table(int maBan, String sttBan, String trangThai, int maKV, String khuVuc, Double TongTien) {
        this.maBan = maBan;
        this.sttBan = sttBan;
        this.trangThai = trangThai;
        this.maKV = maKV;
        this.khuVuc = khuVuc;
        this.tongTien = TongTien;
    }

    public int getMaBan() {
        return maBan;
    }

    public String getSttBan() {
        return sttBan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public String getKhuVuc() {
        return khuVuc;
    }

    public Double getTongTien() {
        return tongTien;
    }

}
