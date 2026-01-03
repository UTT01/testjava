/*
 * File: DialogThemPhieu.java
 */
package View.Folder_Pages_Card.hoa_don_UI;

import database.doanh_thu_sql;
import java.awt.*;
import java.awt.event.ItemEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DialogThemPhieu extends JDialog {

    private JTextField txtSoTien;
    private JComboBox<String> cboLyDo;
    private JTextField txtLyDoKhac;
    private JLabel lblLyDoKhac;
    private boolean isSuccess = false;

    public DialogThemPhieu(JFrame parent, String maCa, String loaiPhieu) {
        super(parent, "Thêm " + loaiPhieu, true);
        
        setLayout(new BorderLayout());

        // --- PHẦN FORM ---
        JPanel pForm = new JPanel(new GridBagLayout());
        pForm.setBorder(new EmptyBorder(20, 20, 20, 20));
        pForm.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0; 
        gbc.weightx = 1;

        // 1. Nhập Số tiền
        pForm.add(new JLabel("Số tiền (VNĐ):"), changeGbc(gbc, 0));
        txtSoTien = new JTextField(15);
        txtSoTien.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pForm.add(txtSoTien, changeGbc(gbc, 1));

        // 2. Chọn Lý do
        pForm.add(new JLabel("Lý do:"), changeGbc(gbc, 2));
        cboLyDo = new JComboBox<>();
        loadLyDo(loaiPhieu); 
        pForm.add(cboLyDo, changeGbc(gbc, 3));

        // 3. Lý do khác (Ẩn)
        lblLyDoKhac = new JLabel("Nhập lý do khác:");
        lblLyDoKhac.setVisible(false);
        pForm.add(lblLyDoKhac, changeGbc(gbc, 4));
        
        txtLyDoKhac = new JTextField(15);
        txtLyDoKhac.setVisible(false);
        pForm.add(txtLyDoKhac, changeGbc(gbc, 5));

        add(pForm, BorderLayout.CENTER);

        // --- XỬ LÝ SỰ KIỆN COMBOBOX ---
        cboLyDo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selected = cboLyDo.getSelectedItem().toString();
                boolean isOther = selected.equals("Khác");
                
                // Ẩn hiện ô nhập
                lblLyDoKhac.setVisible(isOther);
                txtLyDoKhac.setVisible(isOther);
                
                if (isOther) {
                    txtLyDoKhac.setText("");
                    txtLyDoKhac.requestFocus();
                }
                
                pack(); // 1. Co giãn kích thước trước
                setLocationRelativeTo(parent); // 2. Sau đó mới căn giữa lại theo kích thước mới
            }
        });

        // --- PHẦN NÚT BẤM ---
        JPanel pButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pButton.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JButton btnHuy = new JButton("Hủy");
        btnHuy.addActionListener(e -> dispose());

        JButton btnLuu = new JButton("Lưu Phiếu");
        btnLuu.setBackground(new Color(0, 150, 136));
        btnLuu.setForeground(Color.WHITE);
        
        btnLuu.addActionListener(e -> {
            try {
                if(txtSoTien.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền!");
                    return;
                }
                int soTien = Integer.parseInt(txtSoTien.getText().trim());

                String ghiChu = cboLyDo.getSelectedItem().toString();
                if(ghiChu.equals("Khác")) {
                    ghiChu = txtLyDoKhac.getText().trim();
                    if(ghiChu.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Vui lòng nhập lý do cụ thể!");
                        return;
                    }
                }

                doanh_thu_sql data = new doanh_thu_sql();
                if(data.updatePhieuThuChi(maCa, soTien, loaiPhieu, ghiChu)) {
                    isSuccess = true;
                    dispose(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi lưu vào CSDL!");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số tiền phải là số nguyên!");
            }
        });

        pButton.add(btnHuy);
        pButton.add(btnLuu);
        add(pButton, BorderLayout.SOUTH);
        
        // --- CẤU HÌNH BAN ĐẦU ---
        pack(); // Co giãn form cho gọn
        setLocationRelativeTo(parent); // Căn giữa màn hình cha ngay khi mở lên
    }

    private void loadLyDo(String loai) {
        cboLyDo.removeAllItems();
        if (loai.contains("Thu")) {
            cboLyDo.addItem("Tiền khách bo");
            cboLyDo.addItem("Thu nợ khách hàng");
            cboLyDo.addItem("Hoàn tiền mua hàng");
            cboLyDo.addItem("Khác");
        } else { 
            cboLyDo.addItem("Mua nguyên liệu");
            cboLyDo.addItem("Sửa chữa thiết bị");
            cboLyDo.addItem("Ứng lương nhân viên");
            cboLyDo.addItem("Thanh toán điện/nước");
            cboLyDo.addItem("Mua đá/nước lọc");
            cboLyDo.addItem("Khác");
        }
    }
    
    private GridBagConstraints changeGbc(GridBagConstraints gbc, int y) {
        gbc.gridy = y;
        return gbc;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}