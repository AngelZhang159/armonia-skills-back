package com.armoniacode.armoniaskills.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

//    {
//    "id": "550e8400-e29b-41d4-a716-446655440000", // ID PROPIO DEL CHAT
//    "chatId": "550e8400-e29b-41d4-a716-446655440001", // SENDER+RECEIVER (siempre esta cambiado para cada usuario del chat)
//    "senderId": "550e8400-e29b-41d4-a716-446655440002", // ID DEL USUARIO QUE ENVIA EL MENSAJE
//    "receiverId": "550e8400-e29b-41d4-a716-446655440003" // ID DEL USUARIO QUE RECIBE EL MENSAJE
//    }

    @Id
    @GeneratedValue
    private UUID id;
    private UUID chatId;
    private UUID senderId;
    private UUID receiverId;
    private UUID skill;

}
