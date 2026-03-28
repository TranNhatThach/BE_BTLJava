package com.btljava.GiaSu.service;

import com.btljava.GiaSu.dto.TuyenDungResponse;
import com.btljava.GiaSu.dto.TuyenDungTrucTiepRequest;
import com.btljava.GiaSu.entity.*;
import com.btljava.GiaSu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TuyenDungService {

        @Autowired
        private YeuCauTimGiaSuRepository yeuCauRepository;

        @Autowired
        private UngTuyenRepository ungTuyenRepository;

        @Autowired
        private LopHocRepository lopHocRepository;

        @Autowired
        private GiaSuRepository giaSuRepository;

        @Autowired
        private HocVienRepository hocVienRepository;

        @Autowired
        private MonHocRepository monHocRepository;

        @Autowired
        private NotificationService notificationService;

        @Transactional
        public TuyenDungResponse<String> guiLoiMoiTrucTiep(TuyenDungTrucTiepRequest request) {
                GiaSu giaSu = giaSuRepository.findById(request.getMaGiaSu())
                                .orElseThrow(() -> new RuntimeException("Gia sư không tồn tại"));
                HocVien hocVien = hocVienRepository.findById(request.getMaHocVien())
                                .orElseThrow(() -> new RuntimeException("Học viên không tồn tại"));

                MonHoc monHoc = monHocRepository.findByTenMon(request.getTenMon());
                if (monHoc == null) {
                        monHoc = new MonHoc();
                        monHoc.setTenMon(request.getTenMon());
                        monHoc = monHocRepository.save(monHoc);
                }

                // 1. Tạo Yêu Cầu Tìm Gia Sư
                YeuCauTimGiaSu yeuCau = YeuCauTimGiaSu.builder()
                                .monHoc(monHoc)
                                .hocVien(hocVien)
                                .trinhDo(request.getTrinhDo())
                                .lichHocDuKien(request.getLichHocDuKien())
                                .hinhThuc(request.getHinhThuc())
                                .diaDiem(request.getDiaDiem())
                                .nganSachMin(request.getNganSachMin())
                                .nganSachMax(request.getNganSachMax())
                                .moTa(request.getMoTa())
                                .trangThai("TRỰC TIẾP")
                                .build();
                yeuCau = yeuCauRepository.save(yeuCau);

                // 2. Tạo Ứng Tuyển
                UngTuyenId ungTuyenId = new UngTuyenId(giaSu.getMaGiaSu(), yeuCau.getMaYeuCau());
                UngTuyen ungTuyen = UngTuyen.builder()
                                .id(ungTuyenId)
                                .giaSu(giaSu)
                                .yeuCauTimGiaSu(yeuCau)
                                .loiNhan(request.getLoiNhan())
                                .mucHocPhiDeXuat((request.getNganSachMin() != null && request.getNganSachMax() != null)
                                                ? (request.getNganSachMin() + request.getNganSachMax()) / 2
                                                : request.getNganSachMin())
                                .trangThai("CHỜ GIA SƯ XÁC NHẬN")
                                .build();

                ungTuyenRepository.save(ungTuyen);

                // 3. Gửi thông báo cho Gia sư
                notificationService.guiThongBao(giaSu.getTaiKhoan(),
                                "Học viên " + hocVien.getTaiKhoan().getHoTen()
                                                + " vừa gửi cho bạn một lời mời dạy trực tiếp môn "
                                                + monHoc.getTenMon(),
                                "TUYEN_DUNG");

                return TuyenDungResponse.<String>builder()
                                .message("Nộp đơn ứng tuyển thành công!")
                                .status("success")
                                .build();
        }

        public List<UngTuyen> layDanhSachLoiMoi(Integer maGiaSu) {
                return ungTuyenRepository.findByGiaSu_MaGiaSuAndTrangThai(maGiaSu, "CHỜ GIA SƯ XÁC NHẬN");
        }

        @Transactional
        public LopHoc giaSuDongY(Integer maGiaSu, Integer maYeuCau) {
                UngTuyenId id = new UngTuyenId(maGiaSu, maYeuCau);
                UngTuyen ungTuyen = ungTuyenRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy lời mời tuyển dụng"));

                if (!"CHỜ GIA SƯ XÁC NHẬN".equals(ungTuyen.getTrangThai())) {
                        throw new RuntimeException("Trạng thái lời mời không hợp lệ: " + ungTuyen.getTrangThai());
                }

                // Cập nhật trạng thái UngTuyen & YeuCau
                ungTuyen.setTrangThai("ĐỒNG Ý");
                ungTuyenRepository.save(ungTuyen);

                YeuCauTimGiaSu yeuCau = ungTuyen.getYeuCauTimGiaSu();
                yeuCau.setTrangThai("ĐÃ GIAO");
                yeuCauRepository.save(yeuCau);

                // Tạo Lớp Học
                LopHoc lopHoc = LopHoc.builder()
                                .hocVien(yeuCau.getHocVien())
                                .giaSu(ungTuyen.getGiaSu())
                                .monHoc(yeuCau.getMonHoc())
                                .yeuCauTimGiaSu(yeuCau)
                                .hocPhiThoaThuan(ungTuyen.getMucHocPhiDeXuat())
                                .ngayBatDau(LocalDate.now())
                                .trangThai("CHỜ MỞ LỚP")
                                .build();

                LopHoc savedLopHoc = lopHocRepository.save(lopHoc);

                // Gửi thông báo cho Học viên
                notificationService.guiThongBao(yeuCau.getHocVien().getTaiKhoan(),
                                "Gia sư " + ungTuyen.getGiaSu().getTaiKhoan().getHoTen() + " đã đồng ý dạy lớp "
                                                + yeuCau.getMonHoc().getTenMon(),
                                "LOP_HOC");

                return savedLopHoc;
        }

        @Transactional
        public void giaSuTuChoi(Integer maGiaSu, Integer maYeuCau) {
                UngTuyenId id = new UngTuyenId(maGiaSu, maYeuCau);
                UngTuyen ungTuyen = ungTuyenRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy lời mời tuyển dụng"));

                ungTuyen.setTrangThai("GIA SƯ TỪ CHỐI");
                ungTuyenRepository.save(ungTuyen);

                YeuCauTimGiaSu yeuCau = ungTuyen.getYeuCauTimGiaSu();
                yeuCau.setTrangThai("MỞ"); // Mở lại yêu cầu để người khác ứng tuyển hoặc học viên chọn lại
                yeuCauRepository.save(yeuCau);

                // Thông báo cho học viên
                notificationService.guiThongBao(yeuCau.getHocVien().getTaiKhoan(),
                                "Gia sư " + ungTuyen.getGiaSu().getTaiKhoan().getHoTen()
                                                + " đã từ chối lời mời dạy môn " + yeuCau.getMonHoc().getTenMon(),
                                "TUYEN_DUNG");
        }

        @Transactional
        public TuyenDungResponse<String> giaSuUngTuyen(com.btljava.GiaSu.dto.GiaSuUngTuyenRequest request) {
                GiaSu giaSu = giaSuRepository.findById(request.getMaGiaSu())
                                .orElseThrow(() -> new RuntimeException("Gia sư không tồn tại"));
                YeuCauTimGiaSu yeuCau = yeuCauRepository.findById(request.getMaYeuCau())
                                .orElseThrow(() -> new RuntimeException("Yêu cầu không tồn tại"));

                UngTuyenId ungTuyenId = new UngTuyenId(giaSu.getMaGiaSu(), yeuCau.getMaYeuCau());
                if (ungTuyenRepository.existsById(ungTuyenId)) {
                        throw new RuntimeException("Gia sư đã ứng tuyển yêu cầu này rồi");
                }

                UngTuyen ungTuyen = UngTuyen.builder()
                                .id(ungTuyenId)
                                .giaSu(giaSu)
                                .yeuCauTimGiaSu(yeuCau)
                                .loiNhan(request.getLoiNhan())
                                .mucHocPhiDeXuat(request.getMucHocPhiDeXuat())
                                .trangThai("CHỜ HỌC VIÊN XÁC NHẬN")
                                .build();
                ungTuyenRepository.save(ungTuyen);

                // Thông báo cho học viên
                notificationService.guiThongBao(yeuCau.getHocVien().getTaiKhoan(),
                                "Gia sư " + giaSu.getTaiKhoan().getHoTen()
                                                + " vừa ứng tuyển vào yêu cầu tìm gia sư môn "
                                                + yeuCau.getMonHoc().getTenMon(),
                                "TUYEN_DUNG");

                return TuyenDungResponse.<String>builder()
                                .message("Nộp đơn ứng tuyển thành công!")
                                .status("success")
                                .build();

        }

        public List<UngTuyen> layDanhSachUngVien(Integer maYeuCau) {
                // Trả về tất cả ứng viên của yêu cầu đó
                return ungTuyenRepository.findByYeuCauTimGiaSu_MaYeuCau(maYeuCau);
        }

        // Học viên xem danh sách tất cả các đơn ứng tuyển của các yêu cầu của mình
        public List<UngTuyen> layDanhSachUngTuyenChoHocVien(Integer maHocVien) {
                return ungTuyenRepository.findByYeuCauTimGiaSu_HocVien_MaHocVien(maHocVien);
        }

        // Gia sư xem danh sách các đơn đã ứng tuyển của mình
        public List<UngTuyen> layDanhSachDaUngTuyen(Integer maGiaSu) {
                return ungTuyenRepository.findByGiaSu_MaGiaSu(maGiaSu);
        }

        @Transactional
        public LopHoc hocVienDuyet(Integer maHocVien, Integer maGiaSu, Integer maYeuCau) {
                UngTuyenId id = new UngTuyenId(maGiaSu, maYeuCau);
                UngTuyen ungTuyen = ungTuyenRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn ứng tuyển"));

                if (!"CHỜ HỌC VIÊN XÁC NHẬN".equals(ungTuyen.getTrangThai())) {
                        throw new RuntimeException("Trạng thái đơn ứng tuyển không hợp lệ: " + ungTuyen.getTrangThai());
                }

                YeuCauTimGiaSu yeuCau = ungTuyen.getYeuCauTimGiaSu();
                if (!yeuCau.getHocVien().getMaHocVien().equals(maHocVien)) {
                        throw new RuntimeException("Học viên không có quyền duyệt yêu cầu này");
                }

                // Cập nhật trạng thái
                ungTuyen.setTrangThai("ĐỒNG Ý");
                ungTuyenRepository.save(ungTuyen);

                yeuCau.setTrangThai("ĐÃ GIAO");
                yeuCauRepository.save(yeuCau);

                // Tạo Lớp Học
                LopHoc lopHoc = LopHoc.builder()
                                .hocVien(yeuCau.getHocVien())
                                .giaSu(ungTuyen.getGiaSu())
                                .monHoc(yeuCau.getMonHoc())
                                .yeuCauTimGiaSu(yeuCau)
                                .hocPhiThoaThuan(ungTuyen.getMucHocPhiDeXuat())
                                .ngayBatDau(LocalDate.now())
                                .trangThai("CHỜ MỞ LỚP")
                                .build();

                LopHoc savedLopHoc = lopHocRepository.save(lopHoc);

                // Thông báo cho gia sư
                notificationService.guiThongBao(ungTuyen.getGiaSu().getTaiKhoan(),
                                "Học viên " + yeuCau.getHocVien().getTaiKhoan().getHoTen()
                                                + " đã phê duyệt đơn ứng tuyển của bạn cho môn "
                                                + yeuCau.getMonHoc().getTenMon(),
                                "LOP_HOC");

                return savedLopHoc;
        }

        @Transactional
        public void hocVienTuChoi(Integer maHocVien, Integer maGiaSu, Integer maYeuCau) {
                UngTuyenId id = new UngTuyenId(maGiaSu, maYeuCau);
                UngTuyen ungTuyen = ungTuyenRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn ứng tuyển"));

                YeuCauTimGiaSu yeuCau = ungTuyen.getYeuCauTimGiaSu();
                if (!yeuCau.getHocVien().getMaHocVien().equals(maHocVien)) {
                        throw new RuntimeException("Học viên không có quyền từ chối yêu cầu này");
                }

                ungTuyen.setTrangThai("HỌC VIÊN TỪ CHỐI");
                ungTuyenRepository.save(ungTuyen);

                // Thông báo cho gia sư
                notificationService.guiThongBao(ungTuyen.getGiaSu().getTaiKhoan(),
                                "Học viên " + yeuCau.getHocVien().getTaiKhoan().getHoTen()
                                                + " đã từ chối đơn ứng tuyển của bạn cho môn "
                                                + yeuCau.getMonHoc().getTenMon(),
                                "TUYEN_DUNG");
        }
}
