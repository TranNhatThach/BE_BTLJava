package com.btljava.GiaSu.service;

import com.btljava.GiaSu.entity.TaiKhoan;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date; // Dùng java.util.Date
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    // Chuỗi này phải là Base64 và đủ dài (ít nhất 32 ký tự sau khi giải mã)
    private static final String SECRET_KEY = "ZGF5X2xhX2NodW9pX2JpX21hdF9yYXRfZGFpX3ZhX2Jha19tYXRfY3VhX2Jhbl9wcm9qZWN0X2dpYXN1";

    public String generateToken(TaiKhoan taiKhoan) {
        Map<String, Object> extraClaims = new HashMap<>();
        // Nhét vaiTro vào Token để Frontend không cần gọi API lần 2 vẫn biết user là ai
        extraClaims.put("userId", taiKhoan.getMaTaiKhoan());
        extraClaims.put("role", taiKhoan.getVaiTro());
        extraClaims.put("hoTen", taiKhoan.getHoTen());

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(taiKhoan.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 giờ
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // lấy id
    public Integer extractUserId(String token) {
        return extractAllClaims(token).get("userId", Integer.class);
    }

    // kiểm tra xem token hết hạn ch
    public boolean isTokenValid(String token) {
        return !extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}