package com.btljava.GiaSu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiaSuUngTuyenRequest {
    private Integer maGiaSu;
    private Integer maYeuCau;
    private String loiNhan;
    private Integer mucHocPhiDeXuat;
}
