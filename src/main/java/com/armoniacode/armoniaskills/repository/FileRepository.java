package com.armoniacode.armoniaskills.repository;

import com.armoniacode.armoniaskills.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileRepository extends JpaRepository<Image, UUID>{
}
