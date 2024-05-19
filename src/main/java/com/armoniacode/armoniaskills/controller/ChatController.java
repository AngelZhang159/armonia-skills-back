package com.armoniacode.armoniaskills.controller;

import com.armoniacode.armoniaskills.entity.ChatMessage;
import com.armoniacode.armoniaskills.entity.ChatNotification;
import com.armoniacode.armoniaskills.entity.ChatRoom;
import com.armoniacode.armoniaskills.entity.User;
import com.armoniacode.armoniaskills.service.ChatMessageService;
import com.armoniacode.armoniaskills.service.ChatRoomService;
import com.armoniacode.armoniaskills.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final JWTUtil jwtUtil;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;


    //    ESTE ENDPOINT RECIBE UN ChatMessage Y LO GUARDA CON LOS DATOS QUE TIENE DENTRO (VER ENTITY ChatMessage)
    //    LUEGO LO MANDA A /queue/messages/{receiverId} DONDE EL RECEPTOR SE SUBSCRIBE Y RECIBE LOS MENSAJES
    @MessageMapping("/api/v1/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);

        simpMessagingTemplate.convertAndSend("/queue/messages/" + chatMessage.getReceiver().toString(),
                ChatNotification.builder()
                        .id(savedMsg.getId())
                        .senderId(savedMsg.getSender())
                        .receiverId(savedMsg.getReceiver())
                        .content(savedMsg.getContent())
                        .build());
    }

    // SE USA PARA RECUPERAR LOS MENSAJES Y MOSTRARLOS EN EL CHAT, LLAMAR CUANDO SE ABRE UN CHAT
    // RECIBE TOKEN DE UN USUARIO Y CHATID, SI EL ID DEL USUARIO ESTA EN EL CHATROOM DE ESA ID DEVUELVE TODOS LOS MENSAJES
    @GetMapping("/api/v1/messages/{chatId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@RequestHeader("Authorization") String Authorization, @PathVariable UUID chatId) {
        String token = Authorization.substring(7);

        ChatRoom chatRoom = chatRoomService.findById(chatId);

        if (chatRoom.getReceiverId().equals(jwtUtil.getUUID(token)) || chatRoom.getSenderId().equals(jwtUtil.getUUID(token))) {
            return ResponseEntity.ok(chatMessageService.findChatMessages(chatRoom.getSenderId(), chatRoom.getReceiverId(), chatRoom.getSkill()));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

//    SE USA PARA MOSTRAR LA LISTA DE CHATS EN LA VENTANA CHAT
//    COGE EL TOKEN Y DEVUELVE UNA LISTA DE ID DE CHATROOMS DONDE EL USUARIO ESTA PARTICIPANDO
    @GetMapping("/api/v1/chat")
    public ResponseEntity<List<ChatRoom>> getChats(@RequestHeader("Authorization") String token) {

        UUID id = jwtUtil.getUUID(token.substring(7));

        return ResponseEntity.ok(chatRoomService.findAllByUser(id));
    }


//    SE USA PARA CREAR UN NUEVO CHATROOM ENTRE DOS USUARIOS
//    COGE EL TOKEN Y EL ID DEL USUARIO CON EL QUE SE QUIERE EMPEZAR UN CHAT
//    DEVUELVE EL CHATROOM CREADO O EL YA EXISTENTE SI HAY UNO, LUEGO HAY QUE ABRIR LA VENTANA CHATROOM DE ESE ID
    @GetMapping("/api/v1/chat/new/{id}/{skillId}")
    public ResponseEntity<ChatRoom> newChat(@RequestHeader("Authorization") String token, @PathVariable UUID id, @PathVariable UUID skillId) {

        UUID userId = jwtUtil.getUUID(token.substring(7));

        Optional<UUID> chatId = chatRoomService.getChatRoomId(userId, id, skillId,true);

        return ResponseEntity.ok(chatRoomService.findById(chatId.get()));
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
