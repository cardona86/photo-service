package com.synchrony.photo_service.models.user;

import java.io.Serial;
import java.io.Serializable;
import org.springframework.data.web.JsonPath;

public record AuthTokenResponse(
    @JsonPath("access_token")
    String token
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
