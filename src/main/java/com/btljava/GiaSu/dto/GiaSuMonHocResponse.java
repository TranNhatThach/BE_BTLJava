package com.btljava.GiaSu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiaSuMonHocResponse {
    private Integer maMon;
    private String tenMon;
    private String moTa;
    private String trinhDo;
    private Integer hocPhiMoiGio;
}
