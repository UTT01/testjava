package View.Folder_Pages_Card;

import Controller.quan_Ly_nhan_su_Controller;
import Constructor.item_nhanvien;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class quan_ly_nhan_su_UI extends JPanel {

    private String MaNvHienTai; // Người đang đăng nhập
    private JTable danhsachnv;
    private DefaultTableModel dataNv;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private item_nhanvien nvDangMoForm ;
    private JTextField txtMaNv, txtTenNv, txtLuong, txtEmail, txtTimKiem;
    private JDateChooser dateNgaySinh;
    private JComboBox<String> cboChucVu;
    private JButton btnAdd, btnUpdate, btnDelete, btnReset;
    private quan_Ly_nhan_su_Controller controller;
    public quan_ly_nhan_su_UI(String MaNv) {
        this.MaNvHienTai = MaNv;
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(new Color(245, 245, 245));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblTieuDe = new JLabel("QUẢN LÝ NHÂN SỰ");
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTieuDe.setForeground(new Color(33, 150, 243));
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lblTieuDe, BorderLayout.NORTH);
        nvDangMoForm = database.nhan_vien_query.getThongTinNhanVien(MaNvHienTai);
        controller = new quan_Ly_nhan_su_Controller(nvDangMoForm);
        createUI();
        loadDataLenBang(); 
        if (this.nvDangMoForm == null) {
        System.err.println("Lỗi: Không tìm thấy thông tin nhân viên đang đăng nhập!");
        } else {
            System.out.println("User đang thao tác: " + nvDangMoForm.getTenNV());
        }
    }
    
    public void createUI() {
        // --- 1. PANEL NHẬP LIỆU ---
        JPanel pnlInputContainer = new JPanel(new BorderLayout(0, 10));
        pnlInputContainer.setBackground(new Color(245, 245, 245));
        pnlInputContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Thông tin nhân viên",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14)
        ));

        JPanel Input_Data = new JPanel(new GridLayout(3, 4, 15, 15));
        Input_Data.setBackground(new Color(245, 245, 245));
        Input_Data.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        txtMaNv = new JTextField();txtMaNv.setEditable(false);
        txtTenNv = new JTextField();
        dateNgaySinh = new JDateChooser();
        dateNgaySinh.setDateFormatString("yyyy-MM-dd"); 
        txtEmail = new JTextField();
        txtLuong = new JTextField();

        String[] danhSachChucVu = {"Quản lý", "Nhân Viên Pha Chế", "Nhân Viên Bàn", "Nhân Viên Quầy"};
        cboChucVu = new JComboBox<>(danhSachChucVu);

        Input_Data.add(new JLabel("Mã Nhân Viên:")); Input_Data.add(txtMaNv);
        Input_Data.add(new JLabel("Tên Nhân Viên:")); Input_Data.add(txtTenNv);
        Input_Data.add(new JLabel("Ngày Sinh:"));     Input_Data.add(dateNgaySinh);
        Input_Data.add(new JLabel("Chức Vụ:"));       Input_Data.add(cboChucVu);
        Input_Data.add(new JLabel("Lương:"));         Input_Data.add(txtLuong);
        Input_Data.add(new JLabel("Email:"));         Input_Data.add(txtEmail);

   
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setBackground(new Color(245, 245, 245));

        btnAdd = createButton("Thêm", new Color(76, 175, 80));
        btnUpdate = createButton("Sửa", new Color(255, 193, 7));
        btnDelete = createButton("Xóa", new Color(244, 67, 54));
        btnReset = createButton("Làm mới", new Color(33, 150, 243));
        
        txtTimKiem = new JTextField(15);
        
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnReset);
        btnPanel.add(Box.createHorizontalStrut(20));
        btnPanel.add(new JLabel("Tìm tên: "));
        btnPanel.add(txtTimKiem);

        pnlInputContainer.add(Input_Data, BorderLayout.CENTER);
        pnlInputContainer.add(btnPanel, BorderLayout.SOUTH);

        // --- 3. BẢNG DỮ LIỆU ---
        String[] columnsName = {"Mã Nv","Tên NV", "Ngày Sinh", "Chức Vụ", "Lương", "Email"};
        dataNv = new DefaultTableModel(columnsName, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        danhsachnv = new JTable(dataNv);
        danhsachnv.setRowHeight(25);
        rowSorter = new TableRowSorter<>(dataNv);
        danhsachnv.setRowSorter(rowSorter);
        JScrollPane dsNvScroll = new JScrollPane(danhsachnv);
        dsNvScroll.setBorder(BorderFactory.createTitledBorder("Danh sách nhân viên"));

        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.add(pnlInputContainer, BorderLayout.NORTH);
        pnlCenter.add(dsNvScroll, BorderLayout.CENTER);
        this.add(pnlCenter, BorderLayout.CENTER);

        addEvents();
    }
    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        return btn;
    }
    private void loadDataLenBang() {
        controller.loadData(dataNv);
    }
        
    private void addEvents() {
        txtTimKiem.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e) {
                String text = txtTimKiem.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
                        @Override
                        public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                            String tenNV = entry.getStringValue(1);
                            if (tenNV == null) return false;
                            return tenNV.toLowerCase().contains(text.toLowerCase());
                        }
                    });
                }
            }
        });
        danhsachnv.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && danhsachnv.getSelectedRow() != -1) {
                int row = danhsachnv.getSelectedRow();
                txtMaNv.setText(danhsachnv.getValueAt(row, 0).toString());
                txtTenNv.setText(danhsachnv.getValueAt(row, 1).toString());
                try {
                    Object dateObj = danhsachnv.getValueAt(row, 2);
                    if(dateObj != null) {
                         Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateObj.toString());
                         dateNgaySinh.setDate(date);
                    }
                } catch (Exception ex) { ex.printStackTrace(); }
                String hienThiChucVu = danhsachnv.getValueAt(row, 3).toString();
                cboChucVu.setSelectedItem(hienThiChucVu);
                txtLuong.setText(danhsachnv.getValueAt(row, 4).toString());
                Object emailVal = danhsachnv.getValueAt(row, 5);
                txtEmail.setText(emailVal != null ? emailVal.toString() : "");
            }
        });
        btnAdd.addActionListener(e -> {
            if (controller.validateInput(txtTenNv, txtLuong, dateNgaySinh)) {
                if (controller.addNhanVien(
                        txtTenNv,
                        txtLuong,
                        txtEmail,
                        dateNgaySinh,
                        cboChucVu.getSelectedItem().toString()
                )) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công!");
                    loadDataLenBang();
                    resetForm();
                }
            }
        });

        // 3. Nút Sửa
        btnUpdate.addActionListener(e -> {
             if (danhsachnv.getSelectedRow() == -1) return;
            if (controller.validateInput(txtTenNv, txtLuong, dateNgaySinh)) {
                if (controller.updateNhanVien(
                        txtMaNv,
                        txtTenNv,
                        txtLuong,
                        txtEmail,
                        dateNgaySinh,
                        cboChucVu.getSelectedItem().toString()
                )) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    loadDataLenBang();
                }
            }
        });

        // 4. Nút Xóa
        btnDelete.addActionListener(e -> {
            if (txtMaNv.getText().isEmpty()) return;
            int check = JOptionPane.showConfirmDialog(this, 
                    "Bạn Chắc Chắn Xóa Chứ ?",
                    "Xác Nhận",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if(check == JOptionPane.OK_OPTION){
                if (controller.deleteNhanVien(txtMaNv.getText())) {
                    JOptionPane.showMessageDialog(this, "Đã xóa!");
                    loadDataLenBang();
                    resetForm();
                }
            }
        });

            // 5. Nút Làm mới
        btnReset.addActionListener(e -> {
            txtTimKiem.setText("");
            resetForm();
        });
    }
    private void resetForm() {
        txtMaNv.setText("");
        txtTenNv.setText("");
        txtLuong.setText("");
        txtEmail.setText("");
        dateNgaySinh.setDate(null);
        cboChucVu.setSelectedIndex(0);
        danhsachnv.clearSelection();
    }
}