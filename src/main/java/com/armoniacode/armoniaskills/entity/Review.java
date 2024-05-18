package com.armoniacode.armoniaskills.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String content;

    @Column(nullable = false)
    private int stars;

    private UUID skillId;

    @Column(nullable = false)
    private UUID sellerId;

    @Column(nullable = false)
    private UUID buyerId;



}
