package com.armoniacode.armoniaskills.dto;

import com.armoniacode.armoniaskills.entity.StatusCompraEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComprasVentasDTO {

    private UUID id;
    private String imageURL;
    private String username;
    private String skillName;
    private Timestamp date;
    private StatusCompraEnum status;
    private UUID skillID;

}
