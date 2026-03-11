package com.btljava.GiaSu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UngTuyenId implements Serializable {

    @Column(name = "ma_gia_su")
    private Integer maGiaSu;

    @Column(name = "ma_yeu_cau")
    private Integer maYeuCau;
}
