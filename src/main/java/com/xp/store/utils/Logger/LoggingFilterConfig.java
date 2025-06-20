package com.xp.store.utils.Logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class LoggingFilterConfig {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilterConfig.class);

    @Bean
    public OncePerRequestFilter loggingFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                try {
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    logger.error("Erro não tratado na requisição: " + request.getRequestURI(), e);
                    throw e;
                }
            }
        };
    }
}
