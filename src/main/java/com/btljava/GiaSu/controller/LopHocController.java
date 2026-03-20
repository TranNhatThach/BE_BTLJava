package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.dto.LopHocDTO;
import com.btljava.GiaSu.dto.LopHocRequest;
import com.btljava.GiaSu.service.LopHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lop-hoc")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LopHocController {

    private final LopHocService lopHocService;

    @GetMapping("/hoc-vien/{maHocVien}")
    public ResponseEntity<List<LopHocDTO>> getByHocVien(@PathVariable Integer maHocVien) {
        return ResponseEntity.ok(lopHocService.getLopHocByHocVien(maHocVien));
    }

    @GetMapping("/gia-su/{maGiaSu}")
    public ResponseEntity<List<LopHocDTO>> getByGiaSu(@PathVariable Integer maGiaSu) {
        return ResponseEntity.ok(lopHocService.getLopHocByGiaSu(maGiaSu));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LopHocDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(lopHocService.getById(id));
    }

    @PostMapping
    public ResponseEntity<LopHocDTO> createLopHoc(@RequestBody LopHocRequest request) {
        return ResponseEntity.ok(lopHocService.createLopHoc(request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<LopHocDTO> updateStatus(@PathVariable Integer id, @RequestParam String status) {
        return ResponseEntity.ok(lopHocService.updateStatus(id, status));
    }
}
