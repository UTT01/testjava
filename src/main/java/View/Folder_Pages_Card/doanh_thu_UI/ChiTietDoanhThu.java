package View.Folder_Pages_Card.doanh_thu_UI;

import Constructor.DoanhThu;
import Constructor.PhieuThuChi;
import View.Folder_Pages_Card.hoa_don_UI.DialogThemPhieu;
import View.Folder_Pages_Card.hoa_don_UI.DialogThemPhieu;
import database.doanh_thu_sql;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ChiTietDoanhThu extends JPanel {

    private JLabel lblNgay, lblMaCa, lblTongDoanhThu, lblTienMat, lblChuyenKhoan, lblTienTrongKet;
    private JButton btnBack, btnDongCa;
    
    // Khai báo panel chứa danh sách
    private JPanel pListPhieuChi;
    private JPanel pListPhieuThu;
    
    // Khai báo panel vỏ ngoài (cái hộp)
    private JPanel pBlockChi; 
    private JPanel pBlockThu; 
    
    private boolean isChoPhepSua = false;
    private String currentTrangThai = "";

    public ChiTietDoanhThu() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- 1. PHẦN ĐẦU (NORTH) ---
        JPanel pNorth = new JPanel();
        pNorth.setLayout(new BoxLayout(pNorth, BoxLayout.Y_AXIS));
        pNorth.setBackground(Color.WHITE);
        pNorth.setBorder(new EmptyBorder(10, 16, 10, 16));

        JPanel pTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pTop.setBackground(Color.WHITE);
        btnBack = new JButton("<< Quay lại");
        pTop.add(btnBack);
        pNorth.add(pTop);

        JLabel lblTitle = new JLabel("BÁO CÁO DOANH THU CA LÀM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pNorth.add(Box.createVerticalStrut(10));
        pNorth.add(lblTitle);
        pNorth.add(Box.createVerticalStrut(20));

        add(pNorth, BorderLayout.NORTH);

        // --- 2. PHẦN GIỮA (CENTER) ---
        JPanel pCenterContent = new JPanel();
        pCenterContent.setLayout(new BoxLayout(pCenterContent, BoxLayout.Y_AXIS));
        pCenterContent.setBackground(Color.WHITE);
        pCenterContent.setBorder(new EmptyBorder(0, 16, 0, 16));

        // Thông tin chung
        lblNgay = createInfoRow(pCenterContent, "Ngày:");
        lblMaCa = createInfoRow(pCenterContent, "Ca:");
        lblTongDoanhThu = createInfoRow(pCenterContent, "Tổng doanh thu:");
        lblTienMat = createInfoRow(pCenterContent, "Tiền mặt:");
        lblChuyenKhoan = createInfoRow(pCenterContent, "Chuyển khoản:");
        lblTienTrongKet = createInfoRow(pCenterContent, "Tiền trong két:");

        pCenterContent.add(Box.createVerticalStrut(20));

        // --- TẠO KHUNG CHO PHIẾU CHI VÀ THU ---
        pBlockChi = createBlockStructure(pCenterContent, "Phiếu Chi");    
        pCenterContent.add(Box.createVerticalStrut(10));
        
        pBlockThu = createBlockStructure(pCenterContent, "Phiếu Thu");

        // Gắn sự kiện click
        addClickEventToBox(pBlockChi, "Chi");
        addClickEventToBox(pBlockThu, "Thu");
        
        JScrollPane scrollPane = new JScrollPane(pCenterContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // --- 3. PHẦN CUỐI (SOUTH) ---
        JPanel pSouth = new JPanel(new BorderLayout());
        pSouth.setBackground(Color.WHITE);
        pSouth.setBorder(new EmptyBorder(16, 16, 16, 16));

        btnDongCa = new JButton("Đóng ca");
        btnDongCa.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDongCa.setForeground(Color.WHITE);
        btnDongCa.setBackground(new Color(216, 27, 96));
        btnDongCa.setFocusPainted(false);
        btnDongCa.setPreferredSize(new Dimension(0, 50));
        
        pSouth.add(btnDongCa, BorderLayout.CENTER);
        add(pSouth, BorderLayout.SOUTH);
    }

    // --- HÀM TẠO KHUNG XÁM ---
    private JPanel createBlockStructure(JPanel parent, String title) {
        JPanel pBlock = new JPanel();
        pBlock.setLayout(new BorderLayout());
        pBlock.setBackground(new Color(238, 238, 238));
        pBlock.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Mặc định là hand cursor (sẽ được update lại trong setDoanhThuData)
        pBlock.setCursor(new Cursor(Cursor.HAND_CURSOR)); 

        JLabel lblTitle = new JLabel(title); 
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        pBlock.add(lblTitle, BorderLayout.NORTH);

        JPanel pListContainer = new JPanel();
        pListContainer.setLayout(new BoxLayout(pListContainer, BoxLayout.Y_AXIS));
        pListContainer.setBackground(new Color(238, 238, 238));
        pBlock.add(pListContainer, BorderLayout.CENTER);
        
        parent.add(pBlock);
        
        if(title.equals("Phiếu Chi")) this.pListPhieuChi = pListContainer;
        else if(title.equals("Phiếu Thu")) this.pListPhieuThu = pListContainer;

        return pBlock;
    }

    // --- HÀM XỬ LÝ SỰ KIỆN CLICK ---
    private void addClickEventToBox(JPanel panelBox, String loai) {
        panelBox.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // QUAN TRỌNG: Nếu không cho phép sửa (Ca đóng) thì chặn click
                if (!isChoPhepSua) return;

                String maCa = lblMaCa.getText().trim();
                if(maCa.equals("...") || maCa.isEmpty()) return;

                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(ChiTietDoanhThu.this);
                DialogThemPhieu dlg = new DialogThemPhieu(parentFrame, maCa, loai);
                dlg.setVisible(true);

                if (dlg.isSuccess()) {
                    setDoanhThuData(maCa, currentTrangThai); 
                }
            }
        });
    }

    // --- HÀM TẠO DÒNG THÔNG TIN ---
    private JLabel createInfoRow(JPanel parent, String title) {
        JPanel pRow = new JPanel(new BorderLayout());
        pRow.setBackground(Color.WHITE);
        pRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JLabel lblValue = new JLabel("...");
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblValue.setHorizontalAlignment(JLabel.RIGHT);

        pRow.add(lblTitle, BorderLayout.WEST);
        pRow.add(lblValue, BorderLayout.CENTER);

        parent.add(pRow);
        parent.add(Box.createVerticalStrut(8));
        return lblValue;
    }

    // --- HÀM ĐỔ DỮ LIỆU CHÍNH ---
    public void setDoanhThuData(String maCaInput, String trangThai) {
        this.currentTrangThai = trangThai;
        
        // 1. LUÔN LUÔN set Mã Ca ngay từ đầu (Để không bị mất mã nếu data rỗng)
        lblMaCa.setText(maCaInput); 

        // 2. Kiểm tra trạng thái Ca (Có chữ "Mở" thì mới cho sửa)
        this.isChoPhepSua = trangThai.toLowerCase().contains("mở");

        // 3. Load số liệu tiền
        doanh_thu_sql data = new doanh_thu_sql();
        ArrayList<DoanhThu> listDT = data.getDoanhThuByMaCa(maCaInput);
        NumberFormat currency = NumberFormat.getNumberInstance(Locale.US);

        if (!listDT.isEmpty()) {
            DoanhThu dt = listDT.get(0);
            lblNgay.setText(dt.getNgay());
            // lblMaCa.setText(dt.getMaca()); <--- BỎ DÒNG NÀY (Vì đã set ở trên rồi)
            
            int tongDT = dt.getTienmat() + dt.getChuyenkhoan();
            lblTongDoanhThu.setText(currency.format(tongDT) + " VNĐ");
            lblTienMat.setText(currency.format(dt.getTienmat()) + " VNĐ");
            lblChuyenKhoan.setText(currency.format(dt.getChuyenkhoan()) + " VNĐ");
            lblTienTrongKet.setText(currency.format(dt.getMoket()) + " VNĐ");
        } else {
            // [QUAN TRỌNG] Nếu chưa có dữ liệu doanh thu (ca mới), hiển thị số 0
            // KHÔNG ĐƯỢC set lblMaCa thành "Không tìm thấy"
            lblNgay.setText(java.time.LocalDate.now().toString()); // Hoặc lấy ngày từ DB nếu muốn kỹ hơn
            lblTongDoanhThu.setText("0 VNĐ");
            lblTienMat.setText("0 VNĐ");
            lblChuyenKhoan.setText("0 VNĐ");
            
            // Lấy tiền két (MoKet) từ bảng CA riêng nếu bảng DOANHTHU chưa có
            // Nhưng tạm thời để 0 hoặc query thêm cũng được. 
            // Ở đây mình tạm để hiển thị thông báo nhẹ hoặc 0.
            lblTienTrongKet.setText("Updating..."); 
        }

        // 4. Load danh sách phiếu
        loadPhieuToPanel(pListPhieuChi, maCaInput, "Chi"); 
        loadPhieuToPanel(pListPhieuThu, maCaInput, "Thu"); 
        
        // 5. CẬP NHẬT GIAO DIỆN KHÓA/MỞ
        updateBoxState(pBlockChi, "Phiếu Chi");
        updateBoxState(pBlockThu, "Phiếu Thu");
        
        // 6. ẨN/HIỆN NÚT ĐÓNG CA
        if (isChoPhepSua) {
            btnDongCa.setVisible(true); 
        } else {
            btnDongCa.setVisible(false); 
        }
    }
    
    // --- HÀM CẬP NHẬT TRẠNG THÁI CÁI HỘP ---
    private void updateBoxState(JPanel pBlock, String title) {
        JLabel lblTitle = (JLabel) pBlock.getComponent(0); // Lấy cái tiêu đề (Component đầu tiên)
        
        if (isChoPhepSua) {
            // Nếu được sửa: Có dấu (+), có hình bàn tay, màu sáng
            lblTitle.setText(title);
            pBlock.setCursor(new Cursor(Cursor.HAND_CURSOR));
            pBlock.setBackground(new Color(238, 238, 238)); 
        } else {
            // Nếu bị khóa: Không có dấu (+), con trỏ thường, màu tối hơn
            lblTitle.setText(title); 
            pBlock.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            pBlock.setBackground(new Color(220, 220, 220)); 
        }
    }

    // --- HÀM LOAD PHIẾU VÀO UI ---
    private void loadPhieuToPanel(JPanel targetPanel, String maCa, String loaiPhieu) {
        targetPanel.removeAll();
        
        doanh_thu_sql data = new doanh_thu_sql();
        ArrayList<PhieuThuChi> listPhieu = data.getPhieuByMaCa(maCa, loaiPhieu);
        NumberFormat currency = NumberFormat.getNumberInstance(Locale.US);

        if (listPhieu.isEmpty()) {
            JLabel lblEmpty = new JLabel("Không có phiếu " + loaiPhieu.toLowerCase());
            lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            targetPanel.add(lblEmpty);
        } else {
            for (PhieuThuChi p : listPhieu) {
                JPanel pRow = new JPanel(new BorderLayout());
                pRow.setBackground(new Color(238, 238, 238));
                pRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
                pRow.setBorder(new EmptyBorder(2, 0, 2, 0));
                
                JLabel lblTien = new JLabel(currency.format(p.getSotien()));
                lblTien.setFont(new Font("Segoe UI", Font.BOLD, 14));
                if(loaiPhieu.equalsIgnoreCase("Chi")) lblTien.setForeground(new Color(180, 0, 0));
                else lblTien.setForeground(new Color(0, 128, 0));

                JLabel lblGhiChu = new JLabel(" - " + p.getGhichu());
                lblGhiChu.setFont(new Font("Segoe UI", Font.PLAIN, 14));

                JPanel pText = new JPanel(new FlowLayout(FlowLayout.CENTER));
                pText.setBackground(new Color(238, 238, 238));
                pText.add(lblTien);
                pText.add(lblGhiChu);

                targetPanel.add(pText);
            }
        }
        
        targetPanel.revalidate();
        targetPanel.repaint();
    }

    public JButton getBtnBack() {
        return btnBack;
    }

    public JButton getBtnDongCa() {
        return btnDongCa;
    }

    public String getMaCaHienTai() {
        return lblMaCa.getText().trim();
    }
}