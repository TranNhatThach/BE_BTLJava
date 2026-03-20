package com.btljava.GiaSu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DanhGiaDTO {
    private Integer maDanhGia;
    private Integer maLop;
    private Integer diem;
    private String nhanXet;
    private LocalDateTime thoiGianDanhGia;
}
