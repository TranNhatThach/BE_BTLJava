package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.UngTuyen;
import com.btljava.GiaSu.entity.UngTuyenId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UngTuyenRepository extends JpaRepository<UngTuyen, UngTuyenId> {
}
