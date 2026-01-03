/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.Folder_Pages_Card;

import java.awt.*;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author khoin
 */
public class thong_tin_nguoi_dung_UI extends JPanel{
    public thong_tin_nguoi_dung_UI(String TenNv,String ChucVu,String MaNv){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(new Color(245, 245, 245));
        this.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(20, 10, 20, 10)
        ));

        JLabel lblAvatar = new JLabel();
        lblAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        try {
            java.net.URL imgURL = getClass().getResource("/images/user.png");
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(
                    new ImageIcon(imgURL).getImage()
                        .getScaledInstance(80, 80, Image.SCALE_SMOOTH)
                );
                lblAvatar.setIcon(icon);
            } else {
                lblAvatar.setText("No IMG");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel lblName = new JLabel(
            "<html><div style='text-align: center; width: 130px;'>" + TenNv + "</div></html>"
        );
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblName.setForeground(new Color(33, 150, 243));
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblRole = new JLabel(ChucVu + " - " + MaNv);
        lblRole.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblRole.setForeground(Color.GRAY);
        lblRole.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(lblAvatar);
        add(Box.createVerticalStrut(10));
        add(lblName);
        add(Box.createVerticalStrut(5));
        add(lblRole);
    }
}
