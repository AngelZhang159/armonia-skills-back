package com.armoniacode.armoniaskills.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Image {

    @Id
    @GeneratedValue
    private UUID id;

    private String fileName;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageData;

    private String contentType;
}
