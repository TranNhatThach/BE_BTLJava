package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.entity.ThanhToan;
import com.btljava.GiaSu.service.ThanhToanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/thanh-toan")
@RequiredArgsConstructor
public class ThanhToanController {

    private final ThanhToanService thanhToanService;

    // Lọc theo trạng thái: GET /api/thanh-toan/filter?status=Da thanh toan
    @GetMapping("/filter")
    public ResponseEntity<List<ThanhToan>> filterByStatus(@RequestParam String status) {
        return ResponseEntity.ok(thanhToanService.layDanhSachTheoTrangThai(status));
    }

    // Xem theo lớp: GET /api/thanh-toan/lop/1
    @GetMapping("/lop/{maLop}")
    public ResponseEntity<List<ThanhToan>> getByClass(@PathVariable Integer maLop) {
        return ResponseEntity.ok(thanhToanService.layLichSuThanhToanLop(maLop));
    }
}