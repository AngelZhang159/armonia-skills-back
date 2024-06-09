package com.armoniacode.armoniaskills.repository;

import com.armoniacode.armoniaskills.entity.CompraVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface CompraVentaRepository extends JpaRepository<CompraVenta, UUID> {
}
