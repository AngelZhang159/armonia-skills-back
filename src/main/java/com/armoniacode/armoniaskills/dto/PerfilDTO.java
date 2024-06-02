package com.armoniacode.armoniaskills.dto;

import com.armoniacode.armoniaskills.entity.Review;
import com.armoniacode.armoniaskills.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PerfilDTO {

    private String nombre;
    private String foto;
    private int telefono;
    private List<Review> reviews;
    private List<Skill> skills;
}
