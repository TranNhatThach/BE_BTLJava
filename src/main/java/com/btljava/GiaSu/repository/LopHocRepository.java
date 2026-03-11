package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.LopHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LopHocRepository extends JpaRepository<LopHoc, Integer> {
}
