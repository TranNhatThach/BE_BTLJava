package com.btljava.GiaSu.service;

import com.btljava.GiaSu.dto.TaiKhoanResponse;
import com.btljava.GiaSu.entity.GiaSu;
import com.btljava.GiaSu.entity.HocVien;
import com.btljava.GiaSu.entity.TaiKhoan;
import com.btljava.GiaSu.repository.GiaSuRepository;
import com.btljava.GiaSu.repository.HocVienRepository;
import com.btljava.GiaSu.repository.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaiKhoanService {

    private final TaiKhoanRepository taiKhoanRepository;
    private final GiaSuRepository giaSuRepository;
    private final HocVienRepository hocVienRepository;

    public TaiKhoanResponse getById(Integer id) {
        TaiKhoan tk = taiKhoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));
        if ("GIA_SU".equals(tk.getVaiTro())) {
            GiaSu gs = giaSuRepository.findByTaiKhoan(tk);

            return TaiKhoanResponse.builder()
                    .maTaiKhoan(tk.getMaTaiKhoan())
                    .email(tk.getEmail())
                    .hoTen(tk.getHoTen())
                    .soDienThoai(tk.getSoDienThoai())
                    .vaiTro(tk.getVaiTro())
                    .viTri(tk.getViTri())

                    .gioiTinh(gs.getGioiTinh())
                    .truongDaiHoc(gs.getTruongDaiHoc())
                    .chuyenNganh(gs.getChuyenNganh())
                    .namSinh(gs.getNamSinh())
                    .soNamKinhNghiem(gs.getSoNamKinhNghiem())
                    .build();
        }
        if ("HOC_VIEN".equals(tk.getVaiTro())) {
            HocVien hv = hocVienRepository.findByTaiKhoan(tk);

            return TaiKhoanResponse.builder()
                    .maTaiKhoan(tk.getMaTaiKhoan())
                    .email(tk.getEmail())
                    .hoTen(tk.getHoTen())
                    .soDienThoai(tk.getSoDienThoai())
                    .vaiTro(tk.getVaiTro())
                    .viTri(tk.getViTri())

                    .lopHoc(hv.getLopHoc())
                    .truongHoc(hv.getTruongHoc())
                    .hinhThucHocUuTien(hv.getHinhThucHocUuTien())
                    .build();
        }
        return null;
    }

    public TaiKhoan updateTaiKhoan(String email, TaiKhoan updated) {
        TaiKhoan tk = taiKhoanRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email: " + email));

        if (updated.getHoTen() != null) {
            tk.setHoTen(updated.getHoTen());
        }
        if (updated.getSoDienThoai() != null) {
            tk.setSoDienThoai(updated.getSoDienThoai());
        }
        if (updated.getViTri() != null) {
            tk.setViTri(updated.getViTri());
        }

        return taiKhoanRepository.save(tk);
    }
}