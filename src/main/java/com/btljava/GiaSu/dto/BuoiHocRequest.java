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
public class BuoiHocRequest {
    private Integer maLop;
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;
}
