package com.btljava.GiaSu.service;

import com.btljava.GiaSu.dto.DanhGiaDTO;
import com.btljava.GiaSu.dto.DanhGiaRequest;
import com.btljava.GiaSu.entity.DanhGia;
import com.btljava.GiaSu.entity.LopHoc;
import com.btljava.GiaSu.repository.DanhGiaRepository;
import com.btljava.GiaSu.repository.LopHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DanhGiaService {

    private final DanhGiaRepository danhGiaRepository;
    private final LopHocRepository lopHocRepository;

    public List<DanhGiaDTO> getDanhGiaByLopHoc(Integer maLop) {
        return danhGiaRepository.findByLopHoc_MaLop(maLop).stream()
                .map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<DanhGiaDTO> getDanhGiaByGiaSu(Integer maGiaSu) {
        return danhGiaRepository.findByLopHoc_GiaSu_MaGiaSu(maGiaSu).stream()
                .map(this::mapToDTO).collect(Collectors.toList());
    }

    public DanhGiaDTO createDanhGia(DanhGiaRequest request) {
        LopHoc lopHoc = lopHocRepository.findById(request.getMaLop())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học"));

        if (danhGiaRepository.existsByLopHoc_MaLop(request.getMaLop())) {
            throw new RuntimeException("Lớp học này đã được đánh giá rồi. Hãy dùng chức năng cập nhật.");
        }

        DanhGia danhGia = DanhGia.builder()
                .lopHoc(lopHoc)
                .diem(request.getDiem())
                .nhanXet(request.getNhanXet())
                .build();

        return mapToDTO(danhGiaRepository.save(danhGia));
    }

    public DanhGiaDTO updateDanhGia(Integer maDanhGia, DanhGiaRequest request) {
        DanhGia danhGia = danhGiaRepository.findById(maDanhGia)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá với ID: " + maDanhGia));

        danhGia.setDiem(request.getDiem());
        danhGia.setNhanXet(request.getNhanXet());

        return mapToDTO(danhGiaRepository.save(danhGia));
    }

    public void deleteDanhGia(Integer maDanhGia) {
        if (!danhGiaRepository.existsById(maDanhGia)) {
            throw new RuntimeException("Không tìm thấy đánh giá với ID: " + maDanhGia);
        }
        danhGiaRepository.deleteById(maDanhGia);
    }

    private DanhGiaDTO mapToDTO(DanhGia danhGia) {
        return DanhGiaDTO.builder()
                .maDanhGia(danhGia.getMaDanhGia())
                .maLop(danhGia.getLopHoc() != null ? danhGia.getLopHoc().getMaLop() : null)
                .diem(danhGia.getDiem())
                .nhanXet(danhGia.getNhanXet())
                .thoiGianDanhGia(danhGia.getThoiGianDanhGia())
                .build();
    }
}
