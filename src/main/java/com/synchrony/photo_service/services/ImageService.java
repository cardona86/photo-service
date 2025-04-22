package com.synchrony.photo_service.services;

import com.synchrony.photo_service.exceptions.NotFoundException;
import com.synchrony.photo_service.exceptions.RateLimitException;
import com.synchrony.photo_service.exceptions.UnauthorizedException;
import com.synchrony.photo_service.mappers.ImageMapper;
import com.synchrony.photo_service.models.images.Image;
import com.synchrony.photo_service.models.imgur.ImgurImage;
import com.synchrony.photo_service.models.imgur.ImgurResponse;
import com.synchrony.photo_service.repos.ImageRepo;
import com.synchrony.photo_service.services.imgur.ImgurApiClient;
import com.synchrony.photo_service.services.kafka.KafkaProducerService;
import com.synchrony.photo_service.services.metrics.MetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ImageService {
    static Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final ImgurApiClient imgurApiClient;
    private final ImageRepo imageRepo;
    private final ImageMapper imageMapper;
    private final MetricsService metricsService;
    private final RestTemplate restTemplate;
    private final KafkaProducerService kafkaProducerService;

    public ImageService(ImgurApiClient imgurApiClient, ImageRepo imageRepo, ImageMapper imageMapper, MetricsService metricsService, RestTemplate restTemplate, KafkaProducerService kafkaProducerService) {
        this.imgurApiClient = imgurApiClient;
        this.imageRepo = imageRepo;
        this.imageMapper = imageMapper;
        this.metricsService = metricsService;
        this.restTemplate = restTemplate;
        this.kafkaProducerService = kafkaProducerService;
    }

    public byte[] getImage(String imageHash, Authentication authentication) {
        logger.debug("Getting image with hash {}", imageHash);
        try {
            // Fetch image metadata from Imgur
            ImgurResponse response = imgurApiClient.getImage(imageHash);
            String imageUrl = response.data().link();
            logger.debug("Image URL: {}", imageUrl);

            metricsService.increment(MetricsService.MetricType.UPLOAD_IMAGE);

            // Send message to Kafka
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            kafkaProducerService.sendMessage(userDetails.getUsername(), imageUrl);

            // Download the image using RestTemplate
            return restTemplate.getForObject(imageUrl, byte[].class);
        } catch (Exception ex) {
            logger.error("Failed to fetch or download image", ex);
            throw handleImgurExceptions(ex);
        }
    }

    public Image uploadImage(ImgurImage uploadRequest) {
        logger.debug("Uploading image with title: {}", uploadRequest.title());
        try {
            ImgurResponse response = imgurApiClient.uploadImage(uploadRequest);
            logger.debug("Image uploaded successfully: {}", response.data().link());

            Image savedImage = imageRepo.save(imageMapper.fromImgurResponse(response, uploadRequest.userId()));
            logger.debug("Image metadata stored successfully: {}", savedImage);

            metricsService.increment(MetricsService.MetricType.UPLOAD_IMAGE);

            return savedImage;
        } catch (Exception ex) {
            logger.error("Failed to upload image or store metadata", ex);
            throw new RuntimeException("Image upload successful, but failed to store image metadata", ex);
        }
    }

    private RuntimeException handleImgurExceptions(Throwable exception) {
        if (exception instanceof feign.FeignException feignException) {
            return switch (feignException.status()) {
                case 404 -> new NotFoundException("Image not found");
                case 401 -> new UnauthorizedException("Unauthorized access");
                case 429 -> new RateLimitException("Rate limit exceeded");
                case 500 -> new RuntimeException("Internal server error from Imgur API");
                default -> new RuntimeException("Unexpected error occurred");
            };
        }
        return new RuntimeException("Unknown error occurred", exception);
    }
}