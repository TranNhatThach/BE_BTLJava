package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.dto.MonHocDTO;
import com.btljava.GiaSu.service.MonHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mon-hoc")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MonHocController {

    private final MonHocService monHocService;

    @GetMapping
    public ResponseEntity<List<MonHocDTO>> getAll() {
        return ResponseEntity.ok(monHocService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonHocDTO> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(monHocService.getById(id));
    }

    @PostMapping
    public ResponseEntity<MonHocDTO> create(@RequestBody MonHocDTO dto) {
        return ResponseEntity.ok(monHocService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MonHocDTO> update(@PathVariable("id") Integer id, @RequestBody MonHocDTO dto) {
        return ResponseEntity.ok(monHocService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        monHocService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
