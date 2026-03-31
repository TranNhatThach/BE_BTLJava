package com.btljava.GiaSu.service;

import com.btljava.GiaSu.dto.BuoiHocDTO;
import com.btljava.GiaSu.dto.LopHocDTO;
import com.btljava.GiaSu.dto.LopHocRequest;
import com.btljava.GiaSu.entity.BuoiHoc;
import com.btljava.GiaSu.entity.GiaSu;
import com.btljava.GiaSu.entity.HocVien;
import com.btljava.GiaSu.entity.LopHoc;
import com.btljava.GiaSu.entity.MonHoc;
import com.btljava.GiaSu.entity.YeuCauTimGiaSu;
import com.btljava.GiaSu.repository.BuoiHocRepository;
import com.btljava.GiaSu.repository.GiaSuRepository;
import com.btljava.GiaSu.repository.HocVienRepository;
import com.btljava.GiaSu.repository.LopHocRepository;
import com.btljava.GiaSu.repository.MonHocRepository;
import com.btljava.GiaSu.repository.YeuCauTimGiaSuRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LopHocService {

    private final LopHocRepository lopHocRepository;
    private final BuoiHocRepository buoiHocRepository;
    private final HocVienRepository hocVienRepository;
    private final GiaSuRepository giaSuRepository;
    private final MonHocRepository monHocRepository;
    private final YeuCauTimGiaSuRepository yeuCauTimGiaSuRepository;

    public List<LopHocDTO> getLopHocByHocVien(Integer maHocVien) {
        return lopHocRepository.findByHocVien_MaHocVien(maHocVien).stream()
                .map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<LopHocDTO> getLopHocByGiaSu(Integer maGiaSu) {
        return lopHocRepository.findByGiaSu_MaGiaSu(maGiaSu).stream()
                .map(this::mapToDTO).collect(Collectors.toList());
    }

    public LopHocDTO getById(Integer id) {
        LopHoc lopHoc = lopHocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học với ID: " + id));
        return mapToDTO(lopHoc);
    }

    public LopHocDTO createLopHoc(LopHocRequest request) {
        HocVien hocVien = hocVienRepository.findById(request.getMaHocVien())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học viên"));
        GiaSu giaSu = giaSuRepository.findById(request.getMaGiaSu())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy gia sư"));

        MonHoc monHoc = null;
        if (request.getMaMon() != null) {
            monHoc = monHocRepository.findById(request.getMaMon())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy môn học"));
        }

        YeuCauTimGiaSu yeuCau = null;
        if (request.getMaYeuCau() != null) {
            yeuCau = yeuCauTimGiaSuRepository.findById(request.getMaYeuCau()).orElse(null);
        }

        LopHoc lopHoc = LopHoc.builder()
                .hocVien(hocVien)
                .giaSu(giaSu)
                .monHoc(monHoc)
                .yeuCauTimGiaSu(yeuCau)
                .hocPhiThoaThuan(request.getHocPhiThoaThuan())
                .ngayBatDau(request.getNgayBatDau())
                .ngayKetThuc(request.getNgayKetThuc())
                .trangThai("DANG_HOC") // Mặc định khi tạo lớp
                .lichHoc(yeuCau != null ? yeuCau.getLichHocDuKien() : null)
                .tongSoBuoi(request.getTongSoBuoi())
                .soBuoiConLai(request.getTongSoBuoi())
                .build();

        return mapToDTO(lopHocRepository.save(lopHoc));
    }

    public LopHocDTO updateStatus(Integer id, String status) {
        LopHoc lopHoc = lopHocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học với ID: " + id));
        lopHoc.setTrangThai(status);
        return mapToDTO(lopHocRepository.save(lopHoc));
    }

    public LopHocDTO updateLichHoc(Integer id, String lichHoc, String ghiChu) {
        LopHoc lopHoc = lopHocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học với ID: " + id));
        if (lichHoc != null) {
            lopHoc.setLichHoc(lichHoc);
        }
        if (ghiChu != null) {
            lopHoc.setGhiChu(ghiChu);
        }
        return mapToDTO(lopHocRepository.save(lopHoc));
    }

    public LopHocDTO setCongSoBuoi(Integer id, Integer tongSoBuoi) {
        LopHoc lopHoc = lopHocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học với ID: " + id));

        if (lopHoc.getTongSoBuoi() == null) {
            // Lần đầu cài: soBuoiConLai = tongSoBuoi
            lopHoc.setTongSoBuoi(tongSoBuoi);
            lopHoc.setSoBuoiConLai(tongSoBuoi);
        } else {
            // Đã cài rồi: chỉ thêm/bớt PHẦN CHÊNH LỆCH vào soBuoiConLai
            int delta = tongSoBuoi - lopHoc.getTongSoBuoi();
            int newConLai = (lopHoc.getSoBuoiConLai() != null ? lopHoc.getSoBuoiConLai() : 0) + delta;
            lopHoc.setTongSoBuoi(tongSoBuoi);
            lopHoc.setSoBuoiConLai(Math.max(0, newConLai));
        }
        return mapToDTO(lopHocRepository.save(lopHoc));
    }

    // Gia sư bấm "Đã dạy" cho 1 buổi → tạo record + trừ countdown
    public LopHocDTO hoanThanhMoiBuoi(Integer maLop) {
        LopHoc lopHoc = lopHocRepository.findById(maLop)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học với ID: " + maLop));

        if ("HOAN_THANH".equals(lopHoc.getTrangThai())) {
            throw new RuntimeException("Lớp học đã hoàn thành");
        }

        // Tạo record buổi học DA_HOC
        LocalDateTime now = LocalDateTime.now();
        BuoiHoc buoiHoc = BuoiHoc.builder()
                .lopHoc(lopHoc)
                .thoiGianBatDau(now)
                .thoiGianKetThuc(now)
                .trangThai("DA_HOC")
                .build();
        buoiHocRepository.save(buoiHoc);

        // Countdown
        if (lopHoc.getSoBuoiConLai() != null && lopHoc.getSoBuoiConLai() > 0) {
            lopHoc.setSoBuoiConLai(lopHoc.getSoBuoiConLai() - 1);
        }
        if (lopHoc.getSoBuoiConLai() != null && lopHoc.getSoBuoiConLai() == 0) {
            lopHoc.setTrangThai("HOAN_THANH");
        }

        return mapToDTO(lopHocRepository.save(lopHoc));
    }

    private LopHocDTO mapToDTO(LopHoc lopHoc) {
        return LopHocDTO.builder()
                .maLop(lopHoc.getMaLop())
                .maHocVien(lopHoc.getHocVien() != null ? lopHoc.getHocVien().getMaHocVien() : null)
                .tenHocVien(lopHoc.getHocVien() != null && lopHoc.getHocVien().getTaiKhoan() != null
                        ? lopHoc.getHocVien().getTaiKhoan().getHoTen()
                        : null)
                .maGiaSu(lopHoc.getGiaSu() != null ? lopHoc.getGiaSu().getMaGiaSu() : null)
                .tenGiaSu(lopHoc.getGiaSu() != null && lopHoc.getGiaSu().getTaiKhoan() != null
                        ? lopHoc.getGiaSu().getTaiKhoan().getHoTen()
                        : null)
                .maMon(lopHoc.getMonHoc() != null ? lopHoc.getMonHoc().getMaMon() : null)
                .tenMonHoc(lopHoc.getMonHoc() != null ? lopHoc.getMonHoc().getTenMon() : null)
                .maYeuCau(lopHoc.getYeuCauTimGiaSu() != null ? lopHoc.getYeuCauTimGiaSu().getMaYeuCau() : null)
                .hocPhiThoaThuan(lopHoc.getHocPhiThoaThuan())
                .ngayBatDau(lopHoc.getNgayBatDau())
                .ngayKetThuc(lopHoc.getNgayKetThuc())
                .trangThai(lopHoc.getTrangThai())
                .lichHoc(lopHoc.getLichHoc())
                .ghiChu(lopHoc.getGhiChu())
                .tongSoBuoi(lopHoc.getTongSoBuoi())
                .soBuoiConLai(lopHoc.getSoBuoiConLai())
                .build();
    }

}
