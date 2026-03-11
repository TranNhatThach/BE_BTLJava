package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.HocVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HocVienRepository extends JpaRepository<HocVien, Integer> {
}
