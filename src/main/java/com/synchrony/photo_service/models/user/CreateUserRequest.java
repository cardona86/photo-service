package com.synchrony.photo_service.models.user;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public record CreateUserRequest(
    @NotNull
    String username,
    @NotNull
    String password,
    @NotNull
    String email
) implements Serializable { }