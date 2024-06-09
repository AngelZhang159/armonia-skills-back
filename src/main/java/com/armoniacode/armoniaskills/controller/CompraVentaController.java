package com.armoniacode.armoniaskills.controller;

import com.armoniacode.armoniaskills.dto.ComprasVentasDTO;
import com.armoniacode.armoniaskills.service.CompraVentaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class CompraVentaController {

    private final CompraVentaService comprasVentasService;

    @GetMapping("/compras")
    public ResponseEntity<List<ComprasVentasDTO>> getCompras(@RequestHeader("Authorization") String token) {
        return comprasVentasService.getCompras(token);
    }

    @GetMapping("/ventas")
    public ResponseEntity<List<ComprasVentasDTO>> getVentas(@RequestHeader("Authorization") String token) {
        return comprasVentasService.getVentas(token);
    }

    @PostMapping("/comprar/{idSkill}")
    public ResponseEntity<String> comprar(@RequestHeader("Authorization") String token, @PathVariable UUID idSkill) {
        return comprasVentasService.comprar(token, idSkill);
    }

}
