package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.GiaSu;
import com.btljava.GiaSu.entity.HocVien;
import com.btljava.GiaSu.entity.LopHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LopHocRepository extends JpaRepository<LopHoc, Integer> {
    List<LopHoc> findByHocVien_MaHocVien(Integer maHocVien);
    List<LopHoc> findByGiaSu_MaGiaSu(Integer maGiaSu);
    List<LopHoc> findByHocVien(HocVien hocVien);
    List<LopHoc> findByGiaSu(GiaSu giaSu);
}
