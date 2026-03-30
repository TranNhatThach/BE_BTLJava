package com.btljava.GiaSu.service;

import com.btljava.GiaSu.entity.ThanhToan;
import com.btljava.GiaSu.repository.ThanhToanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // Tự động inject ThanhToanRepository
public class ThanhToanService {

    private final ThanhToanRepository thanhToanRepository;

    // Sử dụng findById (Sẵn có của JpaRepository)
    public ThanhToan layTheoId(Integer id) {
        return thanhToanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giao dịch: " + id));
    }

    // Sử dụng findByMaLop_MaLop (Giải quyết cảnh báo 'never used')
    public List<ThanhToan> layLichSuThanhToanLop(Integer maLop) {
        return thanhToanRepository.findByMaLop_MaLop(maLop);
    }

    // Sử dụng findByTrangThai (Giải quyết cảnh báo 'never used')
    public List<ThanhToan> layDanhSachTheoTrangThai(String trangThai) {
        return thanhToanRepository.findByTrangThai(trangThai);
    }
}