package com.btljava.GiaSu.service;

import com.btljava.GiaSu.dto.GiaSuResponse;
import com.btljava.GiaSu.entity.GiaSuMonHoc;
import com.btljava.GiaSu.entity.DanhGia;
import com.btljava.GiaSu.repository.DanhGiaRepository;
import com.btljava.GiaSu.repository.LopHocRepository;
import com.btljava.GiaSu.repository.GiaSuMonHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class GiaSuService {
    private final GiaSuMonHocRepository giaSuMonHocRepository;
    private final DanhGiaRepository danhGiaRepository;
    private final LopHocRepository lopHocRepository;

    public Page<GiaSuResponse> timKiemGiaSu(String monHoc, String trinhDo, String viTri, Double minGia,
            Double maxGia, Integer maGiaSu, int page, int size) {

        String monHocClean = (monHoc != null && !monHoc.trim().isEmpty()) ? monHoc.trim() : null;
        String trinhDoClean = (trinhDo != null && !trinhDo.trim().isEmpty()) ? trinhDo.trim() : null;
        String viTriClean = (viTri != null && !viTri.trim().isEmpty()) ? viTri.trim() : null;

        Pageable pageable = PageRequest.of(page, size);

        Page<GiaSuMonHoc> list = giaSuMonHocRepository.findByFilter(
                monHocClean, trinhDoClean, viTriClean, minGia, maxGia, maGiaSu, pageable);

        return list.map(gsmh -> {
            Integer currentMaGiaSu = gsmh.getGiaSu().getMaGiaSu();

            // Tính điểm đánh giá trung bình
            Double avgRating = danhGiaRepository.findByLopHoc_GiaSu_MaGiaSu(currentMaGiaSu)
                    .stream()
                    .mapToInt(DanhGia::getDiem)
                    .average()
                    .orElse(0.0);

            // Làm tròn 1 chữ số thập phân
            avgRating = Math.round(avgRating * 10) / 10.0;

            // Tính số học viên (tổng số lớp đã dạy hoặc số học viên khác nhau)
            Integer studentCount = (int) lopHocRepository.findByGiaSu_MaGiaSu(currentMaGiaSu)
                    .stream()
                    .map(lh -> lh.getHocVien().getMaHocVien())
                    .distinct()
                    .count();

            return GiaSuResponse.builder()
                    .maGiaSu(currentMaGiaSu)
                    .username(gsmh.getGiaSu().getTaiKhoan().getHoTen())
                    .monHoc(gsmh.getMonHoc().getTenMon())
                    .trinhDo(gsmh.getTrinhDo())
                    .viTri(gsmh.getGiaSu().getTaiKhoan().getViTri())
                    .gioitinh(gsmh.getGiaSu().getGioiTinh())
                    .hocPhiMoiGio(
                            gsmh.getHocPhiMoiGio() != null
                                    ? gsmh.getHocPhiMoiGio().doubleValue()
                                    : null)
                    .truongDaiHoc(gsmh.getGiaSu().getTruongDaiHoc())
                    .chuyenNganh(gsmh.getGiaSu().getChuyenNganh())
                    .soNamKinhNghiem(gsmh.getGiaSu().getSoNamKinhNghiem())
                    .diemDanhGia(avgRating)
                    .soHocVien(studentCount)
                    .moTa(gsmh.getGiaSu().getMoTa())
                    .build();
        });
    }
}
