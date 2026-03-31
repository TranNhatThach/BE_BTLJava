package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.entity.TinNhan;
import com.btljava.GiaSu.repository.TinNhanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin("*")
public class ChatHistoryController {

    @Autowired
    private TinNhanRepository tinNhanRepository;

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Lấy lịch sử chat của một phòng chat theo roomId
     * GET /api/chat/history/{roomId}
     */
    @GetMapping("/history/{roomId}")
    public ResponseEntity<List<ChatController.ChatMessage>> getHistory(@PathVariable String roomId) {
        List<TinNhan> messages = tinNhanRepository.findByRoomIdOrderByNgayGuiAsc(roomId);

        List<ChatController.ChatMessage> result = messages.stream().map(tn -> 
            ChatController.ChatMessage.builder()
                .id(String.valueOf(tn.getMaTinNhan()))
                .from(tn.getSenderRole())
                .text(tn.getNoiDung())
                .time(tn.getNgayGui() != null ? tn.getNgayGui().format(TIME_FMT) : "")
                .senderId(tn.getSenderId())
                .senderName(tn.getSenderName())
                .build()
        ).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
