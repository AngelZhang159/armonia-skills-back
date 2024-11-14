package com.armoniacode.armoniaskills.repository;

import com.armoniacode.armoniaskills.entity.StatusEnum;
import com.armoniacode.armoniaskills.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    Users findByEmail(String email);

    Optional<Users> findById(UUID uuid);

    Users findByUsername(String username);

    List<Users> findByStatus(StatusEnum status);
}