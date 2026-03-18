package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.GiaSuMonHoc;
import com.btljava.GiaSu.entity.GiaSuMonHocId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GiaSuMonHocRepository extends JpaRepository<GiaSuMonHoc, GiaSuMonHocId> {
//    @Query("SELECT gsmh FROM GiaSuMonHoc gsmh " +
//            "WHERE (:tenMon IS NULL OR LOWER(REPLACE(gsmh.monHoc.tenMon, ' ', '')) " +
//            "LIKE LOWER(CONCAT('%', REPLACE(:tenMon, ' ', ''), '%'))) " +
//            "AND (:trinhDo IS NULL OR LOWER(REPLACE(gsmh.trinhDo, ' ', '')) " +
//            "LIKE LOWER(CONCAT('%', REPLACE(:trinhDo, ' ', ''), '%')))")
//    List<GiaSuMonHoc> findByFilter(@Param("tenMon") String tenMon, @Param("trinhDo") String trinhDo);


    @Query("SELECT gsmh FROM GiaSuMonHoc gsmh " +
            "WHERE (:tenMon IS NULL OR LOWER(REPLACE(gsmh.monHoc.tenMon, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:tenMon, ' ', ''), '%'))) " +
            "AND (:trinhDo IS NULL OR LOWER(REPLACE(gsmh.trinhDo, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:trinhDo, ' ', ''), '%'))) " +
            "AND (:viTri IS NULL OR LOWER(REPLACE(gsmh.giaSu.taiKhoan.viTri, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:viTri, ' ', ''), '%'))) " +
            "AND (:minGia IS NULL OR gsmh.hocPhiMoiGio >= :minGia) " +
            "AND (:maxGia IS NULL OR gsmh.hocPhiMoiGio <= :maxGia)")
    List<GiaSuMonHoc> findByFilter(
            @Param("tenMon") String tenMon,
            @Param("trinhDo") String trinhDo,
            @Param("viTri") String viTri,
            @Param("minGia") Double minGia,
            @Param("maxGia") Double maxGia);

    // tạm thời chưa cập nhật tìm theo địa điểm khoảng giá
    // vi SQL tạo sai thiếu mất địa chỉ cho tài khoản nên sập r :))
}
