package com.btljava.GiaSu.service;

import com.btljava.GiaSu.dto.TaiKhoanResponse;
import com.btljava.GiaSu.entity.*;
import com.btljava.GiaSu.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaiKhoanService {

    private final TaiKhoanRepository taiKhoanRepository;
    private final GiaSuRepository giaSuRepository;
    private final HocVienRepository hocVienRepository;
    private final DanhGiaRepository danhGiaRepository;
    private final ThongBaoRepository thongBaoRepository;
    private final YeuCauTimGiaSuRepository yeuCauRepository;
    private final UngTuyenRepository ungTuyenRepository;
    private final LopHocRepository lopHocRepository;
    private final BuoiHocRepository buoiHocRepository;
    private final ThanhToanRepository thanhToanRepository;
    private final LichSuGiaoDichRepository lichSuGiaoDichRepository;
    private final GiaSuMonHocRepository giaSuMonHocRepository;

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
                // Lấy đánh giá của gia sư hiện tại
                java.util.List<DanhGia> danhGias = danhGiaRepository.findByLopHoc_GiaSu_MaGiaSu(gs.getMaGiaSu());
                Double avgRating = danhGias.stream()
                        .mapToInt(DanhGia::getDiem)
                        .average()
                        .orElse(0.0);
                avgRating = Math.round(avgRating * 10) / 10.0;

                builder.gioiTinh(gs.getGioiTinh())
                        .truongDaiHoc(gs.getTruongDaiHoc())
                        .chuyenNganh(gs.getChuyenNganh())
                        .namSinh(gs.getNamSinh())
                        .soNamKinhNghiem(gs.getSoNamKinhNghiem())
                        .diemDanhGia(avgRating)
                        .soDanhGia(danhGias.size())
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
                        .hinhThucHocUuTien(hv.getHinhThucHocUuTien())
                        .moTa(hv.getGhiChu());
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
            if (updated.getMoTa() != null)
                hv.setGhiChu(updated.getMoTa());
            hocVienRepository.save(hv);
        }

        return getById(id);
    }

    public List<TaiKhoan> getAllTaiKhoan() {
        return taiKhoanRepository.findByIsDelete(0);
    }

    public List<TaiKhoan> getDeletedTaiKhoan() {
        return taiKhoanRepository.findByIsDelete(1);
    }

    public void deleteTaiKhoan(Integer id) {
        TaiKhoan tk = taiKhoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại với id: " + id));
        tk.setIsDelete(1);
        taiKhoanRepository.save(tk);
    }

    public void restoreTaiKhoan(Integer id) {
        TaiKhoan tk = taiKhoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại với id: " + id));
        tk.setIsDelete(0);
        taiKhoanRepository.save(tk);
    }

    public void forceDeleteTaiKhoan(Integer id) {
        TaiKhoan tk = taiKhoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại với id: " + id));

        // Xóa thông báo
        thongBaoRepository.deleteAll(thongBaoRepository.findByTaiKhoan(tk));

        if ("HOC_VIEN".equals(tk.getVaiTro())) {
            HocVien hv = hocVienRepository.findByTaiKhoan(tk);
            if (hv != null) {
                List<LopHoc> lopHocList = lopHocRepository.findByHocVien(hv);
                for (LopHoc lop : lopHocList) {
                    danhGiaRepository.deleteAll(danhGiaRepository.findByLopHoc(lop));
                    buoiHocRepository.deleteAll(buoiHocRepository.findByLopHoc(lop));
                    List<ThanhToan> ttList = thanhToanRepository.findByMaLop(lop);
                    for (ThanhToan tt : ttList) {
                        lichSuGiaoDichRepository.deleteAll(lichSuGiaoDichRepository.findByThanhToan(tt));
                    }
                    thanhToanRepository.deleteAll(ttList);
                }
                lopHocRepository.deleteAll(lopHocList);


                List<YeuCauTimGiaSu> yeuCauList = yeuCauRepository.findByHocVien(hv);
                for (YeuCauTimGiaSu yc : yeuCauList) {
                    ungTuyenRepository.deleteAll(ungTuyenRepository.findByYeuCauTimGiaSu(yc));
                }
                yeuCauRepository.deleteAll(yeuCauList);


                hocVienRepository.delete(hv);
            }
        } else if ("GIA_SU".equals(tk.getVaiTro())) {
            GiaSu gs = giaSuRepository.findByTaiKhoan(tk);
            if (gs != null) {

                giaSuMonHocRepository.deleteAll(giaSuMonHocRepository.findByGiaSu(gs));


                ungTuyenRepository.deleteAll(ungTuyenRepository.findByGiaSu(gs));


                List<LopHoc> lopHocList = lopHocRepository.findByGiaSu(gs);

                for (LopHoc lop : lopHocList) {
                    danhGiaRepository.deleteAll(danhGiaRepository.findByLopHoc(lop));
                    buoiHocRepository.deleteAll(buoiHocRepository.findByLopHoc(lop));
                    List<ThanhToan> ttList = thanhToanRepository.findByMaLop(lop);
                    for (ThanhToan tt : ttList) {
                        lichSuGiaoDichRepository.deleteAll(lichSuGiaoDichRepository.findByThanhToan(tt));
                    }
                    thanhToanRepository.deleteAll(ttList);
                }
                lopHocRepository.deleteAll(lopHocList);

                giaSuRepository.delete(gs);
            }
        }

        taiKhoanRepository.delete(tk);
    }

    public List<TaiKhoan> getAllGiaSu(){
        return taiKhoanRepository.findByVaiTroAndIsDelete("GIA_SU", 0);
    }

    public List<TaiKhoan> getAllHocVien() {
        return taiKhoanRepository.findByVaiTroAndIsDelete("HOC_VIEN", 0);
    }


}