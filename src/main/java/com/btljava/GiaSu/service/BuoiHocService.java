package com.btljava.GiaSu.service;

import com.btljava.GiaSu.dto.BuoiHocDTO;
import com.btljava.GiaSu.dto.BuoiHocRequest;
import com.btljava.GiaSu.entity.BuoiHoc;
import com.btljava.GiaSu.entity.LopHoc;
import com.btljava.GiaSu.repository.BuoiHocRepository;
import com.btljava.GiaSu.repository.LopHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuoiHocService {

    private final BuoiHocRepository buoiHocRepository;
    private final LopHocRepository lopHocRepository;

    public List<BuoiHocDTO> getBuoiHocByLop(Integer maLop) {
        return buoiHocRepository.findByLopHoc_MaLop(maLop).stream()
                .map(this::mapToDTO).collect(Collectors.toList());
    }

    public BuoiHocDTO createBuoiHoc(BuoiHocRequest request) {
        LopHoc lopHoc = lopHocRepository.findById(request.getMaLop())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học"));

        BuoiHoc buoiHoc = BuoiHoc.builder()
                .lopHoc(lopHoc)
                .thoiGianBatDau(request.getThoiGianBatDau())
                .thoiGianKetThuc(request.getThoiGianKetThuc())
                .soGioHoc(request.getSoGioHoc())
                .trangThai("CHUA_HOC") // Mặc định khi thêm buổi học mới
                .build();

        return mapToDTO(buoiHocRepository.save(buoiHoc));
    }

    public BuoiHocDTO updateStatus(Integer id, String status) {
        BuoiHoc buoiHoc = buoiHocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy buổi học với ID: " + id));
        buoiHoc.setTrangThai(status);
        return mapToDTO(buoiHocRepository.save(buoiHoc));
    }

    public void deleteBuoiHoc(Integer id) {
        if (!buoiHocRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy buổi học với ID: " + id);
        }
        buoiHocRepository.deleteById(id);
    }

    private BuoiHocDTO mapToDTO(BuoiHoc buoiHoc) {
        return BuoiHocDTO.builder()
                .maBuoi(buoiHoc.getMaBuoi())
                .maLop(buoiHoc.getLopHoc() != null ? buoiHoc.getLopHoc().getMaLop() : null)
                .thoiGianBatDau(buoiHoc.getThoiGianBatDau())
                .thoiGianKetThuc(buoiHoc.getThoiGianKetThuc())
                .soGioHoc(buoiHoc.getSoGioHoc())
                .trangThai(buoiHoc.getTrangThai())
                .build();
    }
}
