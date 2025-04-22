package com.synchrony.photo_service.models.imgur;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ImgurImage(
    @NotNull
    UUID userId,
    @NotNull
    String image,
    ImageType imageType,
    String title,
    String description
) { }
