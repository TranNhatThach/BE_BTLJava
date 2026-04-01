package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.entity.TaiKhoan;
import com.btljava.GiaSu.service.TaiKhoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdminController {
    private final TaiKhoanService taiKhoanService;

    @GetMapping("/show")
    public List<TaiKhoan> findAll() {
        return taiKhoanService.getAllTaiKhoan();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaiKhoan(@PathVariable Integer id) {
        taiKhoanService.deleteTaiKhoan(id);
        return ResponseEntity.ok("Tài khoản đã được xóa thành công!");
    }

    @GetMapping("/gia-su")
    public ResponseEntity<List<TaiKhoan>> getAllGiaSu() {
        return ResponseEntity.ok(taiKhoanService.getAllGiaSu());
    }

    @GetMapping("/hoc-vien")
    public ResponseEntity<List<TaiKhoan>> getAllHocVien() {
        return ResponseEntity.ok(taiKhoanService.getAllHocVien());
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<TaiKhoan>> getDeletedTaiKhoan() {
        return ResponseEntity.ok(taiKhoanService.getDeletedTaiKhoan());
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<String> restoreTaiKhoan(@PathVariable Integer id) {
        taiKhoanService.restoreTaiKhoan(id);
        return ResponseEntity.ok("Tài khoản đã được khôi phục thành công!");
    }

    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<String> forceDeleteTaiKhoan(@PathVariable Integer id) {
        taiKhoanService.forceDeleteTaiKhoan(id);
        return ResponseEntity.ok("Tài khoản đã được xóa vĩnh viễn!");
    }

}
