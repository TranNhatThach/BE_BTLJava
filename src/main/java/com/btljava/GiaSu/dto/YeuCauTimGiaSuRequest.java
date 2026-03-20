package com.btljava.GiaSu.dto;

import lombok.Data;

@Data
public class YeuCauTimGiaSuRequest {
    private Integer maMon;
    private Integer maHocVien;
    private String trinhDo;
    private String lichHocDuKien;
    private String hinhThuc;
    private String diaDiem;
    private Integer nganSachMin;
    private Integer nganSachMax;
    private String moTa;
}
