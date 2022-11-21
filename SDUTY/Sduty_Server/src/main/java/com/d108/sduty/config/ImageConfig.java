package com.d108.sduty.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImageConfig implements WebMvcConfigurer{
    private String connectPath = "/image/**";
    //private String resourcePath = "file:////home/ubuntu/docker-volume/jenkins/workspace/k205sduty/SDUTY/Sduty_Server/src/main/resources/image/";
    private String resourcePath = "file:////home/files/profile";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(connectPath)
                .addResourceLocations(resourcePath);
    }
}
