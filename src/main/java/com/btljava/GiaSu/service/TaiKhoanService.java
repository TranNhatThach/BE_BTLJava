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

            TaiKhoanResponse.TaiKhoanResponseBuilder builder = TaiKhoanResponse.builder()
                    .maTaiKhoan(tk.getMaTaiKhoan())
                    .email(tk.getEmail())
                    .hoTen(tk.getHoTen())
                    .soDienThoai(tk.getSoDienThoai())
                    .vaiTro(tk.getVaiTro())
                    .viTri(tk.getViTri());

            if (gs != null) {
                builder.gioiTinh(gs.getGioiTinh())
                        .truongDaiHoc(gs.getTruongDaiHoc())
                        .chuyenNganh(gs.getChuyenNganh())
                        .namSinh(gs.getNamSinh())
                        .soNamKinhNghiem(gs.getSoNamKinhNghiem())
                        .moTa(gs.getMoTa());
            }

            return builder.build();
        }
        if ("HOC_VIEN".equals(tk.getVaiTro())) {
            HocVien hv = hocVienRepository.findByTaiKhoan(tk);

            TaiKhoanResponse.TaiKhoanResponseBuilder builder = TaiKhoanResponse.builder()
                    .maTaiKhoan(tk.getMaTaiKhoan())
                    .email(tk.getEmail())
                    .hoTen(tk.getHoTen())
                    .soDienThoai(tk.getSoDienThoai())
                    .vaiTro(tk.getVaiTro())
                    .viTri(tk.getViTri());

            if (hv != null) {
                builder.lopHoc(hv.getLopHoc())
                        .truongHoc(hv.getTruongHoc())
                        .hinhThucHocUuTien(hv.getHinhThucHocUuTien());
            }

            return builder.build();
        }
        return null;
    }

    public TaiKhoanResponse updateTaiKhoan(Integer id, TaiKhoanResponse updated) {
        TaiKhoan tk = taiKhoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email: " + id));

        if (updated.getHoTen() != null) {
            tk.setHoTen(updated.getHoTen());
        }
        if (updated.getSoDienThoai() != null) {
            tk.setSoDienThoai(updated.getSoDienThoai());
        }
        if (updated.getViTri() != null) {
            tk.setViTri(updated.getViTri());
        }
        taiKhoanRepository.save(tk);

        if ("GIA_SU".equals(tk.getVaiTro())) {
            GiaSu gs = giaSuRepository.findByTaiKhoan(tk);
            if (gs == null) {
                gs = new GiaSu();
                gs.setTaiKhoan(tk);
            }
            if (updated.getGioiTinh() != null)
                gs.setGioiTinh(updated.getGioiTinh());
            if (updated.getTruongDaiHoc() != null)
                gs.setTruongDaiHoc(updated.getTruongDaiHoc());
            if (updated.getChuyenNganh() != null)
                gs.setChuyenNganh(updated.getChuyenNganh());
            if (updated.getNamSinh() != null)
                gs.setNamSinh(updated.getNamSinh());
            if (updated.getSoNamKinhNghiem() != null)
                gs.setSoNamKinhNghiem(updated.getSoNamKinhNghiem());
            if (updated.getMoTa() != null)
                gs.setMoTa(updated.getMoTa());
            giaSuRepository.save(gs);
        } else if ("HOC_VIEN".equals(tk.getVaiTro())) {
            HocVien hv = hocVienRepository.findByTaiKhoan(tk);
            if (hv == null) {
                hv = new HocVien();
                hv.setTaiKhoan(tk);
            }
            if (updated.getLopHoc() != null)
                hv.setLopHoc(updated.getLopHoc());
            if (updated.getTruongHoc() != null)
                hv.setTruongHoc(updated.getTruongHoc());
            if (updated.getHinhThucHocUuTien() != null)
                hv.setHinhThucHocUuTien(updated.getHinhThucHocUuTien());
            hocVienRepository.save(hv);
        }

        return getById(id);
    }
}