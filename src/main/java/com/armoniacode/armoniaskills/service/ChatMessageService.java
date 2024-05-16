package com.armoniacode.armoniaskills.service;

import com.armoniacode.armoniaskills.entity.ChatMessage;
import com.armoniacode.armoniaskills.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

//    CUANDO SE LLAMA A save(chatMessage) SE GUARDA EL MENSAJE EN LA BASE DE DATOS,
//    SI NO EXISTE UN CHATROOM ENTRE LOS DOS USUARIOS SE CREA UNO NUEVO

    public ChatMessage save(ChatMessage chatMessage) {
        Optional<UUID> chatId = chatRoomService.getChatRoomId(chatMessage.getSender(), chatMessage.getReceiver(), true);

        chatMessage.setChatId(chatId.get());
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> findChatMessages(UUID sender, UUID receiver) {
        Optional<UUID> chatId = chatRoomService.getChatRoomId(sender, receiver, false);
        return chatId.map(chatMessageRepository::findAllByChatId).orElse(new ArrayList<>());
    }
}
