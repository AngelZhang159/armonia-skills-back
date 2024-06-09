package com.armoniacode.armoniaskills.repository;

import com.armoniacode.armoniaskills.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SkillRepository extends JpaRepository<Skill, UUID> {

    Optional<Skill> findById(UUID id);

    List<Skill> findAllByUserID(UUID id);

    //List<Skill> findAllByTitleContainingOrDescriptionContaining(String query, String query2);

    List<Skill> findAllByCategory(String category);

    /*@Query("SELECT s FROM Skill s WHERE s.price BETWEEN :minPrice AND :maxPrice")
    List<Skill> findAllByPriceBetween(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    */
}
