package com.armoniacode.armoniaskills.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Lob
    private byte[] imageData;
}
