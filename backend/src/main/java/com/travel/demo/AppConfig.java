package com.travel.demo;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@EnableJpaRepositories
public class AppConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private ApiRequestRepository rep;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new ApiLogger(this.rep)).addPathPatterns("/*");
    }
}