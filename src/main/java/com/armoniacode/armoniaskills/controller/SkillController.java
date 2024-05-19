package com.armoniacode.armoniaskills.controller;

import com.armoniacode.armoniaskills.entity.Skill;
import com.armoniacode.armoniaskills.service.SkillService;
import com.armoniacode.armoniaskills.util.JWTUtil;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/skill")
@RestController
public class SkillController {

    private final SkillService skillService;
    private final JWTUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

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

        logger.error(skill.toString());

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


}
