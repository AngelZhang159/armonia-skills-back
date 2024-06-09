package com.armoniacode.armoniaskills.controller;

import com.armoniacode.armoniaskills.entity.Skill;
import com.armoniacode.armoniaskills.service.SkillService;
import com.armoniacode.armoniaskills.util.JWTUtil;
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
public class SkillController {

    private final SkillService skillService;
    private final JWTUtil jwtUtil;

    public SkillController(SkillService skillService, JWTUtil jwtUtil) {
        this.skillService = skillService;
        this.jwtUtil = jwtUtil;
    }

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

    @GetMapping("/category/{category}/search/{query}")
    public List<Skill> getSkillsByQuery(@PathVariable("category") String category, @PathVariable("query") String query) {

        if (query == null || query.isEmpty()) {
            System.out.println("Query vacio 🐒🐒🐒");
        }

        System.out.println("Category: " + category);
        System.out.println("Query: " + query);
        if(category.equals("Todas") && query.isEmpty()){
            System.out.println("Todas y vacio");
            return skillService.getSkillList();
        }
        else if(category.equals("Todas") && !query.isEmpty()){
            System.out.println("Todas y no vacio");
            return skillService.getSkillsByQuery(query);
        }
        else if(!category.equals("Todas") && query.isEmpty()){
            System.out.println("No todas y vacio");
            return skillService.getSkillsByCategory(category);
        }
        else if(!category.equals("Todas") && !query.isEmpty()){
            System.out.println("No todas y no vacio");
            List<Skill> skillsByCategory = skillService.getSkillsByCategory(category);
            return skillsByCategory.stream()
                    .filter(skill -> skill.getTitle().contains(query))
                    .collect(Collectors.toList());
        }
        return skillService.getSkillList();
    }

    @GetMapping("/category/{category}")
    public List<Skill> getSkillsByCategory(@PathVariable("category") String category) {

        if(category.equals("Todas"))
            return skillService.getSkillList();

        return skillService.getSkillsByCategory(category);
    }
}
