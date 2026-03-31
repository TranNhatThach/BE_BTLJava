package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.GiaSu;
import com.btljava.GiaSu.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface GiaSuRepository extends JpaRepository<GiaSu, Integer> {
        @Override
        <S extends GiaSu> List<S> saveAllAndFlush(Iterable<S> entities);

        GiaSu findByTaiKhoan(TaiKhoan taiKhoan);

        java.util.Optional<GiaSu> findByTaiKhoan_MaTaiKhoan(Integer maTaiKhoan);

        @Query("SELECT DISTINCT g FROM GiaSu g " +
                        "LEFT JOIN GiaSuMonHoc mh ON mh.giaSu = g " +
                        "LEFT JOIN mh.monHoc m " +
                        // "WHERE (g.truongDaiHoc IS NOT NULL AND g.truongDaiHoc != '' OR g.moTa IS NOT
                        // NULL AND g.moTa != '') " +
                        // "AND (:tenMon IS NULL OR " +
                        "WHERE (:tenMon IS NULL OR " +
                        "    m.tenMon LIKE :tenMon OR " +
                        "    g.taiKhoan.hoTen LIKE :tenMon " +
                        ") " +
                        "AND (:maGiaSu IS NULL OR g.maGiaSu = :maGiaSu) " +
                        "AND (:trinhDo IS NULL OR mh.trinhDo LIKE :trinhDo) " +
                        "AND (:viTri IS NULL OR g.taiKhoan.viTri LIKE :viTri) " +
                        "AND (:minGia IS NULL OR mh.hocPhiMoiGio >= :minGia) " +
                        "AND (:maxGia IS NULL OR mh.hocPhiMoiGio <= :maxGia)")
        Page<GiaSu> findByFilter(
                        @Param("tenMon") String tenMon,
                        @Param("trinhDo") String trinhDo,
                        @Param("viTri") String viTri,
                        @Param("minGia") Double minGia,
                        @Param("maxGia") Double maxGia,
                        @Param("maGiaSu") Integer maGiaSu,
                        Pageable pageable);
}
