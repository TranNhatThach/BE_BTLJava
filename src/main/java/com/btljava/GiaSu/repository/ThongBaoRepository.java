package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Integer> {
    List<ThongBao> findByTaiKhoan_MaTaiKhoanOrderByNgayTaoDesc(Integer maTaiKhoan);
    long countByTaiKhoan_MaTaiKhoanAndDaDocFalse(Integer maTaiKhoan);
}
