package com.armoniacode.armoniaskills.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class ChatDTO {

    private UUID chatId;
    private String nombreUsuario;
    private String ultimoMensaje;
    private Date ultimaHora;
    private String fotoPerfil;
    private String nombreSkill;

}
