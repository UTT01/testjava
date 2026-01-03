


package View.Folder_Pages_Card;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;





public class SiderBar_SanPham_UI extends JPanel {

    public JList<String> listMenu;

    public SiderBar_SanPham_UI(String selectedItem) {

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(180, 0));
        setBackground(new Color(245, 245, 245));

        DefaultListModel<String> model = new DefaultListModel<>();
        listMenu = new JList<>(model);

        model.addElement("Trang chủ");
        model.addElement("Sản phẩm");
        model.addElement("Báo cáo");
        model.addElement("Bán hàng");
        model.addElement("Tài khoản");

        listMenu.setFont(new Font("Segoe UI", Font.BOLD, 16));
        listMenu.setFixedCellHeight(45);

        // renderer (copy lại của bạn)
        listMenu.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                JLabel lbl = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                lbl.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

                if (isSelected) {
                    lbl.setBackground(new Color(33, 150, 243));
                    lbl.setForeground(Color.white);
                } else {
                    lbl.setBackground(new Color(245, 245, 245));
                    lbl.setForeground(new Color(60, 60, 60));
                }
                lbl.setOpaque(true);
                return lbl;
            }
        });

        // ⭐ tự động chọn menu
        listMenu.setSelectedValue(selectedItem, true);

        add(listMenu, BorderLayout.CENTER);
    }
}
