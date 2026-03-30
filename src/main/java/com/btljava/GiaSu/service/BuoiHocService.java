package com.btljava.GiaSu.service;

import com.btljava.GiaSu.dto.BuoiHocDTO;
import com.btljava.GiaSu.dto.BuoiHocRequest;
import com.btljava.GiaSu.entity.BuoiHoc;
import com.btljava.GiaSu.entity.LopHoc;
import com.btljava.GiaSu.repository.BuoiHocRepository;
import com.btljava.GiaSu.repository.LopHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .trangThai("CHUA_HOC") // Mặc định khi thêm buổi học mới
                .build();

        return mapToDTO(buoiHocRepository.save(buoiHoc));
    }

    @Transactional
    public BuoiHocDTO updateStatus(Integer id, String status) {
        BuoiHoc buoiHoc = buoiHocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy buổi học với ID: " + id));

        String oldStatus = buoiHoc.getTrangThai();
        buoiHoc.setTrangThai(status);
        buoiHocRepository.save(buoiHoc);

        // Nếu vừa đánh dấu DA_HOC (và trước đó chưa phải DA_HOC) → countdown lớp học
        if ("DA_HOC".equals(status) && !"DA_HOC".equals(oldStatus)) {
            LopHoc lopHoc = buoiHoc.getLopHoc();
            if (lopHoc != null && lopHoc.getSoBuoiConLai() != null) {
                int conLai = lopHoc.getSoBuoiConLai() - 1;
                lopHoc.setSoBuoiConLai(Math.max(conLai, 0));

                // Tự động kết thúc lớp khi hết buổi học
                if (lopHoc.getSoBuoiConLai() == 0) {
                    lopHoc.setTrangThai("HOAN_THANH");
                }
                lopHocRepository.save(lopHoc);
            }
        }

        // Nếu undo: đánh dấu CHUA_HOC lại (trước đó là DA_HOC) → cộng lại 1 buổi
        if ("CHUA_HOC".equals(status) && "DA_HOC".equals(oldStatus)) {
            LopHoc lopHoc = buoiHoc.getLopHoc();
            if (lopHoc != null && lopHoc.getSoBuoiConLai() != null && lopHoc.getTongSoBuoi() != null) {
                int conLai = lopHoc.getSoBuoiConLai() + 1;
                lopHoc.setSoBuoiConLai(Math.min(conLai, lopHoc.getTongSoBuoi()));
                // Nếu lớp đã HOAN_THANH nhưng undo thì mở lại
                if ("HOAN_THANH".equals(lopHoc.getTrangThai())) {
                    lopHoc.setTrangThai("DANG_HOC");
                }
                lopHocRepository.save(lopHoc);
            }
        }

        return mapToDTO(buoiHoc);
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
                .trangThai(buoiHoc.getTrangThai())
                .build();
    }
}
