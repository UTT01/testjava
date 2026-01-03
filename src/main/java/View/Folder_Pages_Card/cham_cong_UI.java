package View.Folder_Pages_Card;

import Controller.cham_cong_Controller;
import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class cham_cong_UI extends JPanel {
    
    private String maNV;
    private lich_thang_Panel panelLich; // Component lịch tự tạo
    private JLabel lblMonthYear;
    private JLabel lblTimer;
    private JButton btnAction;
    private cham_cong_Controller controller;

    public cham_cong_UI(String maNV) {
        this.maNV = maNV;
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Color.WHITE);
        
        // --- PHẦN 1: TIÊU ĐỀ ---
        this.setBorder(BorderFactory.createTitledBorder(
            null, "Chấm Công & Lịch Làm Việc",
            TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 18), new Color(33, 150, 243)
        ));

        // --- PHẦN 2: LỊCH (CENTER) ---
        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(Color.WHITE);
        
        // Header của lịch (Tháng / Năm)
        JPanel pnlHeaderCal = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlHeaderCal.setBackground(Color.WHITE);
        LocalDate now = LocalDate.now();
        lblMonthYear = new JLabel("Tháng " + now.getMonthValue() + " Năm " + now.getYear());
        lblMonthYear.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblMonthYear.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 0));
        pnlHeaderCal.add(lblMonthYear);
        
        pnlCenter.add(pnlHeaderCal, BorderLayout.NORTH);

        // Body của lịch
        panelLich = new lich_thang_Panel();
        pnlCenter.add(panelLich, BorderLayout.CENTER);

        // --- PHẦN 3: BỘ ĐẾM & NÚT BẤM (SOUTH) ---
        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setBackground(new Color(245, 245, 245));
        pnlBottom.setPreferredSize(new Dimension(0, 150));
        pnlBottom.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.LIGHT_GRAY));

        // Panel chứa đồng hồ
        JPanel pnlTimer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTimer.setOpaque(false);
        lblTimer = new JLabel("00:00:00");
        lblTimer.setFont(new Font("Consolas", Font.BOLD, 48)); 
        lblTimer.setForeground(new Color(33, 33, 33));
        pnlTimer.add(lblTimer);

        // Panel chứa nút bấm
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlButton.setOpaque(false);
        btnAction = new JButton("Chấm công");
        btnAction.setPreferredSize(new Dimension(220, 50));
        btnAction.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnAction.setBackground(new Color(76, 175, 80));
        btnAction.setForeground(Color.WHITE);
        btnAction.setFocusPainted(false);
        btnAction.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlButton.add(btnAction);

        pnlBottom.add(pnlTimer, BorderLayout.CENTER);
        pnlBottom.add(pnlButton, BorderLayout.SOUTH);

        // Add vào layout chính
        this.add(pnlCenter, BorderLayout.CENTER);
        this.add(pnlBottom, BorderLayout.SOUTH);

        // --- KẾT NỐI CONTROLLER ---
        controller = new cham_cong_Controller(maNV, lblTimer, btnAction, panelLich);
        
        btnAction.addActionListener(e -> controller.xuLyNutBam());
        controller.loadDuLieu();
    }
}