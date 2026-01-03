/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.Folder_Pages_Card;

import java.awt.*;
import java.time.YearMonth;
import java.util.Set;
import javax.swing.*;

/**
 *
 * @author khoin
 */
public class lich_thang_Panel extends JPanel{
    private int month;
    private int year;
    private Set<Integer> activeDays; // Các ngày có chấm công

    public lich_thang_Panel() {
        this.setLayout(new GridLayout(0, 7, 5, 5)); // 7 cột cho 7 ngày
        this.setBackground(Color.WHITE);
    }

    public void updateCalendar(int month, int year, Set<Integer> activeDays) {
        this.month = month;
        this.year = year;
        this.activeDays = activeDays;
        this.removeAll();

        // 1. Vẽ Header (Mo, Tu, We...)
        String[] daysOfWeek = {"Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"};
        for (String d : daysOfWeek) {
            JLabel lbl = new JLabel(d, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lbl.setForeground(Color.GRAY);
            this.add(lbl);
        }

        // 2. Tính toán ngày
        YearMonth yearMonthObject = YearMonth.of(year, month);
        int daysInMonth = yearMonthObject.lengthOfMonth();
        int dayOfWeek = yearMonthObject.atDay(1).getDayOfWeek().getValue(); // 1=Mon, 7=Sun

        // 3. Vẽ khoảng trắng trước ngày mùng 1
        for (int i = 1; i < dayOfWeek; i++) {
            this.add(new JLabel(""));
        }

        // 4. Vẽ các ngày
        for (int day = 1; day <= daysInMonth; day++) {
            boolean isActive = activeDays != null && activeDays.contains(day);
            this.add(new DayCell(day, isActive));
        }

        this.revalidate();
        this.repaint();
    }

    // Class con để vẽ ô ngày
    private class DayCell extends JPanel {
        public DayCell(int day, boolean isActive) {
            this.setLayout(new BorderLayout());
            this.setBackground(Color.WHITE);
            
            JLabel lblDay = new JLabel(String.valueOf(day), SwingConstants.CENTER);
            lblDay.setFont(new Font("Segoe UI", isActive ? Font.BOLD : Font.PLAIN, 16));
            
            if (isActive) {
                lblDay.setForeground(Color.WHITE); // Chữ trắng
                // Vẽ hình tròn xanh
                JPanel circle = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(76, 175, 80)); // Màu xanh dương đậm
                        g2.fillOval(getWidth()/2 - 15, getHeight()/2 - 15, 30, 30);
                    }
                };
                circle.setOpaque(false);
                circle.setLayout(new BorderLayout());
                circle.add(lblDay, BorderLayout.CENTER);
                this.add(circle, BorderLayout.CENTER);
            } else {
                lblDay.setForeground(Color.BLACK);
                this.add(lblDay, BorderLayout.CENTER);
            }
        }
    }
}
