package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.dto.GiaSuMonHocRequest;
import com.btljava.GiaSu.dto.GiaSuMonHocResponse;
import com.btljava.GiaSu.service.GiaSuMonHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gia-su/{maGiaSu}/mon-hoc")
@RequiredArgsConstructor
@CrossOrigin("*")
public class GiaSuMonHocController {

    private final GiaSuMonHocService giaSuMonHocService;

    @GetMapping
    public ResponseEntity<List<GiaSuMonHocResponse>> getMonHocByGiaSu(
            @PathVariable("maGiaSu") Integer maGiaSu) {
        return ResponseEntity.ok(giaSuMonHocService.getMonHocByGiaSu(maGiaSu));
    }

    @PostMapping
    public ResponseEntity<GiaSuMonHocResponse> addMonHocToGiaSu(
            @PathVariable("maGiaSu") Integer maGiaSu,
            @RequestBody GiaSuMonHocRequest request) {
        return ResponseEntity.ok(giaSuMonHocService.addMonHocToGiaSu(maGiaSu, request));
    }

    @PutMapping("/{maMon}")
    public ResponseEntity<GiaSuMonHocResponse> updateMonHocGiaSu(
            @PathVariable("maGiaSu") Integer maGiaSu,
            @PathVariable("maMon") Integer maMon,
            @RequestBody GiaSuMonHocRequest request) {
        return ResponseEntity.ok(giaSuMonHocService.updateMonHocGiaSu(maGiaSu, maMon, request));
    }

    @DeleteMapping("/{maMon}")
    public ResponseEntity<Void> removeMonHocGiaSu(
            @PathVariable("maGiaSu") Integer maGiaSu,
            @PathVariable("maMon") Integer maMon) {
        giaSuMonHocService.removeMonHocGiaSu(maGiaSu, maMon);
        return ResponseEntity.noContent().build();
    }
}
