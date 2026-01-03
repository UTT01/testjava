/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Constructor;



public class Discount {

    private int maKM;
    private String tenKM;
    private int tyLeGiam;

    public Discount(int maKM, String tenKM, int tyLeGiam) {
        this.maKM = maKM;
        this.tenKM = tenKM;
        this.tyLeGiam = tyLeGiam;
    }

    public int getMaKM() {
        return maKM;
    }

    public String getTenKM() {
        return tenKM;
    }

    public int getTyLeGiam() {
        return tyLeGiam;
    }

    // ⭐ JList sẽ hiển thị cái này
    @Override
    public String toString() {
        return "Giảm " + tyLeGiam + "% - " + tenKM;
    }
}

