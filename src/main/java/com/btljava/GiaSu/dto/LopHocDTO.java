package com.btljava.GiaSu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LopHocDTO {
    private Integer maLop;
    private Integer maHocVien;
    private Integer maGiaSu;
    private Integer maMon;
    private Integer maYeuCau;
    private Integer hocPhiThoaThuan;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String trangThai;
    private String tenHocVien;
    private String tenGiaSu;
    private String tenMonHoc;
    private String lichHoc;
    private String ghiChu;
}
