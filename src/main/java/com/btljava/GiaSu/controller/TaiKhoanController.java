package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.dto.TaiKhoanResponse;
import com.btljava.GiaSu.service.JwtService;
import com.btljava.GiaSu.service.TaiKhoanService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tai-khoan")
@RequiredArgsConstructor
public class TaiKhoanController {

    private final TaiKhoanService taiKhoanService;
    private final JwtService jwt;

    @GetMapping("/profile")
    public ResponseEntity<TaiKhoanResponse> getTaiKhoan(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String token = authHeader.substring(7);
        Integer userId = jwt.extractUserId(token);
        return ResponseEntity.ok(taiKhoanService.getById(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<TaiKhoanResponse> updateProfile(
            HttpServletRequest request,
            @RequestBody TaiKhoanResponse updated) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authHeader.substring(7);

        Integer userId = jwt.extractUserId(token);

        return ResponseEntity.ok(taiKhoanService.updateTaiKhoan(userId, updated));
    }
}