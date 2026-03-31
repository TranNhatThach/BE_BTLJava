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
        // Lưu tin nhắn vào database
        TinNhan tinNhan = TinNhan.builder()
                .roomId(roomId)
                .senderId(chatMessage.getSenderId())
                .senderRole(chatMessage.getFrom())
                .senderName(chatMessage.getSenderName())
                .noiDung(chatMessage.getText())
                .build();
        TinNhan saved = tinNhanRepository.save(tinNhan);

        // Gán ID từ DB cho message trả về để tránh trùng lặp
        chatMessage.setId(String.valueOf(saved.getMaTinNhan()));

        // Gửi tin nhắn đến topic riêng của phòng chat
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
    }
}
