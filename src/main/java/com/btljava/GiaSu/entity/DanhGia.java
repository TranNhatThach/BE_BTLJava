package com.btljava.GiaSu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Danh_Gia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DanhGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_danh_gia")
    private Integer maDanhGia;

    @ManyToOne
    @JoinColumn(name = "ma_lop", referencedColumnName = "ma_lop")
    private LopHoc lopHoc;

    @Column(name = "diem")
    private Integer diem;

    @Column(name = "nhan_xet", length = 255)
    private String nhanXet;

    @CreationTimestamp
    @Column(name = "thoi_gian_danh_gia", updatable = false)
    private LocalDateTime thoiGianDanhGia;
}
