package com.btljava.GiaSu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Thong_Bao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThongBao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_thong_bao")
    private Integer maThongBao;

    @ManyToOne
    @JoinColumn(name = "ma_tai_khoan", nullable = false)
    private TaiKhoan taiKhoan;

    @Column(name = "noi_dung", nullable = false, length = 500)
    private String noiDung;

    @Column(name = "loai", length = 50)
    private String loai; // TUYEN_DUNG, LOP_HOC, HE_THONG

    @Column(name = "da_doc")
    @Builder.Default
    private Boolean daDoc = false;

    @CreationTimestamp
    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;
}
