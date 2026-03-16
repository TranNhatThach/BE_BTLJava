package com.btljava.GiaSu.service;

import com.btljava.GiaSu.entity.GiaSuMonHoc;
import com.btljava.GiaSu.repository.GiaSuMonHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GiaSuService {
    private final GiaSuMonHocRepository giaSuMonHocRepository;

    public List<GiaSuMonHoc> timKiemGiaSu(String monHoc, String trinhDo) {

        String monHocClean = (monHoc != null && !monHoc.trim().isEmpty()) ? monHoc.trim() : null;
        String trinhDoClean = (trinhDo != null && !trinhDo.trim().isEmpty()) ? trinhDo.trim() : null;

        return giaSuMonHocRepository.findByFilter(monHocClean, trinhDoClean);
    }
}
