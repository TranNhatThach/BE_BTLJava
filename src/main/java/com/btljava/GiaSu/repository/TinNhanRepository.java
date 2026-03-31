package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.TinNhan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TinNhanRepository extends JpaRepository<TinNhan, Long> {

    List<TinNhan> findByRoomIdOrderByNgayGuiAsc(String roomId);
}
