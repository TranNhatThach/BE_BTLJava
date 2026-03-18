package com.btljava.GiaSu.service;

import com.btljava.GiaSu.dto.GiaSuResponse;
import com.btljava.GiaSu.entity.GiaSuMonHoc;
import com.btljava.GiaSu.repository.GiaSuMonHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GiaSuService {
    private final GiaSuMonHocRepository giaSuMonHocRepository;

    public List<GiaSuResponse> timKiemGiaSu(String monHoc, String trinhDo, String viTri, Double minGia, Double maxGia) {

        String monHocClean = (monHoc != null && !monHoc.trim().isEmpty()) ? monHoc.trim() : null;
        String trinhDoClean = (trinhDo != null && !trinhDo.trim().isEmpty()) ? trinhDo.trim() : null;
        String viTriClean = (viTri != null && !viTri.trim().isEmpty()) ? viTri.trim() : null;

        List<GiaSuMonHoc> list = giaSuMonHocRepository.findByFilter(
                monHocClean, trinhDoClean, viTriClean, minGia, maxGia
        );

        return list.stream().map(gsmh -> GiaSuResponse.builder()
                .username(gsmh.getGiaSu().getTaiKhoan().getHoTen())
                .monHoc(gsmh.getMonHoc().getTenMon())
                .trinhDo(gsmh.getTrinhDo())
                .viTri(gsmh.getGiaSu().getTaiKhoan().getViTri())
                .gioitinh(gsmh.getGiaSu().getGioiTinh())
                .hocPhiMoiGio(
                        gsmh.getHocPhiMoiGio() != null
                                ? gsmh.getHocPhiMoiGio().doubleValue()
                                : null
                )
                .build()
        ).toList();
    }
}
