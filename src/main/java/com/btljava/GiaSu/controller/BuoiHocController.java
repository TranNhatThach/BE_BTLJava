package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.dto.BuoiHocDTO;
import com.btljava.GiaSu.dto.BuoiHocRequest;
import com.btljava.GiaSu.service.BuoiHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buoi-hoc")
@RequiredArgsConstructor
public class BuoiHocController {

    private final BuoiHocService buoiHocService;

    @GetMapping("/lop/{maLop}")
    public ResponseEntity<List<BuoiHocDTO>> getByLop(@PathVariable("maLop") Integer maLop) {
        return ResponseEntity.ok(buoiHocService.getBuoiHocByLop(maLop));
    }

    @PostMapping
    public ResponseEntity<BuoiHocDTO> createBuoiHoc(@RequestBody BuoiHocRequest request) {
        return ResponseEntity.ok(buoiHocService.createBuoiHoc(request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BuoiHocDTO> updateStatus(@PathVariable("id") Integer id, @RequestParam("status") String status) {
        return ResponseEntity.ok(buoiHocService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuoiHoc(@PathVariable("id") Integer id) {
        buoiHocService.deleteBuoiHoc(id);
        return ResponseEntity.noContent().build();
    }
}
