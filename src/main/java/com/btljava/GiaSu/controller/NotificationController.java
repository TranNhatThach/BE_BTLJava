package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.entity.ThongBao;
import com.btljava.GiaSu.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin("*")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{maTaiKhoan}")
    public ResponseEntity<List<ThongBao>> getMyNotifications(@PathVariable("maTaiKhoan") Integer maTaiKhoan) {
        return ResponseEntity.ok(notificationService.layThongBaoCuaToi(maTaiKhoan));
    }

    @PutMapping("/{maThongBao}/read")
    public ResponseEntity<String> markAsRead(@PathVariable("maThongBao") Integer maThongBao) {
        notificationService.danhDauDaDoc(maThongBao);
        return ResponseEntity.ok("Success");
    }
}
