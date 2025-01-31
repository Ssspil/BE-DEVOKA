package com.jspp.devoka.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${spring.security.cors.allowed-origins}")
    private String  ALLOW_CROSS_ORIGIN_DOMAIN;

    @Value("${spring.security.cors.allowed-methods}")
    private String[] ALLOW_METHODS;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("허용 도메인 : {}", ALLOW_CROSS_ORIGIN_DOMAIN);
        registry.addMapping("/**")
                .allowedOrigins(ALLOW_CROSS_ORIGIN_DOMAIN)
                .allowedMethods(ALLOW_METHODS)
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
