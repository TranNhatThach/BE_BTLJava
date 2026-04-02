package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.entity.TinNhan;
import com.btljava.GiaSu.repository.TinNhanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatHistoryController {

    private final TinNhanRepository tinNhanRepository;

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    @GetMapping("/history/{roomId}")
    public ResponseEntity<List<ChatController.ChatMessage>> getHistory(@PathVariable String roomId) {
        List<TinNhan> messages = tinNhanRepository.findByRoomIdOrderByNgayGuiAsc(roomId);

        List<ChatController.ChatMessage> result = messages.stream().map(tn -> ChatController.ChatMessage.builder()
                .id(String.valueOf(tn.getMaTinNhan()))
                .from(tn.getSenderRole())
                .text(tn.getNoiDung())
                .type(tn.getFileType() != null && !tn.getFileType().isEmpty() ? tn.getFileType()
                        : (tn.getNoiDung() == null || tn.getNoiDung().isEmpty() ? "FILE" : "CHAT"))
                .time(tn.getNgayGui() != null ? tn.getNgayGui().format(TIME_FMT) : "")
                .senderId(tn.getSenderId())
                .senderName(tn.getSenderName())
                .fileUrl(tn.getFileUrl())
                .fileName(tn.getFileName())
                .fileType(tn.getFileType())
                .build()).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
