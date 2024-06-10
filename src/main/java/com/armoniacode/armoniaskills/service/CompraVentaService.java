package com.armoniacode.armoniaskills.service;

import com.armoniacode.armoniaskills.dto.ComprasVentasDTO;
import com.armoniacode.armoniaskills.entity.CompraVenta;
import com.armoniacode.armoniaskills.entity.Skill;
import com.armoniacode.armoniaskills.entity.User;
import com.armoniacode.armoniaskills.repository.CompraVentaRepository;
import com.armoniacode.armoniaskills.repository.UserRepository;
import com.armoniacode.armoniaskills.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompraVentaService {

    private final JWTUtil jwtUtil;
    private final SkillService skillService;
    private final UserRepository userRepository;
    private final CompraVentaRepository compraVentaRepository;

    public ResponseEntity<List<ComprasVentasDTO>> getCompras(String token) {

        token = token.substring(7);
        Optional<User> user = jwtUtil.getUserFromToken(token);

        if (user.isPresent()) {
            List<CompraVenta> compras = user.get().getCompraList();
            return ResponseEntity.ok(compras.stream().map(compra -> {
                Optional<User> userSeller = jwtUtil.getUserFromToken(compra.getUserSellerId());
                return getComprasVentasDTO(compra, userSeller);
            }).toList());
        } else {
            return ResponseEntity.ok(null);
        }
    }

    public ResponseEntity<List<ComprasVentasDTO>> getVentas(String token) {

        token = token.substring(7);
        Optional<User> user = jwtUtil.getUserFromToken(token);

        if (user.isPresent()) {
            List<CompraVenta> ventas = user.get().getVentaList();
            return ResponseEntity.ok(ventas.stream().map(venta -> {
                Optional<User> userBuyer = jwtUtil.getUserFromToken(venta.getUserBuyerId());
                return getComprasVentasDTO(venta, userBuyer);
            }).toList());
        } else {
            return ResponseEntity.ok(null);
        }
    }

    @NotNull
    private ComprasVentasDTO getComprasVentasDTO(CompraVenta venta, Optional<User> userBuyer) {
        Skill skill = skillService.getSkillById(UUID.fromString(venta.getSkillId()));

        ComprasVentasDTO comprasVentasDTO = new ComprasVentasDTO();
        comprasVentasDTO.setId(venta.getId());
        comprasVentasDTO.setImageURL(userBuyer.get().getImageURL());
        comprasVentasDTO.setUsername(userBuyer.get().getUsername());
        comprasVentasDTO.setSkillName(skill.getTitle());
        comprasVentasDTO.setDate(venta.getDate());
        comprasVentasDTO.setStatus(venta.getStatus());
        comprasVentasDTO.setSkillID(skill.getId());
        return comprasVentasDTO;
    }

    public ResponseEntity<String> comprar(String token, UUID idSkill) {

        token = token.substring(7);

        Skill skill = skillService.getSkillById(idSkill);

        User userBuyer = jwtUtil.getUserFromToken(token).get();
        User userSeller = userRepository.findById(skill.getUserID()).get();

        if (!(userBuyer.getBalance() < skill.getPrice())) {
            CompraVenta compraVenta = new CompraVenta();
            compraVenta.setUserBuyerId(String.valueOf(userBuyer.getId()));
            compraVenta.setUserSellerId(String.valueOf(userSeller.getId()));
            compraVenta.setSkillId(String.valueOf(skill.getId()));
            userBuyer.setBalance(userBuyer.getBalance() - skill.getPrice());

            compraVentaRepository.save(compraVenta);

            return ResponseEntity.ok("Compra realizada con éxito");
        } else {
            return ResponseEntity.badRequest().body("Saldo insuficiente");
        }
    }
}
