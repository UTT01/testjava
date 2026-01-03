package View.BanHang;

import Constructor.Table;
import Constructor.KhuVuc;
import View.Giao_Dien_Chinh_UI;
import database.HoaDon_Query;

import java.awt.*;
import java.util.List;
import javax.swing.*;

public class GiaoDienKhuVuc extends JPanel {

    private Giao_Dien_Chinh_UI mainUI;
    private HoaDon_Query sqlHoaDon = new HoaDon_Query();

    private JComboBox<KhuVuc> cbKhuVuc;
    private DefaultComboBoxModel<KhuVuc> modelKhuVuc;

    private String MaNv, MaCa;
    private JPanel panelBan;

    public GiaoDienKhuVuc(String maNv, String MaCa, Giao_Dien_Chinh_UI mainUI) {
        this.MaNv = maNv;
        this.MaCa = MaCa;
        this.mainUI = mainUI;

        setLayout(new BorderLayout());

        // ✅ KIỂM TRA CA TRƯỚC
        if (this.MaCa == null) {
            JOptionPane.showMessageDialog(
                this,
                "Không xác định được ca làm việc.\nVui lòng mở ca trước!",
                "Lỗi ca làm",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        taoGiaoDien();
    }

    private void taoGiaoDien() {

        /* ===== HEADER ===== */
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel title = new JLabel("QUẢN LÝ BÁN HÀNG");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        header.add(title);
        add(header, BorderLayout.NORTH);

        /* ===== CONTENT ===== */
        JPanel contentPanel = new JPanel(new BorderLayout());

        /* ===== COMBOBOX KHU VỰC ===== */
        modelKhuVuc = new DefaultComboBoxModel<>();
        cbKhuVuc = new JComboBox<>(modelKhuVuc);

        modelKhuVuc.addElement(new KhuVuc(-1, "Chọn khu vực"));

        try {
            for (KhuVuc kv : sqlHoaDon.getAllKhuVuc()) {
                modelKhuVuc.addElement(kv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel khuVucPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        khuVucPanel.add(new JLabel("Khu vực: "));
        khuVucPanel.add(cbKhuVuc);

        contentPanel.add(khuVucPanel, BorderLayout.NORTH);

        /* ===== PANEL BÀN ===== */
        panelBan = new JPanel(new GridBagLayout());
        panelBan.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapperPanel.add(panelBan);

        contentPanel.add(wrapperPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        /* ===== ACTION COMBOBOX ===== */
        cbKhuVuc.addActionListener(e -> {
            KhuVuc kv = (KhuVuc) cbKhuVuc.getSelectedItem();
            if (kv == null || kv.getMaKV() == -1) return;
            System.out.println(kv.getMaKV());
            loadBanTheoKhuVuc(kv.getMaKV());
        });

        /* ===== AUTO CHỌN KHU VỰC HÓA ĐƠN (MaKV = 5) ===== */
        SwingUtilities.invokeLater(() -> autoSelectKhuVuc(5));

        /* ===== FOOTER ===== */
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.add(new JLabel("Hệ thống bán hàng - Version 1.0"));
        add(footer, BorderLayout.SOUTH);
    }

    private void autoSelectKhuVuc(int maKV) {
        for (int i = 0; i < modelKhuVuc.getSize(); i++) {
            KhuVuc kv = modelKhuVuc.getElementAt(i);
            if (kv.getMaKV() == maKV) {
                cbKhuVuc.setSelectedIndex(i);
                break;
            }
        }
    }

    private void loadBanTheoKhuVuc(int maKV) {

        panelBan.removeAll();

        List<Table> dsBan =
            (maKV == 5)
                ? sqlHoaDon.getTableSelected()
                : sqlHoaDon.getTableByMaKV(maKV);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        int col = 0, row = 0, numCols = 4;

        for (Table ban : dsBan) {

            JPanel cardBan = new JPanel(new BorderLayout(5, 5));
            cardBan.setPreferredSize(new Dimension(120, 110));
            cardBan.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            JButton btnBan = new JButton(ban.getSttBan());
            btnBan.setFont(new Font("Arial", Font.BOLD, 16));
            btnBan.setFocusPainted(false);
            btnBan.setOpaque(true);
            btnBan.setBorderPainted(false);

            int maBan = ban.getMaBan();
            btnBan.addActionListener(ev ->
                moFormChiTietBan(maBan, ban.getSttBan())
            );

            JLabel lblTien = new JLabel("", SwingConstants.CENTER);
            lblTien.setFont(new Font("Segoe UI", Font.BOLD, 13));

            if ("full".equalsIgnoreCase(ban.getTrangThai())) {
                btnBan.setBackground(new Color(220, 53, 69));
                btnBan.setForeground(Color.WHITE);

                lblTien.setText(String.format("%,.0f đ", ban.getTongTien()));
                lblTien.setForeground(new Color(220, 53, 69));
            }

            cardBan.add(btnBan, BorderLayout.CENTER);
            cardBan.add(lblTien, BorderLayout.SOUTH);

            gbc.gridx = col;
            gbc.gridy = row;
            panelBan.add(cardBan, gbc);

            col++;
            if (col == numCols) {
                col = 0;
                row++;
            }
        }

        panelBan.revalidate();
        panelBan.repaint();
    }

    private void moFormChiTietBan(int maBan, String tenBan) {

        Giao_Dien_Chinh_UI mainUI =
            (Giao_Dien_Chinh_UI) SwingUtilities.getWindowAncestor(this);

        if (mainUI == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy giao diện chính!");
            return;
        }

        GiaoDienOder frmOrder = new GiaoDienOder(
            maBan,
            this.MaNv,
            tenBan,
            MaCa,
            mainUI
        );

        frmOrder.setVisible(true);
    }
}
