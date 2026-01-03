package Controller;

import View.Folder_Pages_Card.thong_tin_tai_khoan_UI;
import Constructor.item_nhanvien;
import database.*;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.awt.event.ItemEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class thong_tin_tai_khoan_Controller {

    private thong_tin_tai_khoan_UI view;
    private String maNv;

    public thong_tin_tai_khoan_Controller(thong_tin_tai_khoan_UI view, String maNv) {
        this.view = view;
        this.maNv = maNv;
    }

    public void loadData() {
        view.getLblTongLuong().setText("Đang tính toán...");

        int thang = view.getCboThang().getSelectedIndex() + 1;
        int nam = (int) view.getSpnNam().getValue();

        new Thread(() -> {
            // 1. Lấy thông tin nhân viên để biết Lương Cứng (Lương/Giờ)
            item_nhanvien nv = nhan_vien_query.getThongTinNhanVien(this.maNv);
            String tenTH = (nv != null) ? nhan_vien_query.getTenThuongHieu(nv.getMaThuongHieu()) : "";
            
            // Lấy lương theo giờ (Giả sử trong DB cột Luong là lương theo giờ)
            double luongTheoGio = (nv != null) ? nv.getLuong() : 0;

            // 2. Lấy lịch sử Chấm công (Thay vì lấy lịch sử đăng ký ca)
            ArrayList<Object[]> listHistory = cham_cong_query.getLichSuChamCongTheoThang(this.maNv, thang, nam);

            SwingUtilities.invokeLater(() -> {
                if (nv != null) {
                    view.getLblTen().setText(nv.getTenNV());
                    if (nv.getNgaySinh() != null) {
                        view.getLblNgaySinh().setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format(nv.getNgaySinh()));
                    }
                    view.getLblChucVu().setText(nv.getChucVu());
                    view.getLblEmail().setText(nv.getEmail());
                    view.getLblCoSo().setText(tenTH);
                }

                view.getModelLichSu().setRowCount(0);
                double tongTienLuong = 0;
                double tongGioLam = 0;
                DecimalFormat df = new DecimalFormat("#,###");

                int stt = 1;
                for (Object[] row : listHistory) {
                    // Cấu trúc row trả về từ query: [0:Ngay, 1:Ca, 2:Vao, 3:Ra, 4:SoGio, 5:TrangThai]
                    float soGioLam = (float) row[4];
                    double thanhTien = soGioLam * luongTheoGio;

                    // Thêm vào bảng UI
                    view.getModelLichSu().addRow(new Object[]{
                        stt++,
                        row[0], // Ngày
                        row[1], // Ca
                        row[2], // Giờ vào
                        row[3], // Giờ ra
                        df.format(soGioLam), // Số giờ
                        df.format(thanhTien) // Thành tiền ca đó
                    });

                    tongGioLam += soGioLam;
                    tongTienLuong += thanhTien;
                }

                view.getLblTongLuong().setText("TỔNG LƯƠNG THÁNG " + thang + "/" + nam + ": "
                        + df.format(tongGioLam) + " giờ x " + df.format(luongTheoGio) + "/h = "
                        + df.format(tongTienLuong) + " VNĐ");
            });

        }).start();
    }

    public void handleFilterChange(Object e) {
        if (e instanceof java.awt.event.ItemEvent) {
             if (((ItemEvent) e).getStateChange() == ItemEvent.SELECTED) {
                 loadData();
             }
        } 
        else {
            loadData();
        }
    }

    // Xóa bỏ phần xuLyTableChange (Lưu ghi chú) vì bảng Chấm công thường không cho nhân viên tự sửa ghi chú trực tiếp ở đây
    
    // --- XỬ LÝ ĐỔI MẬT KHẨU (Giữ nguyên) ---
    public void xuLyDoiMatKhau() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JPasswordField pfOld = new JPasswordField();
        JPasswordField pfNew = new JPasswordField();
        JPasswordField pfConfirm = new JPasswordField();

        panel.add(new JLabel("Mật khẩu cũ:"));
        panel.add(pfOld);
        panel.add(new JLabel("Mật khẩu mới:"));
        panel.add(pfNew);
        panel.add(new JLabel("Xác nhận mật khẩu mới:"));
        panel.add(pfConfirm);

        int result = JOptionPane.showConfirmDialog(view, panel, "Đổi mật khẩu",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String passCu = new String(pfOld.getPassword());
            String passMoi = new String(pfNew.getPassword());
            String xacNhan = new String(pfConfirm.getPassword());

            if (!passMoi.equals(xacNhan)) {
                JOptionPane.showMessageDialog(view, "Mật khẩu xác nhận không khớp!");
                return;
            }

            String email = view.getLblEmail().getText();
            
            String check = tai_khoan_query.checkLogin(email, passCu);

            if (check == null) {
                JOptionPane.showMessageDialog(view, "Mật khẩu cũ không đúng!");
            } else {
                boolean thanhCong = tai_khoan_query.updatePassword(email, passMoi);
                if (thanhCong) {
                    JOptionPane.showMessageDialog(view, "Đổi mật khẩu thành công!");
                } else {
                    JOptionPane.showMessageDialog(view, "Lỗi hệ thống.");
                }
            }
        }
    }
}