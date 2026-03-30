package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.YeuCauTimGiaSu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YeuCauTimGiaSuRepository extends JpaRepository<YeuCauTimGiaSu, Integer> {
    List<YeuCauTimGiaSu> findByTrangThaiIn(List<String> trangThaiList);
    List<YeuCauTimGiaSu> findByHocVien_MaHocVien(Integer maHocVien);
}
