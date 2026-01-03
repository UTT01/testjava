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
public class MonDaBan implements Serializable{
    String tenmon;
    int soluong;


    public String getTenmon() {
        return tenmon;
    }

    public int getSoluong() {
        return soluong;
    }

    public MonDaBan(String tenmon, int soluong) {
        this.tenmon = tenmon;
        this.soluong = soluong;
    }
}
