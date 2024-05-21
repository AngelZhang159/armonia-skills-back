package com.armoniacode.armoniaskills.entity;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
public class MessageDTO {
    private UUID sender;
    private UUID receiver;
    private String content;
    private Timestamp date;
}
