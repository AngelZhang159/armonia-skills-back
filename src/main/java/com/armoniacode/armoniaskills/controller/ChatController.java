package com.armoniacode.armoniaskills.controller;

import com.armoniacode.armoniaskills.entity.ChatMessage;
import com.armoniacode.armoniaskills.entity.ChatNotification;
import com.armoniacode.armoniaskills.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;


//    ESTE ENDPOINT RECIBE UN ChatMessage Y LO GUARDA CON LOS DATOS QUE TIENE DENTRO (VER ENTITY ChatMessage)
    @MessageMapping("/api/v1/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);

        simpMessagingTemplate.convertAndSendToUser(chatMessage.getReceiver().toString(),
                "/queue/messages",
                ChatNotification.builder()
                        .id(savedMsg.getId())
                        .senderId(savedMsg.getSender())
                        .receiverId(savedMsg.getReceiver())
                        .content(savedMsg.getContent())
                        .build());
    }

//    ESTE ENDPOINT RECIBE DOS UUID DE USUARIO Y DEVUELVE EL CHAT COMPLETO

    @GetMapping("/api/v1/messages/{senderId}/{receiverId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable UUID senderId, @PathVariable UUID receiverId) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, receiverId));
    }

//    @MessageMapping("/api/v1/chat/sendMessage")
//    @SendTo("/api/v1/topic/public")
//    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
//        return chatMessage;
//    }
//
//    @MessageMapping("/api/v1/chat/addUser")
//    @SendTo("/api/v1/topic/public")
//    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){
//
//        //Añadir usuario a la sesion del webSocket
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//        return chatMessage;
//    }

}
