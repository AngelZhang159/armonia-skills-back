package com.armoniacode.armoniaskills.controller;

import com.armoniacode.armoniaskills.entity.Skill;
import com.armoniacode.armoniaskills.service.SkillService;
import com.armoniacode.armoniaskills.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/skill")
@RestController
@Slf4j
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;
    private final JWTUtil jwtUtil;

    @GetMapping
    public List<Skill> getSkills() {
        return skillService.getSkillList();
    }

    @PostMapping
    public ResponseEntity<Skill> postSkill(@RequestHeader String Authorization, @RequestBody Skill skill) {

        log.info(skill.toString());

        String token = Authorization.substring(7);
        UUID userID = jwtUtil.getUUID(token);
        skill.setUserID(userID);

        return new ResponseEntity<>(skillService.postSkill(skill), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public Skill getSkillById(@PathVariable("id") UUID id) {
        return skillService.getSkillById(id);
    }

    @PutMapping("/{id}")
    public Skill updateSkillById(@PathVariable("id") UUID id, @RequestBody Skill skill) {
        return skillService.updateSkillById(id, skill);
    }

    @DeleteMapping("/{id}")
    public String deleteSkill(@PathVariable("id") UUID id) {
        skillService.deleteSkill(id);
        return "Skill with id: " + id + " deleted";
    }

    @GetMapping("/category/{category}/search/{query}/price/{priceRange}")
    public List<Skill> getSkillsByQuery(@PathVariable("category") String category, @PathVariable("query") String query, @PathVariable("priceRange") String priceRange){


        List<Skill> skills;

        // Paso 1: Obtén todas las habilidades que coincidan con la categoría proporcionada
        if(category.equals("Todas")) {
            skills = skillService.getSkillList();
        } else {
            skills = skillService.getSkillsByCategory(category);
        }

        // Paso 2: Si la consulta no está vacía, filtra las habilidades para incluir solo aquellas cuyo título contenga la consulta
        if(!query.equals("default_query")) {
            skills = skills.stream()
                    .filter(skill -> skill.getTitle().contains(query))
                    .collect(Collectors.toList());
        }

        // Paso 3: Si el rango de precio no está vacío, filtra las habilidades para incluir solo aquellas cuyo precio esté dentro del rango de precio

        if(!priceRange.equals("Todos")) {
            // Precio mínimo y máximo del rango seleccionado
            String[] precios = priceRange.split("-");
            int minPrice = Integer.parseInt(precios[0]);
            int maxPrice = Integer.parseInt(precios[1]);
            skills = skills.stream()
                    .filter(skill -> skill.getPrice() >= minPrice && skill.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }

        System.out.println("🐒🐒🐒🐒🐒👹👹👹👹🥶🥶🥶🥶 Habilidades encontradas: " + skills);
        return skills;
    }

    @GetMapping("/category/{category}")
    public List<Skill> getSkillsByCategory(@PathVariable("category") String category) {

        if(category.equals("Todas"))
            return skillService.getSkillList();

        return skillService.getSkillsByCategory(category);
    }
}
