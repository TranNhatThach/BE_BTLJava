package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.dto.GiaSuResponse;
import com.btljava.GiaSu.service.GiaSuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gia-su")
@RequiredArgsConstructor
public class LocGiaSuController {

    private final GiaSuService giaSuService;

    @GetMapping("/search")
    public ResponseEntity<List<GiaSuResponse>> searchGiaSu(
            @RequestParam(required = false) String usename,
            @RequestParam(required = false) String tenMon,
            @RequestParam(required = false) String trinhDo,
            @RequestParam(required = false) String viTri,
            @RequestParam(required = false) Double minGia,
            @RequestParam(required = false) Double maxGia
    ) {
        List<GiaSuResponse> ketQua = giaSuService.timKiemGiaSu(
                tenMon, trinhDo, viTri, minGia, maxGia
        );

        return ResponseEntity.ok(ketQua);
    }
}