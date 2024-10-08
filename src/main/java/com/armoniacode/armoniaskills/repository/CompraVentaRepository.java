package com.armoniacode.armoniaskills.repository;

import com.armoniacode.armoniaskills.entity.CompraVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface CompraVentaRepository extends JpaRepository<CompraVenta, UUID> {
    List<CompraVenta> getComprasByUserBuyerIdOrderByDate(String s);

    List<CompraVenta> getVentasByUserSellerIdOrderByDate(String s);
}
