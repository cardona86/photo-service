package com.synchrony.photo_service.controllers;

import com.synchrony.photo_service.models.images.Image;
import com.synchrony.photo_service.models.imgur.ImgurImage;
import com.synchrony.photo_service.services.ImageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/images")
@Validated
public class ImageController {

    final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<byte[]> getImage(
        @RequestParam String imageHash,
        Authentication authentication
    ) {
        byte[] imageBytes = imageService.getImage(imageHash, authentication);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
            .body(imageBytes);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Image uploadImage(@Valid @RequestBody ImgurImage imgurImage) {
        return imageService.uploadImage(imgurImage);
    }
}
