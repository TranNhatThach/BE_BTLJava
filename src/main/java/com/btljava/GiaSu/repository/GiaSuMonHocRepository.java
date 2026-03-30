package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.GiaSuMonHoc;
import com.btljava.GiaSu.entity.GiaSuMonHocId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface GiaSuMonHocRepository extends JpaRepository<GiaSuMonHoc, GiaSuMonHocId> {
        List<GiaSuMonHoc> findByGiaSu_MaGiaSu(Integer maGiaSu);

        @Query("SELECT gsmh FROM GiaSuMonHoc gsmh " +
                        "WHERE (:tenMon IS NULL OR gsmh.monHoc.tenMon LIKE :tenMon) " +
                        "AND (:maGiaSu IS NULL OR gsmh.giaSu.maGiaSu = :maGiaSu) " +
                        "AND (:trinhDo IS NULL OR gsmh.trinhDo LIKE :trinhDo) " +
                        "AND (:viTri IS NULL OR gsmh.giaSu.taiKhoan.viTri LIKE :viTri) " +
                        "AND (:minGia IS NULL OR gsmh.hocPhiMoiGio >= :minGia) " +
                        "AND (:maxGia IS NULL OR gsmh.hocPhiMoiGio <= :maxGia)")
        Page<GiaSuMonHoc> findByFilter(
                        @Param("tenMon") String tenMon,
                        @Param("trinhDo") String trinhDo,
                        @Param("viTri") String viTri,
                        @Param("minGia") Double minGia,
                        @Param("maxGia") Double maxGia,
                        @Param("maGiaSu") Integer maGiaSu,
                        Pageable pageable);
}
