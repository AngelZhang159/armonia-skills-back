package com.armoniacode.armoniaskills.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

//    @ManyToOne
//    @JoinColumn(referencedColumnName = "id")
//    @JsonBackReference
    private String skillId;

//    @ManyToOne
//    @JoinColumn(referencedColumnName = "id")
//    @JsonIdentityReference(alwaysAsId = true)
    private String userSellerId;

//    @ManyToOne
//    @JoinColumn(referencedColumnName = "id")
//    @JsonIdentityReference(alwaysAsId = true)
    private String userBuyerId;

//  Pendiente al momento de la compra
    @Enumerated(EnumType.STRING)
    private StatusCompraEnum status = StatusCompraEnum.PENDIENTE;

    @CreationTimestamp
    private Timestamp date;

}
