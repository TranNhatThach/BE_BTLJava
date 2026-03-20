package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.dto.TaiKhoanResponse;
import com.btljava.GiaSu.entity.TaiKhoan;
import com.btljava.GiaSu.service.TaiKhoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tai-khoan")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TaiKhoanController {

    private final TaiKhoanService taiKhoanService;

    @GetMapping("/{id}")
    public ResponseEntity<TaiKhoanResponse> getTaiKhoan(@PathVariable Integer id) {
        return ResponseEntity.ok(taiKhoanService.getById(id));
    }

    @PutMapping("/{username}")
    public ResponseEntity<TaiKhoan> updateTaiKhoan(
            @PathVariable String username,
            @RequestBody TaiKhoan updated) {
        return ResponseEntity.ok(taiKhoanService.updateTaiKhoan(username, updated));
    }
}