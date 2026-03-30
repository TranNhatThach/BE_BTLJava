package com.btljava.GiaSu.dto;

import lombok.Data;

@Data // Tự động tạo Getter/Setter từ Lombok
public class ThanhToanRequest {
    private Integer maLop;
    private Integer soTien;
    private String phuongThuc;
}