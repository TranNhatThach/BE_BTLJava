package com.btljava.GiaSu.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiaSuResponse {
    private String username;
    private String trinhDo;
    private String monHoc;
    private String viTri;
    private String gioitinh;
    private Double hocPhiMoiGio;
}
