package com.btljava.GiaSu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Yeu_Cau_Tim_Gia_Su")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YeuCauTimGiaSu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_yeu_cau")
    private Integer maYeuCau;

    @ManyToOne
    @JoinColumn(name = "ma_mon", referencedColumnName = "ma_mon")
    private MonHoc monHoc;

    @ManyToOne
    @JoinColumn(name = "ma_hoc_vien", referencedColumnName = "ma_hoc_vien")
    private HocVien hocVien;

    @Column(name = "trinh_do", length = 50)
    private String trinhDo;

    @Column(name = "lich_hoc_du_kien", length = 255)
    private String lichHocDuKien;

    @Column(name = "hinh_thuc", length = 50)
    private String hinhThuc;

    @Column(name = "dia_diem", length = 255)
    private String diaDiem;

    @Column(name = "ngan_sach_min")
    private Integer nganSachMin;

    @Column(name = "ngan_sach_max")
    private Integer nganSachMax;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    @CreationTimestamp
    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;
}
