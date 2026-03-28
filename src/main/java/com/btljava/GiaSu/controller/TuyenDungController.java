package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.dto.GiaSuUngTuyenRequest;
import com.btljava.GiaSu.dto.TuyenDungResponse;
import com.btljava.GiaSu.dto.TuyenDungTrucTiepRequest;
import com.btljava.GiaSu.entity.LopHoc;
import com.btljava.GiaSu.entity.UngTuyen;
import com.btljava.GiaSu.service.TuyenDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tuyen-dung")
@CrossOrigin("*")
public class TuyenDungController {

    @Autowired
    private TuyenDungService tuyenDungService;

    // Học viên gửi yêu cầu trực tiếp cho một gia sư cụ thể
    @PostMapping("/gui-loi-moi")
    public ResponseEntity<TuyenDungResponse<String>> guiLoiMoiTrucTiep(@RequestBody TuyenDungTrucTiepRequest request) {
        TuyenDungResponse<String> result = tuyenDungService.guiLoiMoiTrucTiep(request);
        return ResponseEntity.ok(result);
    }

    // Gia sư xem danh sách các lời mời tuyển dụng
    @GetMapping("/loi-moi/{maGiaSu}")
    public ResponseEntity<List<UngTuyen>> getLoiMoi(@PathVariable("maGiaSu") Integer maGiaSu) {
        List<UngTuyen> list = tuyenDungService.layDanhSachLoiMoi(maGiaSu);
        return ResponseEntity.ok(list);
    }

    // Gia sư đồng ý lời mời, tạo lớp học
    @PostMapping("/gia-su-dong-y")
    public ResponseEntity<LopHoc> giaSuDongY(
            @RequestParam("maGiaSu") Integer maGiaSu,
            @RequestParam("maYeuCau") Integer maYeuCau) {
        LopHoc lopHoc = tuyenDungService.giaSuDongY(maGiaSu, maYeuCau);
        return ResponseEntity.ok(lopHoc);
    }

    // Gia sư ứng tuyển vào một yêu cầu chung
    @PostMapping("/ung-tuyen")
    public ResponseEntity<TuyenDungResponse<String>> ungTuyen(@RequestBody GiaSuUngTuyenRequest request) {
        try {
            TuyenDungResponse<String> response = tuyenDungService.giaSuUngTuyen(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    TuyenDungResponse.<String>builder()
                            .status("error")
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    // Gia sư từ chối lời mời
    @PostMapping("/gia-su-tu-choi")
    public ResponseEntity<TuyenDungResponse<String>> giaSuTuChoi(
            @RequestParam("maGiaSu") Integer maGiaSu,
            @RequestParam("maYeuCau") Integer maYeuCau) {
        tuyenDungService.giaSuTuChoi(maGiaSu, maYeuCau);
        return ResponseEntity.ok(TuyenDungResponse.<String>builder()
                .status("success")
                .message("Đã từ chối lời mời dạy")
                .build());
    }

    // Học viên xem danh sách gia sư đã ứng tuyển vào yêu cầu của mình
    @GetMapping("/danh-sach-ung-vien/{maYeuCau}")
    public ResponseEntity<List<UngTuyen>> layDanhSachUngVien(@PathVariable("maYeuCau") Integer maYeuCau) {
        List<UngTuyen> list = tuyenDungService.layDanhSachUngVien(maYeuCau);
        return ResponseEntity.ok(list);
    }

    // Gia sư xem danh sách các đơn mình đã ứng tuyển
    @GetMapping("/da-ung-tuyen/{maGiaSu}")
    public ResponseEntity<List<UngTuyen>> layDanhSachDaUngTuyen(@PathVariable("maGiaSu") Integer maGiaSu) {
        List<UngTuyen> list = tuyenDungService.layDanhSachDaUngTuyen(maGiaSu);
        return ResponseEntity.ok(list);
    }

    // Học viên duyệt và đồng ý một gia sư ứng tuyển
    @PostMapping("/hoc-vien-duyet")
    public ResponseEntity<LopHoc> hocVienDuyet(
            @RequestParam("maHocVien") Integer maHocVien,
            @RequestParam("maGiaSu") Integer maGiaSu,
            @RequestParam("maYeuCau") Integer maYeuCau) {
        LopHoc lopHoc = tuyenDungService.hocVienDuyet(maHocVien, maGiaSu, maYeuCau);
        return ResponseEntity.ok(lopHoc);
    }

    // Học viên từ chối gia sư ứng tuyển
    @PostMapping("/hoc-vien-tu-choi")
    public ResponseEntity<TuyenDungResponse<String>> hocVienTuChoi(
            @RequestParam("maHocVien") Integer maHocVien,
            @RequestParam("maGiaSu") Integer maGiaSu,
            @RequestParam("maYeuCau") Integer maYeuCau) {
        tuyenDungService.hocVienTuChoi(maHocVien, maGiaSu, maYeuCau);
        return ResponseEntity.ok(TuyenDungResponse.<String>builder()
                .status("success")
                .message("Đã từ chối đơn ứng tuyển")
                .build());
    }
}
