package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.LopHoc;
import com.btljava.GiaSu.entity.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Integer> {

    List<ThanhToan> findByMaLop_MaLop(Integer maLop);
    List<ThanhToan> findByTrangThai(String trangThai);
    List<ThanhToan> findByMaLop(LopHoc maLop);
}