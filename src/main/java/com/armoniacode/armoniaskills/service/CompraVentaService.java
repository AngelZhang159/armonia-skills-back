package com.armoniacode.armoniaskills.service;

import com.armoniacode.armoniaskills.dto.ComprasVentasDTO;
import com.armoniacode.armoniaskills.entity.CompraVenta;
import com.armoniacode.armoniaskills.entity.Skill;
import com.armoniacode.armoniaskills.entity.StatusCompraEnum;
import com.armoniacode.armoniaskills.entity.Users;
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
        Optional<Users> user = jwtUtil.getUserFromToken(token);

        if (user.isPresent()) {
            List<CompraVenta> compras = compraVentaRepository.getComprasByUserBuyerIdOrderByDate(String.valueOf(user.get().getId()));
            return ResponseEntity.ok(compras.stream().map(compra -> {
                Optional<Users> userSeller = userRepository.findById(UUID.fromString(compra.getUserSellerId()));
                return getComprasVentasDTO(compra, userSeller);
            }).toList());
        } else {
            return ResponseEntity.ok(null);
        }
    }

    public ResponseEntity<List<ComprasVentasDTO>> getVentas(String token) {

        token = token.substring(7);
        Optional<Users> user = jwtUtil.getUserFromToken(token);

        if (user.isPresent()) {
            List<CompraVenta> ventas = compraVentaRepository.getVentasByUserSellerIdOrderByDate(String.valueOf(user.get().getId()));
            return ResponseEntity.ok(ventas.stream().map(venta -> {
                Optional<Users> userBuyer = userRepository.findById(UUID.fromString(venta.getUserBuyerId()));
                return getComprasVentasDTO(venta, userBuyer);
            }).toList());
        } else {
            return ResponseEntity.ok(null);
        }
    }

    @NotNull
    private ComprasVentasDTO getComprasVentasDTO(CompraVenta venta, Optional<Users> userBuyer) {
        Skill skill = skillService.getSkillById(UUID.fromString(venta.getSkillId()));

        ComprasVentasDTO comprasVentasDTO = new ComprasVentasDTO();
        comprasVentasDTO.setId(venta.getId());
        comprasVentasDTO.setImageURL(userBuyer.get().getImageURL());
        comprasVentasDTO.setUsername(userBuyer.get().getUsername());
        comprasVentasDTO.setSkillName(skill.getTitle());
        comprasVentasDTO.setDate(venta.getDate());
        comprasVentasDTO.setStatus(venta.getStatus());
        comprasVentasDTO.setSkillId(skill.getId());
        comprasVentasDTO.setPrice(venta.getPrice());
        return comprasVentasDTO;
    }

    public ResponseEntity<String> comprar(String token, UUID idSkill) {

        token = token.substring(7);

        Skill skill = skillService.getSkillById(idSkill);

        Users userBuyer = jwtUtil.getUserFromToken(token).get();
        Users userSeller = userRepository.findById(skill.getUserID()).get();

        if (!(userBuyer.getBalance() < skill.getPrice())) {
            CompraVenta compraVenta = new CompraVenta();
            compraVenta.setUserBuyerId(String.valueOf(userBuyer.getId()));
            compraVenta.setUserSellerId(String.valueOf(userSeller.getId()));
            compraVenta.setSkillId(String.valueOf(skill.getId()));
            compraVenta.setPrice(skill.getPrice());
            userBuyer.setBalance(userBuyer.getBalance() - skill.getPrice());

            compraVentaRepository.save(compraVenta);

            return ResponseEntity.ok("Compra realizada con éxito");
        } else {
            return ResponseEntity.badRequest().body("Saldo insuficiente");
        }
    }

    public ResponseEntity<String> modificarVenta(String token, UUID idVenta, StatusCompraEnum status) {

        token = token.substring(7);

        Optional<Users> user = jwtUtil.getUserFromToken(token);

        if (user.isPresent()) {
            Optional<CompraVenta> compraVenta = compraVentaRepository.findById(idVenta);

            if (compraVenta.isPresent()) {
                CompraVenta compraVentaUpdate = compraVenta.get();
                if (compraVentaUpdate.getUserSellerId().equals(String.valueOf(user.get().getId()))) {

                    compraVentaUpdate.setStatus(status);
                    compraVentaRepository.save(compraVentaUpdate);


                    return ResponseEntity.ok("Venta modificada con éxito");
                } else if (status.equals(StatusCompraEnum.COMPLETADO) && compraVentaUpdate.getUserBuyerId().equals(String.valueOf(user.get().getId()))) {
                    Users userBuyer = userRepository.findById(UUID.fromString(compraVentaUpdate.getUserSellerId())).get();

                    compraVentaUpdate.setStatus(status);
                    compraVentaRepository.save(compraVentaUpdate);

                    userBuyer.setBalance(userBuyer.getBalance() + compraVenta.get().getPrice());
                    userRepository.save(userBuyer);
                    return ResponseEntity.ok("Venta modificada con éxito");
                } else {
                    return ResponseEntity.badRequest().body("Usuario no autorizado");
                }
            } else {
                return ResponseEntity.badRequest().body("Venta no encontrada");
            }
        } else {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }
    }

    public ResponseEntity<ComprasVentasDTO> getCompraVenta(String token, UUID idVenta) {

        token = token.substring(7);
        Optional<Users> user = jwtUtil.getUserFromToken(token);

        if (user.isPresent()) {
            Optional<CompraVenta> compraVenta = compraVentaRepository.findById(idVenta);

            if (compraVenta.isPresent()) {
                Optional<Users> userSeller = userRepository.findById(UUID.fromString(compraVenta.get().getUserSellerId()));
                return ResponseEntity.ok(getComprasVentasDTO(compraVenta.get(), userSeller));
            } else {
                return ResponseEntity.ok(null);
            }
        } else {
            return ResponseEntity.ok(null);
        }
    }
}
