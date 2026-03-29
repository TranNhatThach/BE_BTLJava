package com.btljava.GiaSu.service;

import com.btljava.GiaSu.dto.LopHocDTO;
import com.btljava.GiaSu.dto.LopHocRequest;
import com.btljava.GiaSu.entity.GiaSu;
import com.btljava.GiaSu.entity.HocVien;
import com.btljava.GiaSu.entity.LopHoc;
import com.btljava.GiaSu.entity.MonHoc;
import com.btljava.GiaSu.entity.YeuCauTimGiaSu;
import com.btljava.GiaSu.repository.GiaSuRepository;
import com.btljava.GiaSu.repository.HocVienRepository;
import com.btljava.GiaSu.repository.LopHocRepository;
import com.btljava.GiaSu.repository.MonHocRepository;
import com.btljava.GiaSu.repository.YeuCauTimGiaSuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LopHocService {

    private final LopHocRepository lopHocRepository;
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

    private LopHocDTO mapToDTO(LopHoc lopHoc) {
        return LopHocDTO.builder()
                .maLop(lopHoc.getMaLop())
                .maHocVien(lopHoc.getHocVien() != null ? lopHoc.getHocVien().getMaHocVien() : null)
                .tenHocVien(lopHoc.getHocVien() != null && lopHoc.getHocVien().getTaiKhoan() != null ? lopHoc.getHocVien().getTaiKhoan().getHoTen() : null)
                .maGiaSu(lopHoc.getGiaSu() != null ? lopHoc.getGiaSu().getMaGiaSu() : null)
                .tenGiaSu(lopHoc.getGiaSu() != null && lopHoc.getGiaSu().getTaiKhoan() != null ? lopHoc.getGiaSu().getTaiKhoan().getHoTen() : null)
                .maMon(lopHoc.getMonHoc() != null ? lopHoc.getMonHoc().getMaMon() : null)
                .tenMonHoc(lopHoc.getMonHoc() != null ? lopHoc.getMonHoc().getTenMon() : null)
                .maYeuCau(lopHoc.getYeuCauTimGiaSu() != null ? lopHoc.getYeuCauTimGiaSu().getMaYeuCau() : null)
                .hocPhiThoaThuan(lopHoc.getHocPhiThoaThuan())
                .ngayBatDau(lopHoc.getNgayBatDau())
                .ngayKetThuc(lopHoc.getNgayKetThuc())
                .trangThai(lopHoc.getTrangThai())
                .lichHoc(lopHoc.getLichHoc())
                .ghiChu(lopHoc.getGhiChu())
                .build();
    }
}
