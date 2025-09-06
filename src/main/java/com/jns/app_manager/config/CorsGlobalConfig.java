package com.jns.app_manager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsGlobalConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // todas as rotas
                .allowedOrigins("*") // qualquer origem
                .allowedMethods("*") // qualquer método HTTP
                .allowedHeaders("*") // qualquer cabeçalho
                .allowCredentials(false); // sem cookies/autenticação
    }
}