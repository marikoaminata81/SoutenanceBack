package com.bezkoder.spring.login;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = "uploads";
        String uploadDir1= "uploads";

        Path uploadPath = Paths.get(uploadDir);
        Path uploadPath1 = Paths.get(uploadDir1);

        String uploadAbsolutePath = uploadPath.toFile().getAbsolutePath();
        String uploadAbsolutePath1 = uploadPath1.toFile().getAbsolutePath();

        registry.addResourceHandler( "/" + uploadDir + "/**")
                .addResourceLocations("file:" + uploadAbsolutePath + "/");

        registry.addResourceHandler( "/" + uploadDir1 + "/**")
                .addResourceLocations("file:" + uploadAbsolutePath1 + "/");
    }

}
