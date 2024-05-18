package com.armoniacode.armoniaskills.repository;

import com.armoniacode.armoniaskills.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    Optional<ChatRoom> findBySenderIdAndReceiverId(UUID senderId, UUID receiverId);

    List<ChatRoom> findAllBySenderIdOrReceiverId(UUID senderId, UUID receiverId);
}