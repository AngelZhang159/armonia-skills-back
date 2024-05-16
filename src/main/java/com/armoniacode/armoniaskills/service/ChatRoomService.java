package com.armoniacode.armoniaskills.service;

import com.armoniacode.armoniaskills.entity.ChatRoom;
import com.armoniacode.armoniaskills.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public Optional<UUID> getChatRoomId(UUID senderId, UUID receiverId, boolean createIfNotExist) {
    return chatRoomRepository.findBySenderIdAndReceiverId(senderId, receiverId)
            .map(ChatRoom::getId)
            .or(() -> {
                if (createIfNotExist) {
                    UUID chatId = createChat(senderId, receiverId);
                    return Optional.of(chatId);
                }
                return Optional.empty();
            });
}

    private UUID createChat(UUID senderId, UUID receiverId) {
        UUID chatId = UUID.randomUUID();

        ChatRoom idSender = ChatRoom.builder()
                .chatId(chatId)
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        ChatRoom idReceiver = ChatRoom.builder()
                .chatId(chatId)
                .senderId(receiverId)
                .receiverId(senderId)
                .build();

        chatRoomRepository.save(idSender);
        chatRoomRepository.save(idReceiver);
        return chatId;
    }
}
