package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.entity.ThongBao;
import com.btljava.GiaSu.service.JwtService;
import com.btljava.GiaSu.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtService jwtService;

    @GetMapping("/my")
    public ResponseEntity<List<ThongBao>> getMyNotifications(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            String token = authHeader.substring(7);
            Integer maTaiKhoan = jwtService.extractUserId(token);
            return ResponseEntity.ok(notificationService.layThongBaoCuaToi(maTaiKhoan));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/{maThongBao}/read")
    public ResponseEntity<String> markAsRead(@PathVariable("maThongBao") Integer maThongBao) {
        notificationService.danhDauDaDoc(maThongBao);
        return ResponseEntity.ok("Success");
    }
}
