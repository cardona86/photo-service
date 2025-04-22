package com.synchrony.photo_service.repos;

import com.synchrony.photo_service.models.user.UserProfile;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findByUsername(String username);
}
