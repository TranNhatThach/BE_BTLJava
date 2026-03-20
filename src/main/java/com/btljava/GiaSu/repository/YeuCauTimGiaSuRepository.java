package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.YeuCauTimGiaSu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YeuCauTimGiaSuRepository extends JpaRepository<YeuCauTimGiaSu, Integer> {

}
