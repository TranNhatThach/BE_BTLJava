package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.HocVien;
import com.btljava.GiaSu.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HocVienRepository extends JpaRepository<HocVien, Integer> {
    HocVien findByTaiKhoan(TaiKhoan taiKhoan);
}
