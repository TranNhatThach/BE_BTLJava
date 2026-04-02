package com.btljava.GiaSu.service;

import com.btljava.GiaSu.dto.GiaSuResponse;
import com.btljava.GiaSu.entity.GiaSuMonHoc;
import com.btljava.GiaSu.entity.DanhGia;
import com.btljava.GiaSu.entity.GiaSu;
import com.btljava.GiaSu.repository.DanhGiaRepository;
import com.btljava.GiaSu.repository.GiaSuRepository;
import com.btljava.GiaSu.repository.GiaSuMonHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiaSuService {
    private final GiaSuRepository giaSuRepository;
    private final GiaSuMonHocRepository giaSuMonHocRepository;
    private final DanhGiaRepository danhGiaRepository;

    public Page<GiaSuResponse> timKiemGiaSu(String monHoc, String trinhDo, String viTri, Double minGia,
            Double maxGia, Integer maGiaSu, int page, int size) {

        String monHocClean = (monHoc != null && !monHoc.trim().isEmpty()) ? "%" + monHoc.trim() + "%" : null;
        String trinhDoClean = (trinhDo != null && !trinhDo.trim().isEmpty()) ? "%" + trinhDo.trim() + "%" : null;
        String viTriClean = (viTri != null && !viTri.trim().isEmpty()) ? "%" + viTri.trim() + "%" : null;

        Pageable pageable = PageRequest.of(page, size);

        Page<GiaSu> list = giaSuRepository.findByFilter(
                monHocClean, trinhDoClean, viTriClean, minGia, maxGia, maGiaSu, pageable);

        return list.map(gs -> {
            Integer currentMaGiaSu = gs.getMaGiaSu();

            // Tính điểm đánh giá trung bình
            List<DanhGia> danhGias = danhGiaRepository.findByLopHoc_GiaSu_MaGiaSu(currentMaGiaSu);
            Double avgRating = danhGias.stream()
                    .mapToInt(DanhGia::getDiem)
                    .average()
                    .orElse(0.0);

            // Làm tròn 1 chữ số thập phân
            avgRating = Math.round(avgRating * 10) / 10.0;

            // Tính số học viện
            // Đã đổi qua dùng số lượt đánh giá (danhGias.size()) cho logic UI mới

            // Lấy các môn học
            List<GiaSuMonHoc> mhList = giaSuMonHocRepository.findByGiaSu_MaGiaSu(currentMaGiaSu);
            String monHocs = mhList.isEmpty() ? null : mhList.stream()
                    .map(m -> m.getMonHoc().getTenMon())
                    .distinct()
                    .collect(Collectors.joining(", "));
            
            Double minPrice = mhList.stream()
                    .map(GiaSuMonHoc::getHocPhiMoiGio)
                    .filter(java.util.Objects::nonNull)
                    .mapToDouble(n -> ((Number)n).doubleValue())
                    .min()
                    .orElse(0.0);
            
            String trinhDos = mhList.isEmpty() ? null : mhList.stream()
                    .map(GiaSuMonHoc::getTrinhDo)
                    .distinct()
                    .collect(Collectors.joining(", "));

            return GiaSuResponse.builder()
                    .maGiaSu(currentMaGiaSu)
                    .username(gs.getTaiKhoan().getHoTen())
                    .hoTen(gs.getTaiKhoan().getHoTen())
                    .monHoc(monHocs)
                    .trinhDo(trinhDos)
                    .viTri(gs.getTaiKhoan().getViTri())
                    .gioitinh(gs.getGioiTinh())
                    .hocPhiMoiGio(minPrice > 0 ? minPrice : null)
                    .truongDaiHoc(gs.getTruongDaiHoc())
                    .chuyenNganh(gs.getChuyenNganh())
                    .soNamKinhNghiem(gs.getSoNamKinhNghiem())
                    .namSinh(gs.getNamSinh())
                    .diemDanhGia(avgRating)
                    .soHocVien(danhGias.size()) // Use total reviews instead of unique students, per user
                    .moTa(gs.getMoTa())
                    .avatar(gs.getTaiKhoan().getAvatar())
                    .build();
        });
    }
}
