package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.UngTuyen;
import com.btljava.GiaSu.entity.UngTuyenId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UngTuyenRepository extends JpaRepository<UngTuyen, UngTuyenId> {
    List<UngTuyen> findByGiaSu_MaGiaSuAndTrangThai(Integer maGiaSu, String trangThai);
    List<UngTuyen> findByGiaSu_MaGiaSu(Integer maGiaSu);
    List<UngTuyen> findByYeuCauTimGiaSu_MaYeuCauAndTrangThai(Integer maYeuCau, String trangThai);
}
