package com.armoniacode.armoniaskills.repository;

import com.armoniacode.armoniaskills.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SkillRepository extends JpaRepository<Skill, UUID> {

    Optional<Skill> findById(UUID id);

    List<Skill> findAllByUserID(UUID id);

    List<Skill> findAllByTitleContainingOrDescriptionContaining(String query, String query2);

    List<Skill> findAllByCategory(String category);

}
