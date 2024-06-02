package com.armoniacode.armoniaskills.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
public class ChatDTO {

    private UUID chatId;
    private String nombreUsuario;
    private String ultimoMensaje;
    private Timestamp ultimaHora;
    private String fotoPerfil;
    private String nombreSkill;
    private UUID otroUsuarioId;

}
