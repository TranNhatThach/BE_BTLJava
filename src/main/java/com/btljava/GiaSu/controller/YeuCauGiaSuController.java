package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.entity.MonHoc;
import com.btljava.GiaSu.entity.YeuCauTimGiaSu;
import com.btljava.GiaSu.dto.YeuCauTimGiaSuRequest;
import com.btljava.GiaSu.repository.MonHocRepository;
import com.btljava.GiaSu.service.YeuCauGiaSuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class YeuCauGiaSuController {

    private final YeuCauGiaSuService yeuCauService;
    private final MonHocRepository monHocRepository;

    @GetMapping("/mon-hoc")
    public List<MonHoc> findAll() {
        return monHocRepository.findAll();
    }

    @PostMapping("/dang-bai")
    public YeuCauTimGiaSu dangBai(@RequestBody YeuCauTimGiaSuRequest yeucau) {
        return yeuCauService.luuBaiDang(yeucau);
    }

    @GetMapping("/danh-sach-yeu-cau")
    public List<YeuCauTimGiaSu> danhSachBaiDang() {
        return yeuCauService.layTatCa();
    }

    @GetMapping("/danh-sach-yeu-cau/{maHocVien}")
    public List<YeuCauTimGiaSu> danhSachYeuCauCuaHocVien(@PathVariable Integer maHocVien) {
        return yeuCauService.layCuaHocVien(maHocVien);
    }
}
