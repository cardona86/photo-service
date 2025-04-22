package com.synchrony.photo_service.models.user;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.web.JsonPath;

public record AuthTokenRequest(
    @NotNull
    @JsonPath("username")
    String username,
    @NotNull
    @JsonPath("password")
    String password
) {}
