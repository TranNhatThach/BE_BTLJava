package com.btljava.GiaSu.service;

import com.btljava.GiaSu.entity.TaiKhoan;
import com.btljava.GiaSu.entity.ThongBao;
import com.btljava.GiaSu.repository.ThongBaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final ThongBaoRepository thongBaoRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void guiThongBao(TaiKhoan nguoiNhan, String noiDung, String loai) {
        // 1. Lưu vào Database
        ThongBao thongBao = ThongBao.builder()
                .taiKhoan(nguoiNhan)
                .noiDung(noiDung)
                .loai(loai)
                .daDoc(false)
                .build();
        thongBao = thongBaoRepository.save(thongBao);

        // 2. Gửi qua WebSocket (Realtime)
        // Topic: /topic/notifications/{maTaiKhoan}
        messagingTemplate.convertAndSend("/topic/notifications/" + nguoiNhan.getMaTaiKhoan(), thongBao);
    }

    public List<ThongBao> layThongBaoCuaToi(Integer maTaiKhoan) {
        return thongBaoRepository.findByTaiKhoan_MaTaiKhoanOrderByNgayTaoDesc(maTaiKhoan);
    }

    public void danhDauDaDoc(Integer maThongBao) {
        thongBaoRepository.findById(maThongBao).ifPresent(tb -> {
            tb.setDaDoc(true);
            thongBaoRepository.save(tb);
        });
    }
}
