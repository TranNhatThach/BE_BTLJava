package com.btljava.GiaSu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Lop_Hoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LopHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_lop")
    private Integer maLop;

    @ManyToOne
    @JoinColumn(name = "ma_hoc_vien", referencedColumnName = "ma_hoc_vien")
    private HocVien hocVien;

    @ManyToOne
    @JoinColumn(name = "ma_gia_su", referencedColumnName = "ma_gia_su")
    private GiaSu giaSu;

    @ManyToOne
    @JoinColumn(name = "ma_mon", referencedColumnName = "ma_mon")
    private MonHoc monHoc;

    @ManyToOne
    @JoinColumn(name = "ma_yeu_cau", referencedColumnName = "ma_yeu_cau")
    private YeuCauTimGiaSu yeuCauTimGiaSu;

    @Column(name = "hoc_phi_thoa_thuan")
    private Integer hocPhiThoaThuan;

    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    @Column(name = "lich_hoc", columnDefinition = "NVARCHAR(255)")
    private String lichHoc;

    @Column(name = "ghi_chu", columnDefinition = "NVARCHAR(500)")
    private String ghiChu;
}
