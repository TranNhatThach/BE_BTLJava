package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.LichSuGiaoDich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LichSuGiaoDichRepository extends JpaRepository<LichSuGiaoDich, Integer> {
}
