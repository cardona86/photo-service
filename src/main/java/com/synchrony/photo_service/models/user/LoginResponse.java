package com.synchrony.photo_service.models.user;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class LoginResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID userId;

    private String token;

    public LoginResponse() { }

    public LoginResponse(UUID userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
