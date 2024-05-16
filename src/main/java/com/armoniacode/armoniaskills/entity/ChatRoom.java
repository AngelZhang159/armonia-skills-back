package com.armoniacode.armoniaskills.entity;

import jakarta.persistence.Entity;
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

//    CHAT CON ID PROPIO,
//    chatid (USERID+USERID), PARA UN USUARIO ES SU ID + ID DEL OTRO USUARIO, PARA EL OTRO USUARIO ES EL ID DEL OTRO USUARIO + SU ID
//    senderid,
//    receiverid

    @Id
    private UUID id;
    private UUID chatId;
    private UUID senderId;
    private UUID receiverId;
}
