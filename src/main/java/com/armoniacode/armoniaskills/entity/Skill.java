package com.armoniacode.armoniaskills.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Double price;

    private String location;

    @Column(nullable = false)
    private UUID userID;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> imageList;

    @OneToMany
//    @JsonBackReference
    private List<CompraVenta> compraVentaList;

    @Override
    public String toString() {
        return "Skill{}";
    }
}
