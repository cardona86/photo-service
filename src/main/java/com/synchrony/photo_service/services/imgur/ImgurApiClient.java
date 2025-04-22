package com.synchrony.photo_service.services.imgur;

import com.synchrony.photo_service.configs.FeignClientConfig;
import com.synchrony.photo_service.models.imgur.ImgurImage;
import com.synchrony.photo_service.models.imgur.ImgurResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
@FeignClient(name = "imgur-api-client", url = "https://api.imgur.com", configuration = FeignClientConfig.class)
public interface ImgurApiClient {

    @GetMapping(path = "/3/image/{imageHash}")
    ImgurResponse getImage(
        @PathVariable("imageHash") String imageHash
    );

    @PostMapping(path = "/3/image")
    ImgurResponse uploadImage(
        @RequestBody ImgurImage imgurImage
    );
}