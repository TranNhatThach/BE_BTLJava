package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {
    List<DanhGia> findByLopHoc_MaLop(Integer maLop);
    List<DanhGia> findByLopHoc_GiaSu_MaGiaSu(Integer maGiaSu);
}
