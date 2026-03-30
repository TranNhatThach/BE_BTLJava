package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.LichSuGiaoDich;
import com.btljava.GiaSu.entity.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LichSuGiaoDichRepository extends JpaRepository<LichSuGiaoDich, Integer> {
    List<LichSuGiaoDich> findByThanhToan(ThanhToan thanhToan);
}
