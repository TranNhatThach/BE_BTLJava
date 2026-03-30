package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.dto.GiaSuMonHocRequest;
import com.btljava.GiaSu.dto.GiaSuMonHocResponse;
import com.btljava.GiaSu.service.GiaSuMonHocService;
import com.btljava.GiaSu.service.JwtService;
import com.btljava.GiaSu.repository.GiaSuRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gia-su-mon-hoc")
@RequiredArgsConstructor
@CrossOrigin("*")
public class GiaSuMonHocController {

    private final GiaSuMonHocService giaSuMonHocService;
    private final JwtService jwt;
    private final GiaSuRepository giaSuRepository;

    private Integer getMaGiaSuFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Chưa xác thực");
        }
        String token = authHeader.substring(7);
        Integer userId = jwt.extractUserId(token);

        return giaSuRepository.findByTaiKhoan_MaTaiKhoan(userId)
                .map(com.btljava.GiaSu.entity.GiaSu::getMaGiaSu)
                .orElseThrow(() -> new RuntimeException("Tài khoản này không phải gia sư"));
    }

    @GetMapping("/me")
    public ResponseEntity<List<GiaSuMonHocResponse>> getMyMonHoc(HttpServletRequest request) {
        Integer maGiaSu = getMaGiaSuFromRequest(request);
        return ResponseEntity.ok(giaSuMonHocService.getMonHocByGiaSu(maGiaSu));
    }

    @PostMapping("/me")
    public ResponseEntity<GiaSuMonHocResponse> addMyMonHoc(
            HttpServletRequest request,
            @RequestBody GiaSuMonHocRequest reqBody) {
        Integer maGiaSu = getMaGiaSuFromRequest(request);
        return ResponseEntity.ok(giaSuMonHocService.addMonHocToGiaSu(maGiaSu, reqBody));
    }

    @PutMapping("/me/{maMon}")
    public ResponseEntity<GiaSuMonHocResponse> updateMyMonHoc(
            HttpServletRequest request,
            @PathVariable("maMon") Integer maMon,
            @RequestBody GiaSuMonHocRequest reqBody) {
        Integer maGiaSu = getMaGiaSuFromRequest(request);
        return ResponseEntity.ok(giaSuMonHocService.updateMonHocGiaSu(maGiaSu, maMon, reqBody));
    }

    @DeleteMapping("/me/{maMon}")
    public ResponseEntity<Void> removeMyMonHoc(
            HttpServletRequest request,
            @PathVariable("maMon") Integer maMon) {
        Integer maGiaSu = getMaGiaSuFromRequest(request);
        giaSuMonHocService.removeMonHocGiaSu(maGiaSu, maMon);
        return ResponseEntity.noContent().build();
    }
}

