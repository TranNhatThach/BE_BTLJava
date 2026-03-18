package com.btljava.GiaSu.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Tai_Khoan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaiKhoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_tai_khoan")
    private Integer maTaiKhoan;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "mat_khau_ma_hoa", nullable = false, length = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String matKhauMaHoa;

    @Column(name = "so_dien_thoai", length = 20)
    private String soDienThoai;

    @Column(name = "ho_ten", length = 100)
    private String hoTen;

    @Column(name = "vai_tro", length = 20)
    private String vaiTro; // HOC_VIEN, GIA_SU

    @Column(name = "trang_thai", length = 20)
    private String trangThai; // HOAT_DONG, KHOA

    @CreationTimestamp
    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "vi_tri",length = 225)
    private String viTri;
}
