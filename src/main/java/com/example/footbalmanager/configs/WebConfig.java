package com.example.footbalmanager.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String playersPhotoPath = "file:///" + System.getProperty("user.dir") + File.separator + "playersPhoto" + File.separator;
        String clubsPhotoPath = "file:///" + System.getProperty("user.dir") + File.separator + "clubsPhoto" + File.separator;
        String userPhotoPath = "file:///" + System.getProperty("user.dir") + File.separator + "usersPhoto" + File.separator;

        registry.addResourceHandler("/players/photo/**")
                .addResourceLocations(playersPhotoPath);
        registry.addResourceHandler("/clubs/photo/**")
                .addResourceLocations(clubsPhotoPath);
        registry.addResourceHandler("/admin/users/photo/**")
                .addResourceLocations(userPhotoPath);
    }
}
