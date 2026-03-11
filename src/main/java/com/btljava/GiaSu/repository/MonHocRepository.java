package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.MonHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonHocRepository extends JpaRepository<MonHoc, Integer> {
}
