package com.synchrony.photo_service.models.user;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public class LoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String username;

    @NotNull
    private String password;

    public LoginRequest() {}

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
