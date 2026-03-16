package com.btljava.GiaSu.repository;

import com.btljava.GiaSu.entity.GiaSu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiaSuRepository extends JpaRepository<GiaSu, Integer> {
    @Override
    <S extends GiaSu> List<S> saveAllAndFlush(Iterable<S> entities);
}
