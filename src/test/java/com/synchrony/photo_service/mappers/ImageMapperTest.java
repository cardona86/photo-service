package com.synchrony.photo_service.mappers;

import com.synchrony.photo_service.models.images.Image;
import com.synchrony.photo_service.models.imgur.ImgurResponse;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageMapperTest {

    @Test
    void testFromImgurResponse() {
        // Setup
        ImageMapper imageMapper = new ImageMapper();
        UUID userId = UUID.randomUUID();
        String imgurId = "IMGUR_ID";

        ImgurResponse.Data mockData = mock(ImgurResponse.Data.class);
        when(mockData.id()).thenReturn(imgurId);

        ImgurResponse mockResponse = mock(ImgurResponse.class);
        when(mockResponse.data()).thenReturn(mockData);

        // Execute
        Image result = imageMapper.fromImgurResponse(mockResponse, userId);

        // Verify
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(imgurId, result.getImgurImageId());
        assertNotNull(result.getId());
        assertEquals(Instant.EPOCH, result.getCreatedAt());
        assertEquals(Instant.EPOCH, result.getUpdatedAt());
    }
}