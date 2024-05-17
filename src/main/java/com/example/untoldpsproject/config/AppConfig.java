package com.example.untoldpsproject.config;

import com.example.untoldpsproject.factory.ArtistFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ArtistFactory artistFactory(){
        return new ArtistFactory();
    }
}
