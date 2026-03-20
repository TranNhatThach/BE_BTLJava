package com.btljava.GiaSu.service;

import com.btljava.GiaSu.dto.GiaSuMonHocRequest;
import com.btljava.GiaSu.dto.GiaSuMonHocResponse;
import com.btljava.GiaSu.entity.GiaSu;
import com.btljava.GiaSu.entity.GiaSuMonHoc;
import com.btljava.GiaSu.entity.GiaSuMonHocId;
import com.btljava.GiaSu.entity.MonHoc;
import com.btljava.GiaSu.repository.GiaSuMonHocRepository;
import com.btljava.GiaSu.repository.GiaSuRepository;
import com.btljava.GiaSu.repository.MonHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiaSuMonHocService {

    private final GiaSuMonHocRepository giaSuMonHocRepository;
    private final GiaSuRepository giaSuRepository;
    private final MonHocRepository monHocRepository;

    public List<GiaSuMonHocResponse> getMonHocByGiaSu(Integer maGiaSu) {
        List<GiaSuMonHoc> list = giaSuMonHocRepository.findByGiaSu_MaGiaSu(maGiaSu);
        return list.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public GiaSuMonHocResponse addMonHocToGiaSu(Integer maGiaSu, GiaSuMonHocRequest request) {
        GiaSu giaSu = giaSuRepository.findById(maGiaSu)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy gia sư với ID: " + maGiaSu));
        MonHoc monHoc = monHocRepository.findById(request.getMaMon())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy môn học với ID: " + request.getMaMon()));

        GiaSuMonHocId id = new GiaSuMonHocId(maGiaSu, request.getMaMon());
        
        if (giaSuMonHocRepository.existsById(id)) {
            throw new RuntimeException("Gia sư đã đăng ký môn học này rồi");
        }

        GiaSuMonHoc gsmh = GiaSuMonHoc.builder()
                .id(id)
                .giaSu(giaSu)
                .monHoc(monHoc)
                .trinhDo(request.getTrinhDo())
                .hocPhiMoiGio(request.getHocPhiMoiGio())
                .build();

        return mapToResponse(giaSuMonHocRepository.save(gsmh));
    }

    public GiaSuMonHocResponse updateMonHocGiaSu(Integer maGiaSu, Integer maMon, GiaSuMonHocRequest request) {
        GiaSuMonHocId id = new GiaSuMonHocId(maGiaSu, maMon);
        GiaSuMonHoc gsmh = giaSuMonHocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đăng ký môn học này của gia sư"));

        gsmh.setTrinhDo(request.getTrinhDo());
        gsmh.setHocPhiMoiGio(request.getHocPhiMoiGio());

        return mapToResponse(giaSuMonHocRepository.save(gsmh));
    }

    public void removeMonHocGiaSu(Integer maGiaSu, Integer maMon) {
        GiaSuMonHocId id = new GiaSuMonHocId(maGiaSu, maMon);
        if (!giaSuMonHocRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy đăng ký môn học này của gia sư");
        }
        giaSuMonHocRepository.deleteById(id);
    }

    private GiaSuMonHocResponse mapToResponse(GiaSuMonHoc gsmh) {
        return GiaSuMonHocResponse.builder()
                .maMon(gsmh.getMonHoc().getMaMon())
                .tenMon(gsmh.getMonHoc().getTenMon())
                .moTa(gsmh.getMonHoc().getMoTa())
                .trinhDo(gsmh.getTrinhDo())
                .hocPhiMoiGio(gsmh.getHocPhiMoiGio())
                .build();
    }
}
