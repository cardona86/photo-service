package com.synchrony.photo_service.configs;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor authRequestInterceptor(@Value("${imgur.client.token}") String token) {
        return requestTemplate ->
            requestTemplate.header("Authorization", "Bearer " + token);
    }
}