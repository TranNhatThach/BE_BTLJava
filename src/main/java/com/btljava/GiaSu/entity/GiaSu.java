package com.btljava.GiaSu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Gia_Su")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiaSu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_gia_su")
    private Integer maGiaSu;

    @OneToOne
    @JoinColumn(name = "ma_tai_khoan", referencedColumnName = "ma_tai_khoan", unique = true)
    private TaiKhoan taiKhoan;

    @Column(name = "gioi_tinh", length = 10)
    private String gioiTinh;

    @Column(name = "truong_dai_hoc", length = 100)
    private String truongDaiHoc;

    @Column(name = "chuyen_nganh", length = 100)
    private String chuyenNganh;

    @Column(name = "nam_sinh")
    private Integer namSinh;
    

    @Column(name = "so_nam_kinh_nghiem")
    private Integer soNamKinhNghiem;
}
