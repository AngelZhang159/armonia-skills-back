package com.armoniacode.armoniaskills.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Entity
@Data
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    private String fullName;
    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private int phone;
    @NotNull
    private String password;
    @NotNull
    private String role = "USER";

}
