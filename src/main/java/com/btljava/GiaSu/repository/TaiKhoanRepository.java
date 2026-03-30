package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {

    List<TaiKhoan> findByVaiTro(String vaiTro);

    Optional<TaiKhoan> findByEmail(String email);

    boolean existsByEmail(String email);
}
