package com.btljava.GiaSu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Buoi_Hoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuoiHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_buoi")
    private Integer maBuoi;

    @ManyToOne
    @JoinColumn(name = "ma_lop", referencedColumnName = "ma_lop")
    private LopHoc lopHoc;

    @Column(name = "thoi_gian_bat_dau")
    private LocalDateTime thoiGianBatDau;

    @Column(name = "thoi_gian_ket_thuc")
    private LocalDateTime thoiGianKetThuc;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;
}
