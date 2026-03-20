package com.btljava.GiaSu.service;

import com.btljava.GiaSu.dto.MonHocDTO;
import com.btljava.GiaSu.entity.MonHoc;
import com.btljava.GiaSu.repository.MonHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonHocService {

    private final MonHocRepository monHocRepository;

    public List<MonHocDTO> getAll() {
        return monHocRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public MonHocDTO getById(Integer id) {
        MonHoc monHoc = monHocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy môn học với ID: " + id));
        return mapToDTO(monHoc);
    }

    public MonHocDTO create(MonHocDTO dto) {
        MonHoc monHoc = MonHoc.builder()
                .tenMon(dto.getTenMon())
                .moTa(dto.getMoTa())
                .build();
        return mapToDTO(monHocRepository.save(monHoc));
    }

    public MonHocDTO update(Integer id, MonHocDTO dto) {
        MonHoc monHoc = monHocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy môn học với ID: " + id));
        
        monHoc.setTenMon(dto.getTenMon());
        monHoc.setMoTa(dto.getMoTa());
        
        return mapToDTO(monHocRepository.save(monHoc));
    }

    public void delete(Integer id) {
        if (!monHocRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy môn học với ID: " + id);
        }
        monHocRepository.deleteById(id);
    }

    private MonHocDTO mapToDTO(MonHoc monHoc) {
        return MonHocDTO.builder()
                .maMon(monHoc.getMaMon())
                .tenMon(monHoc.getTenMon())
                .moTa(monHoc.getMoTa())
                .build();
    }
}
