package com.armoniacode.armoniaskills.controller;

import com.armoniacode.armoniaskills.entity.Skill;
import com.armoniacode.armoniaskills.service.SkillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/skill")
@RestController
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public List<Skill> getSkills(){
        return skillService.getSkillList();
    }

    @PostMapping
    public ResponseEntity<Skill> postSkill(@RequestBody Skill skill){
        return new ResponseEntity<>(skillService.postSkill(skill), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public Skill getSkillById(@PathVariable("id")UUID id){
        return skillService.getSkillById(id);
    }

    @PutMapping("/{id}")
    public Skill updateSkillById(@PathVariable("id") UUID id, @RequestBody Skill skill){
        return  skillService.updateSkillById(id, skill);
    }

    @DeleteMapping("/{id}")
    public String deleteSkill(@PathVariable("id") UUID id){
        skillService.deleteSkill(id);
        return "Skill with id: " + id + " deleted";
    }



}
