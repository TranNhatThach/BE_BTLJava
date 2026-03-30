package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.dto.LopHocDTO;
import com.btljava.GiaSu.dto.LopHocRequest;
import com.btljava.GiaSu.entity.GiaSu;
import com.btljava.GiaSu.entity.HocVien;
import com.btljava.GiaSu.entity.TaiKhoan;
import com.btljava.GiaSu.repository.GiaSuRepository;
import com.btljava.GiaSu.repository.HocVienRepository;
import com.btljava.GiaSu.repository.TaiKhoanRepository;
import com.btljava.GiaSu.service.JwtService;
import com.btljava.GiaSu.service.LopHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/lop-hoc")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LopHocController {

    private final LopHocService lopHocService;
    private final JwtService jwtService;
    private final TaiKhoanRepository taiKhoanRepository;
    private final HocVienRepository hocVienRepository;
    private final GiaSuRepository giaSuRepository;

    @GetMapping("/cua-toi")
    public ResponseEntity<?> getMyLopHoc(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token không hợp lệ");
        }
        String token = authHeader.substring(7);
        Integer maTaiKhoan = jwtService.extractUserId(token);

        Optional<TaiKhoan> optionalTaiKhoan = taiKhoanRepository.findById(maTaiKhoan);
        if (optionalTaiKhoan.isEmpty()) {
            return ResponseEntity.badRequest().body("Không tìm thấy người dùng");
        }

        TaiKhoan taiKhoan = optionalTaiKhoan.get();
        if ("HOC_VIEN".equals(taiKhoan.getVaiTro())) {
            HocVien hocVien = hocVienRepository.findByTaiKhoan(taiKhoan);
            if (hocVien != null) {
                return ResponseEntity.ok(lopHocService.getLopHocByHocVien(hocVien.getMaHocVien()));
            } else {
                return ResponseEntity.badRequest().body("Tài khoản chưa có hồ sơ Học Viên");
            }
        } else if ("GIA_SU".equals(taiKhoan.getVaiTro())) {
            GiaSu giaSu = giaSuRepository.findByTaiKhoan(taiKhoan);
            if (giaSu != null) {
                return ResponseEntity.ok(lopHocService.getLopHocByGiaSu(giaSu.getMaGiaSu()));
            } else {
                return ResponseEntity.badRequest().body("Tài khoản chưa có hồ sơ Gia Sư");
            }
        }

        return ResponseEntity.badRequest().body("Vai trò không hợp lệ");
    }

    @GetMapping("/{id}")
    public ResponseEntity<LopHocDTO> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(lopHocService.getById(id));
    }

    @PostMapping
    public ResponseEntity<?> createLopHoc(
            @RequestBody LopHocRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token không hợp lệ");
        }
        String token = authHeader.substring(7);
        Integer maTaiKhoan = jwtService.extractUserId(token);

        Optional<TaiKhoan> optionalTaiKhoan = taiKhoanRepository.findById(maTaiKhoan);
        if (optionalTaiKhoan.isEmpty()) {
            return ResponseEntity.badRequest().body("Không tìm thấy người dùng");
        }

        TaiKhoan taiKhoan = optionalTaiKhoan.get();
        if ("HOC_VIEN".equals(taiKhoan.getVaiTro())) {
            HocVien hocVien = hocVienRepository.findByTaiKhoan(taiKhoan);
            if (hocVien != null) {
                request.setMaHocVien(hocVien.getMaHocVien());
            } else {
                return ResponseEntity.badRequest().body("Tài khoản chưa có hồ sơ Học Viên để tạo lớp");
            }
        } else if ("GIA_SU".equals(taiKhoan.getVaiTro())) {
            GiaSu giaSu = giaSuRepository.findByTaiKhoan(taiKhoan);
            if (giaSu != null) {
                request.setMaGiaSu(giaSu.getMaGiaSu());
            } else {
                return ResponseEntity.badRequest().body("Tài khoản chưa có hồ sơ Gia Sư để tạo lớp");
            }
        } else {
            return ResponseEntity.badRequest().body("Vai trò không có quyền tạo lớp học");
        }

        return ResponseEntity.ok(lopHocService.createLopHoc(request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<LopHocDTO> updateStatus(@PathVariable("id") Integer id,
            @RequestParam("status") String status) {
        return ResponseEntity.ok(lopHocService.updateStatus(id, status));
    }

    @PutMapping("/{id}/lich-hoc")
    public ResponseEntity<LopHocDTO> updateLichHoc(@PathVariable("id") Integer id,
            @RequestParam("lichHoc") String lichHoc,
            @RequestParam(value = "ghiChu", required = false) String ghiChu) {
        return ResponseEntity.ok(lopHocService.updateLichHoc(id, lichHoc, ghiChu));
    }

    @PutMapping("/{id}/so-buoi")
    public ResponseEntity<LopHocDTO> setCongSoBuoi(@PathVariable("id") Integer id,
            @RequestParam("tongSoBuoi") Integer tongSoBuoi) {
        return ResponseEntity.ok(lopHocService.setCongSoBuoi(id, tongSoBuoi));
    }

    @PostMapping("/{id}/hoan-thanh-buoi")
    public ResponseEntity<LopHocDTO> hoanThanhMoiBuoi(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(lopHocService.hoanThanhMoiBuoi(id));
    }
}
