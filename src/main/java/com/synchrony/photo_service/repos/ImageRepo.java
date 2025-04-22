package com.synchrony.photo_service.repos;

import com.synchrony.photo_service.models.images.Image;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepo extends JpaRepository<Image, UUID> {
    List<Image> findByUserId(UUID userId);
}