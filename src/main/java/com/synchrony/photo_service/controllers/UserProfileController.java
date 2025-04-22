package com.synchrony.photo_service.controllers;

import com.synchrony.photo_service.models.user.*;
import com.synchrony.photo_service.services.UserProfileService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class UserProfileController {
    final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfile registerUser(@RequestBody CreateUserRequest createUserRequest) {
        return userProfileService.createUser(createUserRequest);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return userProfileService.login(loginRequest);
    }

    @GetMapping("/{userId}")
    public UserWithImages getUserAndImages(@PathVariable UUID userId) {
        return userProfileService.getUserWithImages(userId);
    }
}
