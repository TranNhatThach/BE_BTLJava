package com.btljava.GiaSu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiaSuMonHocRequest {
    private Integer maMon;
    private String trinhDo;
    private Integer hocPhiMoiGio;
}
