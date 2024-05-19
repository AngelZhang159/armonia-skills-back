package com.armoniacode.armoniaskills.service;

import com.armoniacode.armoniaskills.entity.ChatRoom;
import com.armoniacode.armoniaskills.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public Optional<UUID> getChatRoomId(UUID senderId, UUID receiverId, UUID skillId, boolean createIfNotExist) {
        return chatRoomRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .map(ChatRoom::getId)
                .or(() -> {
                    if (createIfNotExist) {
                        UUID chatId = createChat(senderId, receiverId, skillId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    private UUID createChat(UUID senderId, UUID receiverId, UUID skillId) {

        ChatRoom idSender = ChatRoom.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(idSender);
        return savedChatRoom.getId();
    }

    public List<ChatRoom> findAllByUser(UUID userId) {
        return chatRoomRepository.findAllBySenderIdOrReceiverId(userId, userId);
    }

    public ChatRoom findById(UUID chatId) {
        return chatRoomRepository.findById(chatId).orElse(null);
    }
}
