package com.armoniacode.armoniaskills.dto;

import com.armoniacode.armoniaskills.entity.ChatMessage;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
public class ChatMessageDTO {

    private UUID chatId;
    private UUID sender;
    private UUID receiver;
    private String content;
    private Timestamp date;

    public static ChatMessageDTO fromChatMessage(ChatMessage chatMessage) {
        return ChatMessageDTO.builder()
                .chatId(chatMessage.getChatId())
                .sender(chatMessage.getSender())
                .receiver(chatMessage.getReceiver())
                .content(chatMessage.getContent())
                .date(chatMessage.getDate())
                .build();
    }
}
