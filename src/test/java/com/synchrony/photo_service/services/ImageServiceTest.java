package com.synchrony.photo_service.services;

import com.synchrony.photo_service.exceptions.NotFoundException;
import com.synchrony.photo_service.mappers.ImageMapper;
import com.synchrony.photo_service.models.images.Image;
import com.synchrony.photo_service.models.imgur.ImgurImage;
import com.synchrony.photo_service.models.imgur.ImgurResponse;
import com.synchrony.photo_service.repos.ImageRepo;
import com.synchrony.photo_service.services.imgur.ImgurApiClient;
import com.synchrony.photo_service.services.kafka.KafkaProducerService;
import com.synchrony.photo_service.services.metrics.MetricsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    RestTemplate mockRestTemplate = mock(RestTemplate.class);

    @Mock
    private ImgurApiClient imgurApiClient;

    @Mock
    private ImageRepo imageRepo;

    @Mock
    private ImageMapper imageMapper;

    @Mock
    private MetricsService metricsService;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private ImageService imageService;

    @Test
    void testGetImage_Success() {
        // Setup
        String imageHash = "testHash";
        String imageUrl = "http://example.com/image.jpg";
        byte[] imageBytes = new byte[]{1, 2, 3};

        // Mock Authentication and UserDetails
        Authentication mockAuthentication = mock(Authentication.class);
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockUserDetails.getUsername()).thenReturn("testUser");

        // Mock ImgurApiClient and RestTemplate
        when(imgurApiClient.getImage(imageHash)).thenReturn(null);
        when(mockRestTemplate.getForObject(imageUrl, byte[].class)).thenReturn(imageBytes);
        ImgurResponse mockResponse = mock(ImgurResponse.class);
        ImgurResponse.Data mockData = mock(ImgurResponse.Data.class);
        when(mockResponse.data()).thenReturn(mockData);
        when(mockData.link()).thenReturn(imageUrl);
        when(imgurApiClient.getImage(imageHash)).thenReturn(mockResponse);
        when(mockRestTemplate.getForObject(imageUrl, byte[].class)).thenReturn(imageBytes);

        // Mock Kafka Producer
        doNothing().when(kafkaProducerService).sendMessage("testUser", imageUrl);

        // Execute
        byte[] result = imageService.getImage(imageHash, mockAuthentication);

        // Verify
        assertArrayEquals(imageBytes, result);
        verify(imgurApiClient, times(1)).getImage(imageHash);
    }

    @Test
    void testUploadImage_Success() {
        // Setup
        ImgurImage uploadRequest = new ImgurImage(null, "BASE64", null, "title", "description");
        ImgurResponse mockResponse = mock(ImgurResponse.class);
        ImgurResponse.Data mockData = mock(ImgurResponse.Data.class);
        Image mockImage = mock(Image.class);

        // Mocking
        when(imgurApiClient.uploadImage(uploadRequest)).thenReturn(mockResponse);
        when(mockResponse.data()).thenReturn(mockData);
        when(mockData.link()).thenReturn("http://example.com/image.jpg");
        when(imageMapper.fromImgurResponse(mockResponse, null)).thenReturn(mockImage);
        when(imageRepo.save(mockImage)).thenReturn(mockImage);

        // Execute
        Image result = imageService.uploadImage(uploadRequest);

        // Verify
        assertNotNull(result);
        InOrder inOrder = inOrder(imgurApiClient, imageMapper, imageRepo, metricsService);
        inOrder.verify(imgurApiClient, times(1)).uploadImage(uploadRequest);
        inOrder.verify(imageMapper, times(1)).fromImgurResponse(mockResponse, null);
        inOrder.verify(imageRepo, times(1)).save(mockImage);
        inOrder.verify(metricsService, times(1)).increment(MetricsService.MetricType.UPLOAD_IMAGE);
    }
}