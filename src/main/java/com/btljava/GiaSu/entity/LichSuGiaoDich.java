package com.btljava.GiaSu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Lich_Su_Giao_Dich")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichSuGiaoDich {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_giao_dich")
    private Integer maGiaoDich;

    @ManyToOne
    @JoinColumn(name = "ma_thanh_toan", referencedColumnName = "ma_thanh_toan")
    private ThanhToan thanhToan;

    @Column(name = "so_tien")
    private Integer soTien;

    @Column(name = "loai", length = 50)
    private String loai;

    @CreationTimestamp
    @Column(name = "ngay_giao_dich", updatable = false)
    private LocalDateTime ngayGiaoDich;
}
