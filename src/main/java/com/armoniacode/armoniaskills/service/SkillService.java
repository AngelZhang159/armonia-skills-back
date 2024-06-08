package com.armoniacode.armoniaskills.service;

import com.armoniacode.armoniaskills.entity.Skill;
import com.armoniacode.armoniaskills.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> getSkillList() {
        return skillRepository.findAll();
    }

    public Skill postSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public Skill getSkillById(UUID id) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new RuntimeException("The skill with id: " + id + " doesn't exist"));
        return skill;
    }

    public Skill updateSkillById(UUID id, Skill skillUpdated) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new RuntimeException("The skill with id: " + id + " doesn't exist"));

        skill = Skill.builder()
                .id(skill.getId())
                .title(skillUpdated.getTitle() != null ? skillUpdated.getTitle() : skill.getTitle())
                .description(skillUpdated.getDescription() != null ? skillUpdated.getDescription() : skill.getDescription())
                .category(skillUpdated.getCategory() != null ? skillUpdated.getDescription() : skill.getDescription())
                .price(skillUpdated.getPrice() != null ? skillUpdated.getPrice() : skill.getPrice())
                .location(skillUpdated.getPrice() != null ? skillUpdated.getLocation() : skill.getLocation())
                .userID(skill.getUserID())
                .imageList(skillUpdated.getImageList() != null ? skillUpdated.getImageList() : skill.getImageList())
                .build();

        return skillRepository.save(skill);
    }

    public void deleteSkill(UUID id) {
        skillRepository.deleteById(id);
    }

    public List<Skill> getSkillsByQuery(String query) {
        return skillRepository.findAllByTitleContainingOrDescriptionContaining(query, query);
    }

    public List<Skill> getSkillsByCategoryAndTitle(String category, String query) {

        List<Skill> skillsByCategory = skillRepository.findAllByCategory(category);

        // Filtra los resultados para incluir solo las skills cuyo título contenga la cadena de título proporcionada

        return skillsByCategory.stream()
                .filter(skill -> skill.getTitle().contains(query))
                .collect(Collectors.toList());
    }


    public List<Skill> getSkillsByCategory(String category) {
        System.out.println("Buscando habilidades por categoría: " + category);
        List<Skill> skills = skillRepository.findAllByCategory(category);
        System.out.println("Habilidades encontradas: " + skills);
        return skills;
    }
}
