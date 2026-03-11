package com.btljava.GiaSu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Gia_Su_Mon_Hoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiaSuMonHoc {

    @EmbeddedId
    private GiaSuMonHocId id;

    @ManyToOne
    @MapsId("maGiaSu")
    @JoinColumn(name = "ma_gia_su")
    private GiaSu giaSu;

    @ManyToOne
    @MapsId("maMon")
    @JoinColumn(name = "ma_mon")
    private MonHoc monHoc;

    @Column(name = "trinh_do", length = 50)
    private String trinhDo;

    @Column(name = "hoc_phi_moi_gio")
    private Integer hocPhiMoiGio;
}
