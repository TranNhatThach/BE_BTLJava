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

    public ThanhToan layTheoId(Integer id) {
        return thanhToanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giao dịch: " + id));
    }

    public List<ThanhToan> layLichSuThanhToanLop(Integer maLop) {
        return thanhToanRepository.findByMaLop_MaLop(maLop);
    }

    public List<ThanhToan> layDanhSachTheoTrangThai(String trangThai) {
        return thanhToanRepository.findByTrangThai(trangThai);
    }
}