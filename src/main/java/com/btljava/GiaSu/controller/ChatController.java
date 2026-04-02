package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.entity.TinNhan;
import com.btljava.GiaSu.repository.TinNhanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private TinNhanRepository tinNhanRepository;

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@DestinationVariable String roomId, @Payload ChatMessage chatMessage) {
        if (roomId == null || roomId.trim().isEmpty()) {
            roomId = "public";
        }

        TinNhan tinNhan = TinNhan.builder()
                .roomId(roomId)
                .senderId(chatMessage.getSenderId())
                .senderRole(chatMessage.getFrom())
                .senderName(chatMessage.getSenderName())
                .noiDung(chatMessage.getText())
                .fileUrl(chatMessage.getFileUrl())
                .fileName(chatMessage.getFileName())
                .fileType(chatMessage.getFileType())
                .build();
        TinNhan saved = tinNhanRepository.save(tinNhan);

        chatMessage.setId(String.valueOf(saved.getMaTinNhan()));
        chatMessage.setTime(saved.getNgayGui() != null ? saved.getNgayGui().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")) : "");

        messagingTemplate.convertAndSend("/topic/chat/" + roomId, chatMessage);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatMessage {
        private String id;
        private String from;
        private String text;
        private String time;
        private Integer senderId;
        private String senderName;
        private String type;
        
        private String fileUrl;
        private String fileName;
        private String fileType;
    }
}
