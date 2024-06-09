package com.armoniacode.armoniaskills.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
public class CompraVenta {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    @JsonBackReference
    private Skill skill;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    @JsonBackReference
    private User userSeller;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    @JsonBackReference
    private User userBuyer;

//  Pendiente al momento de la compra
    @Enumerated(EnumType.STRING)
    private StatusCompraEnum status = StatusCompraEnum.PENDIENTE;

    @CreationTimestamp
    private Timestamp date;

    @Override
    public String toString() {
        return "CompraVenta{}";
    }
}
