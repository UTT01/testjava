package View.Folder_Pages_Card.doanh_thu_UI;

import Constructor.Ca;
import View.Giao_Dien_Chinh_UI;
import View.Giao_Dien_Chinh_UI;
import database.doanh_thu_sql;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class GiaoDienDoanhThu extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public GiaoDienDoanhThu() {
        // 1. Setup Layout
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(16, 16, 16, 16));

        // --- TIÊU ĐỀ ---
        JLabel lblTitle = new JLabel("Ca làm việc - Doanh thu");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(51, 51, 51));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        add(lblTitle, BorderLayout.NORTH);

        // --- BẢNG ---
        String[] columns = {"Mã Ca", "Ngày", "Ca", "Trạng thái"};
        
        // Không cho sửa bảng
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table = new JTable(model);

    // --- THÊM SỰ KIỆN CLICK ---
    table.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String maCa = table.getValueAt(row, 0).toString();

                String trangThai = table.getValueAt(row, 3).toString(); 

                java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(GiaoDienDoanhThu.this);
                if (window instanceof Giao_Dien_Chinh_UI) {
                    ((Giao_Dien_Chinh_UI) window).showChiTietDoanhThu(maCa, trangThai);
                }
            }
        }
    });

        // --- STYLE BẢNG ---
        // Header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(63, 81, 181)); // Xanh đậm
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 40));

        // Rows
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));

        // Căn giữa nội dung
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Độ rộng cột
        table.getColumnModel().getColumn(0).setPreferredWidth(150); // Mã Ca
        table.getColumnModel().getColumn(1).setPreferredWidth(250); // Ngày
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Ca
        table.getColumnModel().getColumn(3).setPreferredWidth(150); // Trạng thái

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(scrollPane, BorderLayout.CENTER);
        
        // --- GỌI HÀM LẤY DỮ LIỆU ---
        loadDoanhThu();
    }

    // Hàm lấy dữ liệu thật từ SQL
    public void loadDoanhThu() {
        // 1. Xóa dữ liệu cũ
        model.setRowCount(0);

        doanh_thu_sql data = new doanh_thu_sql();
        ArrayList<Ca> listCa = data.getAllCa();

        // 3. Đổ dữ liệu vào bảng
        for (Ca caItem : listCa) {
            model.addRow(new Object[]{
                caItem.getMaca(),
                caItem.getNgay(),     // Cột Ngày
                caItem.getCa(),       // Cột Tên Ca (Sáng/Chiều/Tối)
                caItem.getTrangthai() // Cột Trạng thái
            });
        }
    }
}