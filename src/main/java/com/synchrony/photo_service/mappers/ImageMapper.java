package com.synchrony.photo_service.mappers;

import com.synchrony.photo_service.models.images.Image;
import com.synchrony.photo_service.models.imgur.ImgurResponse;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper {
    public Image fromImgurResponse(ImgurResponse response, UUID userId) {
        return new Image(
            UUID.randomUUID(),
            userId,
            response.data().id(),
            Instant.EPOCH,
            Instant.EPOCH
        );
    }
}