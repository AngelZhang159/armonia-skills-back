package com.armoniacode.armoniaskills.repository;

import com.armoniacode.armoniaskills.entity.StatusEnum;
import com.armoniacode.armoniaskills.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    Optional<User> findById(UUID uuid);

    User findByUsername(String username);

    List<User> findByStatus(StatusEnum status);
}