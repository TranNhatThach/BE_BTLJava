package com.btljava.GiaSu.service;

import com.btljava.GiaSu.entity.HocVien;
import com.btljava.GiaSu.entity.MonHoc;
import com.btljava.GiaSu.entity.YeuCauTimGiaSu;
import com.btljava.GiaSu.dto.YeuCauTimGiaSuRequest;
import com.btljava.GiaSu.repository.HocVienRepository;
import com.btljava.GiaSu.repository.MonHocRepository;
import com.btljava.GiaSu.repository.YeuCauTimGiaSuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class YeuCauGiaSuService {
    private final YeuCauTimGiaSuRepository repo;
    private final HocVienRepository hocVienRepository;
    private final MonHocRepository monHocRepository;

    public YeuCauTimGiaSu luuBaiDang(YeuCauTimGiaSuRequest request) {
        HocVien hocVien = hocVienRepository.findById(request.getMaHocVien())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học viên với ID: " + request.getMaHocVien()));

        MonHoc monHoc = monHocRepository.findByTenMon(request.getTenMon());
        if (monHoc == null) {
            monHoc = new MonHoc();
            monHoc.setTenMon(request.getTenMon());
            monHoc = monHocRepository.save(monHoc);
        }

        YeuCauTimGiaSu yeuCau = new YeuCauTimGiaSu();
        yeuCau.setHocVien(hocVien);
        yeuCau.setMonHoc(monHoc);
        yeuCau.setTrinhDo(request.getTrinhDo());
        yeuCau.setLichHocDuKien(request.getLichHocDuKien());
        yeuCau.setHinhThuc(request.getHinhThuc());
        yeuCau.setDiaDiem(request.getDiaDiem());
        yeuCau.setNganSachMin(request.getNganSachMin());
        yeuCau.setNganSachMax(request.getNganSachMax());
        yeuCau.setMoTa(request.getMoTa());
        yeuCau.setTrangThai("CHỜ DUYỆT");

        return repo.save(yeuCau);
    }

    public List<YeuCauTimGiaSu> layTatCa() {
        // Chỉ lấy những yêu cầu CHỜ DUYỆT hoặc MỞ, không hiển thị các yêu cầu ĐÃ GIAO hoặc TRỰC TIẾP
        return repo.findByTrangThaiIn(java.util.Arrays.asList("CHỜ DUYỆT", "MỞ"));
    }

    public List<YeuCauTimGiaSu> layCuaHocVien(Integer maHocVien) {
        // Học viên xem được toàn bộ yêu cầu của chính họ, bao gồm cả Mời trực tiếp hoặc TỪ CHỐI
        return repo.findByHocVien_MaHocVien(maHocVien);
    }
}
