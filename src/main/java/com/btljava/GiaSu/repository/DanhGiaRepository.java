package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {
}
