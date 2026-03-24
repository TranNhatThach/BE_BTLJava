package com.btljava.GiaSu.dto;

import lombok.Data;

@Data
public class TuyenDungTrucTiepRequest {
    private Integer maGiaSu;
    private Integer maHocVien; 
    private String tenMon;
    private String trinhDo;
    private String lichHocDuKien;
    private String hinhThuc;
    private String diaDiem;
    private Integer nganSachMin;
    private Integer nganSachMax;
    private String moTa;
    private String loiNhan; // A message to the tutor from the student
}
