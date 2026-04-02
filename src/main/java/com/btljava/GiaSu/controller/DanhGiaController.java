package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.dto.DanhGiaDTO;
import com.btljava.GiaSu.dto.DanhGiaRequest;
import com.btljava.GiaSu.service.DanhGiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/danh-gia")
@RequiredArgsConstructor
public class DanhGiaController {

    private final DanhGiaService danhGiaService;

    @GetMapping("/lop-hoc/{maLop}")
    public ResponseEntity<List<DanhGiaDTO>> getDanhGiaByLopHoc(@PathVariable("maLop") Integer maLop) {
        return ResponseEntity.ok(danhGiaService.getDanhGiaByLopHoc(maLop));
    }

    @GetMapping("/gia-su/{maGiaSu}")
    public ResponseEntity<List<DanhGiaDTO>> getDanhGiaByGiaSu(@PathVariable("maGiaSu") Integer maGiaSu) {
        return ResponseEntity.ok(danhGiaService.getDanhGiaByGiaSu(maGiaSu));
    }

    @PostMapping
    public ResponseEntity<DanhGiaDTO> createDanhGia(@RequestBody DanhGiaRequest request) {
        return ResponseEntity.ok(danhGiaService.createDanhGia(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DanhGiaDTO> updateDanhGia(@PathVariable("id") Integer id,
            @RequestBody DanhGiaRequest request) {
        return ResponseEntity.ok(danhGiaService.updateDanhGia(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDanhGia(@PathVariable("id") Integer id) {
        danhGiaService.deleteDanhGia(id);
        return ResponseEntity.noContent().build();
    }
}
