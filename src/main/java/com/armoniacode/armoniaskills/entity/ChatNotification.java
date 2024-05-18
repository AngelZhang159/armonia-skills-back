package com.armoniacode.armoniaskills.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {

//    {
//    "id": "550e8400-e29b-41d4-a716-446655440000", // ID DE LA NOTIFICACION
//    "senderId": "550e8400-e29b-41d4-a716-446655440001", // ID DEL USUARIO QUE ENVIA LA NOTIFICACION
//    "receiverId": "550e8400-e29b-41d4-a716-446655440002", // ID DEL USUARIO QUE RECIBE LA NOTIFICACION
//    "content": "Hello, how are you?" // CONTENIDO DE LA NOTIFICACION
//    }

    private UUID id;
    private UUID senderId;
    private UUID receiverId;
    private String content;
}

