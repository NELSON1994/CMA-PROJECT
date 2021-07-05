package com.cma.cmaproject.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//@Configuration
//@ComponentScan(basePackages = "com.cma.cmaproject.controllers")
//@EnableWebMvc
public class WebConfig{
  //  public class WebConfig implements WebMvcConfigurer {
//    @Value("${cors}")
//    String cors;
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins(cors).allowedMethods("GET", "POST", "OPTIONS", "PUT" ,"DELETE")
//                .allowedHeaders("Content-Type", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers")
//                .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
//                .allowCredentials(false).maxAge(3600);
//    }

}