package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.BuoiHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuoiHocRepository extends JpaRepository<BuoiHoc, Integer> {
    List<BuoiHoc> findByLopHoc_MaLop(Integer maLop);
}
