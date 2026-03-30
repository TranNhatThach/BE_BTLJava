package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.GiaSu;
import com.btljava.GiaSu.entity.UngTuyen;
import com.btljava.GiaSu.entity.UngTuyenId;
import com.btljava.GiaSu.entity.YeuCauTimGiaSu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UngTuyenRepository extends JpaRepository<UngTuyen, UngTuyenId> {
    List<UngTuyen> findByGiaSu_MaGiaSuAndTrangThai(Integer maGiaSu, String trangThai);
    List<UngTuyen> findByGiaSu_MaGiaSu(Integer maGiaSu);
    List<UngTuyen> findByYeuCauTimGiaSu_MaYeuCauAndTrangThai(Integer maYeuCau, String trangThai);
    List<UngTuyen> findByYeuCauTimGiaSu_MaYeuCau(Integer maYeuCau);
    List<UngTuyen> findByYeuCauTimGiaSu_HocVien_MaHocVien(Integer maHocVien);
    List<UngTuyen> findByYeuCauTimGiaSu(YeuCauTimGiaSu yeuCau);
    List<UngTuyen> findByGiaSu(GiaSu giaSu);
}
