package com.btljava.GiaSu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Hoc_Vien")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HocVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_hoc_vien")
    private Integer maHocVien;

    @OneToOne
    @JoinColumn(name = "ma_tai_khoan", referencedColumnName = "ma_tai_khoan", unique = true)
    private TaiKhoan taiKhoan;

    @Column(name = "lop_hoc", length = 50)
    private String lopHoc;

    @Column(name = "truong_hoc", length = 100)
    private String truongHoc;

    @Column(name = "vi_do")
    private Double viDo;

    @Column(name = "kinh_do")
    private Double kinhDo;

    @Column(name = "hinh_thuc_hoc_uu_tien", length = 50)
    private String hinhThucHocUuTien;

    @Column(name = "ghi_chu", length = 255)
    private String ghiChu;
}
