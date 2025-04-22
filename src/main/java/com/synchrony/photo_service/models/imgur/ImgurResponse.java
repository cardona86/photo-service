package com.synchrony.photo_service.models.imgur;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ImgurResponse(
    int status,
    boolean success,
    Data data
) {
    public record Data(
        String id,
        String link
    ) { }
}