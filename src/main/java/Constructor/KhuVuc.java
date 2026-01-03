/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Constructor;


public class KhuVuc {

    private int maKV;
    private String khuVuc;


    public KhuVuc(int maKV, String khuVuc) {

        this.maKV = maKV;
        this.khuVuc = khuVuc;

    }
    public int getMaKV() {
        return maKV;
    }

    public String getKhuVuc() {
        return khuVuc;
    }
    
    @Override
    public String toString() {
        return khuVuc; // ⭐ ComboBox hiển thị tên
    }

}

