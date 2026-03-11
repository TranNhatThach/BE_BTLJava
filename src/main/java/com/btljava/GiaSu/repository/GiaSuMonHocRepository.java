package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.GiaSuMonHoc;
import com.btljava.GiaSu.entity.GiaSuMonHocId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiaSuMonHocRepository extends JpaRepository<GiaSuMonHoc, GiaSuMonHocId> {
}
