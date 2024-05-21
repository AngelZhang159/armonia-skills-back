package com.armoniacode.armoniaskills.controller;

import com.armoniacode.armoniaskills.entity.*;
import com.armoniacode.armoniaskills.service.ChatMessageService;
import com.armoniacode.armoniaskills.service.ChatRoomService;
import com.armoniacode.armoniaskills.service.SkillService;
import com.armoniacode.armoniaskills.service.UserService;
import com.armoniacode.armoniaskills.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final JWTUtil jwtUtil;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final SkillService skillService;

    // Retrieve messages for a specific chat room
        @GetMapping("/api/v1/messages/{chatId}")
    public ResponseEntity<List<MessageDTO>> findChatMessages(@RequestHeader("Authorization") String Authorization, @PathVariable UUID chatId) {
        String token = Authorization.substring(7);
        ChatRoom chatRoom = chatRoomService.findById(chatId);

        if (chatRoom.getReceiverId().equals(jwtUtil.getUUID(token)) || chatRoom.getSenderId().equals(jwtUtil.getUUID(token))) {

            List<ChatMessage> listaMensajes = chatMessageService.findChatMessages(chatId);

            List<MessageDTO> listaMensajesDTO = listaMensajes.stream().map(chatMessage -> MessageDTO.builder()
                    .sender(chatMessage.getSender())
                    .receiver(chatMessage.getReceiver())
                    .content(chatMessage.getContent())
                    .date(chatMessage.getDate())
                    .build()).toList();

            return ResponseEntity.ok(listaMensajesDTO);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // Retrieve list of chat rooms for a user
    @GetMapping("/api/v1/chat")
    public ResponseEntity<List<ChatDTO>> getChats(@RequestHeader("Authorization") String token) {
        UUID id = jwtUtil.getUUID(token.substring(7));
        List<ChatRoom> chatRooms = chatRoomService.findAllByUser(id);
        ArrayList<ChatDTO> chatDTOs = new ArrayList<>();

        for (ChatRoom chatRoom : chatRooms) {
            chatRoom.setSenderId(id);
            User user = userService.getUserById(chatRoom.getReceiverId());
            Skill skill = skillService.getSkillById(chatRoom.getSkill());
            ChatMessage lastMessage = chatMessageService.findLastMessage(chatRoom.getId());

            chatDTOs.add(ChatDTO.builder()
                    .chatId(chatRoom.getId())
                    .fotoPerfil(user.getImageURL())
                    .nombreUsuario(user.getUsername())
                    .nombreSkill(skill.getTitle())
                    .ultimoMensaje(Optional.ofNullable(lastMessage).map(ChatMessage::getContent).orElse(null))
                    .ultimaHora(Optional.ofNullable(lastMessage).map(ChatMessage::getDate).orElse(null))
                    .build());
        }

        return ResponseEntity.ok(chatDTOs);
    }

    // Create a new chat room between two users
    @GetMapping("/api/v1/chat/new/{id}/{skillId}")
    public ResponseEntity<ChatRoom> newChat(@RequestHeader("Authorization") String token, @PathVariable UUID id, @PathVariable UUID skillId) {
        UUID userId = jwtUtil.getUUID(token.substring(7));
        Optional<UUID> chatId = chatRoomService.getChatRoomId(userId, id, skillId, true);
        return ResponseEntity.ok(chatRoomService.findById(chatId.get()));
    }
}
