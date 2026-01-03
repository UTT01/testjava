package Controller;

import database.cham_cong_query;
import View.Folder_Pages_Card.lich_thang_Panel; // Import panel lịch mới
import View.Folder_Pages_Card.lich_thang_Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Set;
import javax.swing.*;

public class cham_cong_Controller {
    
    private String maNV;
    private JLabel lblTimer;
    private JButton btnAction;
    private lich_thang_Panel panelLich; // Thay JTable bằng PanelLich
    private Timer timer;
    private Timestamp thoiGianBatDau;
    private int currentMaCC = -1;
    private boolean isWorking = false;

    public cham_cong_Controller(String maNV, JLabel lblTimer, JButton btnAction, lich_thang_Panel panelLich) {
        this.maNV = maNV;
        this.lblTimer = lblTimer;
        this.btnAction = btnAction;
        this.panelLich = panelLich;
        
        timer = new Timer(1000, e -> capNhatBoDem());
    }

    public void loadDuLieu() {
        loadLichThangNay();
        checkTrangThaiHienTai();
    }

    private void loadLichThangNay() {
        LocalDate now = LocalDate.now();
        // Lấy danh sách ngày đã làm việc trong tháng
        Set<Integer> activeDays = cham_cong_query.getNgayCoDuLieuChamCong(maNV, now.getMonthValue(), now.getYear());
        // Vẽ lại lịch
        panelLich.updateCalendar(now.getMonthValue(), now.getYear(), activeDays);
    }

    private void checkTrangThaiHienTai() {
        String maCaHienTai = cham_cong_query.getMaCaHienTai();
        
        if (maCaHienTai == null) {
            btnAction.setText("Ngoài giờ làm");
            btnAction.setEnabled(false);
            btnAction.setBackground(java.awt.Color.GRAY);
            lblTimer.setText("Hiện tại không có ca");
            timer.stop();
            return;
        }

        // Kiểm tra xem có đang treo ca nào chưa kết thúc không
        currentMaCC = cham_cong_query.layMaCCDangLam(maNV, maCaHienTai);
        
        if (currentMaCC != -1) {
            // ĐANG LÀM VIỆC (Có ca chưa đóng)
            isWorking = true;
            thoiGianBatDau = cham_cong_query.layThoiGianBatDau(currentMaCC);
            
            btnAction.setText("Tan làm / Tạm nghỉ");
            btnAction.setEnabled(true);
            btnAction.setBackground(new java.awt.Color(244, 67, 54)); // Đỏ
            timer.start();
        } else {
            // KHÔNG LÀM VIỆC (Hoặc chưa chấm, hoặc đã đóng ca trước đó rồi)
            // Cho phép mở lại bất cứ lúc nào nếu còn trong giờ ca
            isWorking = false;
            thoiGianBatDau = null;
            
            btnAction.setText("Chấm công");
            btnAction.setEnabled(true);
            btnAction.setBackground(new java.awt.Color(76, 175, 80)); // Xanh
            lblTimer.setText("00:00:00");
            timer.stop();
        }
    }

    private void capNhatBoDem() {
        if (isWorking && thoiGianBatDau != null) {
            long now = System.currentTimeMillis();
            long start = thoiGianBatDau.getTime();
            long durationMillis = now - start;

            long seconds = (durationMillis / 1000) % 60;
            long minutes = (durationMillis / (1000 * 60)) % 60;
            long hours = (durationMillis / (1000 * 60 * 60));

            lblTimer.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        }
    }

    public void xuLyNutBam() {
        String maCaHienTai = cham_cong_query.getMaCaHienTai();
        if (maCaHienTai == null) return;

        if (!isWorking) {
            // --- LOGIC: BẮT ĐẦU LÀM ---
            if (cham_cong_query.checkIn(maNV, maCaHienTai)) {
                checkTrangThaiHienTai(); 
                loadLichThangNay(); // Cập nhật dấu xanh trên lịch
                JOptionPane.showMessageDialog(null, "Bắt đầu tính giờ làm!");
            }
        } else {
            // --- LOGIC: KẾT THÚC / TẠM NGHỈ ---
            int confirm = JOptionPane.showConfirmDialog(null, 
                    "Xác nhận dừng ca làm việc hiện tại?\n(Bạn có thể mở lại sau nếu vẫn còn trong ca)", 
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (cham_cong_query.checkOut(currentMaCC)) {
                    checkTrangThaiHienTai();
                    // Load lại lịch (để chắc chắn ngày hôm nay vẫn xanh)
                    loadLichThangNay(); 
                }
            }
        }
    }
}