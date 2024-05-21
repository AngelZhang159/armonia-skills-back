package com.armoniacode.armoniaskills.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ChatMessage {

//    {
//        "id": "550e8400-e29b-41d4-a716-446655440000", // ID PROPIO DEL MENSAJE
//            "chatid": "550e8400-e29b-41d4-a716-446655440001", // ID DEL CHAT
//            "sender": "550e8400-e29b-41d4-a716-446655440002", // ID DEL USUARIO QUE ENVIA EL MENSAJE
//            "receiver": "550e8400-e29b-41d4-a716-446655440003", // ID DEL USUARIO QUE RECIBE EL MENSAJE
//            "content": "hello, how are you?", // CONTENIDO DEL MENSAJE
//            "type": "CHAT", // TIPO DE MENSAJE, PUEDE SER CHAT O JOIN O LEAVE
//            "date": "2022-04-01t10:30:00" // FECHA Y HORA DEL MENSAJE
//    }

    @Id
    @GeneratedValue
    private UUID id;
    private UUID chatId;
    private UUID sender;
    private UUID receiver;
    private UUID skillId;
    private String content;
    private Timestamp date;

}
