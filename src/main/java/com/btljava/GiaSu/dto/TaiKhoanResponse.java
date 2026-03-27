package com.btljava.GiaSu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoanResponse {
    private Integer maTaiKhoan;
    private String email;
    private String hoTen;
    private String soDienThoai;
    private String vaiTro;
    private String viTri;
    // gia su
    private String gioiTinh;
    private String truongDaiHoc;
    private String chuyenNganh;
    private Integer namSinh;
    private Integer soNamKinhNghiem;
    private String moTa;
    // hoc vien
    private String lopHoc;
    private String truongHoc;
    private String hinhThucHocUuTien;

}
