package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.entity.TinNhan;
import com.btljava.GiaSu.repository.TinNhanRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.*;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final TinNhanRepository tinNhanRepository;

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@DestinationVariable String roomId, @Payload ChatMessage chatMessage) {
        TinNhan tinNhan = TinNhan.builder()
                .roomId(roomId)
                .senderId(chatMessage.getSenderId())
                .senderRole(chatMessage.getFrom())
                .senderName(chatMessage.getSenderName())
                .noiDung(chatMessage.getText())
                .build();
        TinNhan saved = tinNhanRepository.save(tinNhan);

        chatMessage.setId(String.valueOf(saved.getMaTinNhan()));

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
        private String type;
        private String time;
        private Integer senderId;
        private String senderName;
        private String fileUrl;
        private String fileName;
        private String fileType;
    }
}
