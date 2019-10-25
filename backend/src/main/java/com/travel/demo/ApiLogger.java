package com.travel.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiLogger extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ApiLogger.class);

    private ApiRequestRepository apiRequestRepository;

    public ApiLogger(ApiRequestRepository rep) {
        this.apiRequestRepository = rep;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestId = UUID.randomUUID().toString();
        log(request,response, requestId);
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        request.setAttribute("requestId", requestId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        long startTime = (Long)request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;

        String log_entry = request.getAttribute("requestId") + "," + request.getMethod() + "," + response.getStatus() + "," + request.getRequestURI() + "," + executeTime;
        logger.info(log_entry);

        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        if (queryString != null && queryString.length() > 0) {
            uri += "?" + queryString;
        }

        // this.repository.save(new ApiRequest((String)request.getAttribute("requestId"), uri, response.getStatus(), request.getMethod(), executeTime));
        this.apiRequestRepository.save(new ApiRequest(uri, response.getStatus(), request.getMethod(), executeTime));

    }

    private void log(HttpServletRequest request, HttpServletResponse response, String requestId) {
        // logger.info("requestId {}, host {}  HttpMethod: {}, URI : {}",requestId, request.getHeader("host"), request.getMethod(), request.getRequestURI() );
    }
}