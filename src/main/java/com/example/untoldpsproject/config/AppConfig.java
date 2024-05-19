package com.example.untoldpsproject.config;

import com.example.untoldpsproject.factory.ArtistFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.Map;

@Configuration
public class AppConfig {
    @Bean
    public ArtistFactory artistFactory(){
        return new ArtistFactory();
    }
}
