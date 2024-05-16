package com.armoniacode.armoniaskills.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ChatMessage {

    @Id
    private UUID id;
    private UUID chatId;
    private UUID sender;
    private UUID receiver;
    private String content;
    private MessageType type;
    private Date date;

}
