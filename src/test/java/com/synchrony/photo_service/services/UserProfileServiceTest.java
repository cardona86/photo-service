package com.synchrony.photo_service.services;

import com.synchrony.photo_service.exceptions.BadRequestException;
import com.synchrony.photo_service.models.user.CreateUserRequest;
import com.synchrony.photo_service.models.user.UserProfile;
import com.synchrony.photo_service.repos.UserProfileRepo;
import com.synchrony.photo_service.services.metrics.MetricsService;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserProfileRepo userProfileRepo;

    @Mock
    private MetricsService metricsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserProfileService userProfileService;

    @Test
    void testCreateUser() {
        // Setup
        CreateUserRequest createUserRequest = new CreateUserRequest("testUser", "password", "test@example.com");
        UserProfile savedUserProfile = new UserProfile(UUID.randomUUID(), "testUser", "hashedPassword", "test@example.com", Instant.now(), Instant.now(), true);

        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
        when(userProfileRepo.findByUsername(createUserRequest.username())).thenReturn(Optional.empty());
        when(userProfileRepo.save(any(UserProfile.class))).thenReturn(savedUserProfile);

        // Execute
        UserProfile result = userProfileService.createUser(createUserRequest);

        // Verify
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertEquals("hashedPassword", result.getPassword());
        assertEquals("test@example.com", result.getEmail());

        InOrder inOrder = inOrder(userProfileRepo, metricsService);
        inOrder.verify(userProfileRepo, times(1)).findByUsername(createUserRequest.username());
        inOrder.verify(userProfileRepo, times(1)).save(any(UserProfile.class));
        inOrder.verify(metricsService, times(1)).increment(MetricsService.MetricType.CREATE_USER);
        verifyNoMoreInteractions(userProfileRepo, metricsService);
    }

    @Test
    void testCreateUser_userAlreadyExists() {
        // Setup
        CreateUserRequest createUserRequest = new CreateUserRequest("testUser", "password", "test@example.com");
        UserProfile existingUserProfile = new UserProfile(UUID.randomUUID(), "testUser", "hashedPassword", "test@example.com", Instant.now(), Instant.now(), true);

        when(userProfileRepo.findByUsername(createUserRequest.username())).thenReturn(Optional.of(existingUserProfile));

        // Execute & Verify
        BadRequestException exception = assertThrows(BadRequestException.class, () -> userProfileService.createUser(createUserRequest));
        assertEquals("User already exists", exception.getMessage());

        verify(userProfileRepo, times(1)).findByUsername(createUserRequest.username());
        verifyNoMoreInteractions(userProfileRepo, metricsService);
    }
}