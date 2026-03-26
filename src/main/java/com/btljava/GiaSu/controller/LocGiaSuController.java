package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.dto.GiaSuResponse;
import com.btljava.GiaSu.service.GiaSuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/gia-su")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LocGiaSuController {

    private final GiaSuService giaSuService;

    @GetMapping("/search")
    public ResponseEntity<Page<GiaSuResponse>> searchGiaSu(
            @RequestParam(required = false) Integer maGiaSu,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String tenMon,
            @RequestParam(required = false) String trinhDo,
            @RequestParam(required = false) String viTri,
            @RequestParam(required = false) Double minGia,
            @RequestParam(required = false) Double maxGia,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<GiaSuResponse> ketQua = giaSuService.timKiemGiaSu(
                tenMon, trinhDo, viTri, minGia, maxGia, maGiaSu, page, size
        );

        return ResponseEntity.ok(ketQua);
    }

    @GetMapping("/{maGiaSu}")
    public ResponseEntity<GiaSuResponse> getGiaSuById(@PathVariable("maGiaSu") Integer maGiaSu) {
        Page<GiaSuResponse> ketQua = giaSuService.timKiemGiaSu(
                null, null, null, null, null, maGiaSu, 0, 1
        );
        
        if (ketQua.hasContent()) {
            return ResponseEntity.ok(ketQua.getContent().get(0));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}