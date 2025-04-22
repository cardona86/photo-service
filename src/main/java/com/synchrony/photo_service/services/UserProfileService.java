package com.synchrony.photo_service.services;

import com.synchrony.photo_service.exceptions.BadRequestException;
import com.synchrony.photo_service.exceptions.NotFoundException;
import com.synchrony.photo_service.exceptions.UnauthorizedException;
import com.synchrony.photo_service.models.images.Image;
import com.synchrony.photo_service.models.user.*;
import com.synchrony.photo_service.repos.ImageRepo;
import com.synchrony.photo_service.repos.UserProfileRepo;
import com.synchrony.photo_service.services.metrics.MetricsService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserProfileService  {
    private final UserProfileRepo userProfileRepo;
    private final ImageRepo imageRepo;
    private final MetricsService metricsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserProfileService(UserProfileRepo userProfileRepo, ImageRepo imageRepo, MetricsService metricsService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userProfileRepo = userProfileRepo;
        this.imageRepo = imageRepo;
        this.metricsService = metricsService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public UserProfile createUser(CreateUserRequest createUserRequest) {
        final String hashedPassword = passwordEncoder.encode(createUserRequest.password());
        final UserProfile userProfile = new UserProfile(
            UUID.randomUUID(),
            createUserRequest.username(),
            hashedPassword,
            createUserRequest.email(),
            Instant.EPOCH,
            Instant.EPOCH,
            true
        );

        Optional<UserProfile> profile = userProfileRepo.findByUsername(createUserRequest.username());
        if (profile.isPresent()) {
            throw new BadRequestException("User already exists");
        } else {
            UserProfile savedUserProfile = userProfileRepo.save(userProfile);
            metricsService.increment(MetricsService.MetricType.CREATE_USER);

            return savedUserProfile;
        }
    }

    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Fetch the UserProfile from the database
            UserProfile userProfile = userProfileRepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

            // Check if active
            if (!userProfile.getActive()) {
                throw new UnauthorizedException("User is not active");
            }

            // Generate JWT token
            String token = JwtUtil.generateToken(userProfile.getUsername());

            return new LoginResponse(userProfile.getId(), token);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    public UserWithImages getUserWithImages(UUID userId) {
        if (userId == null) {
            throw new BadRequestException("Username is required");
        }

        Optional<UserProfile> user = userProfileRepo.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        List<Image> images = imageRepo.findByUserId(user.get().getId());
        return new UserWithImages(user.get(), images);
    }
}
