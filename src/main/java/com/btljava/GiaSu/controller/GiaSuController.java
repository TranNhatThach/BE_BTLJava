package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.entity.GiaSuMonHoc;
import com.btljava.GiaSu.service.GiaSuService;
import lombok.RequiredArgsConstructor; // Thêm import này
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gia-su")
@RequiredArgsConstructor // Thay thế cho constructor thủ công
public class GiaSuController {

    private final GiaSuService giaSuService;

    @GetMapping("/search")
    public ResponseEntity<List<GiaSuMonHoc>> searchGiaSu(
            @RequestParam(required = false) String tenMon,
            @RequestParam(required = false) String trinhDo) {

        List<GiaSuMonHoc> ketQua = giaSuService.timKiemGiaSu(tenMon, trinhDo);
        return ResponseEntity.ok(ketQua);
    }
}