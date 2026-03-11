package com.btljava.GiaSu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Ung_Tuyen")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UngTuyen {

    @EmbeddedId
    private UngTuyenId id;

    @ManyToOne
    @MapsId("maGiaSu")
    @JoinColumn(name = "ma_gia_su")
    private GiaSu giaSu;

    @ManyToOne
    @MapsId("maYeuCau")
    @JoinColumn(name = "ma_yeu_cau")
    private YeuCauTimGiaSu yeuCauTimGiaSu;

    @Column(name = "loi_nhan", length = 255)
    private String loiNhan;

    @Column(name = "muc_hoc_phi_de_xuat")
    private Integer mucHocPhiDeXuat;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    @CreationTimestamp
    @Column(name = "ngay_ung_tuyen", updatable = false)
    private LocalDateTime ngayUngTuyen;
}
