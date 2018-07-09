package com.intexsoft.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intexsoft.controller.filter.AuthFilter;
import com.intexsoft.controller.filter.BookFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class AppConfig {

    @Value("${application.api.key}")
    private String apiKey;

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter() {
        FilterRegistrationBean<AuthFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new AuthFilter());
        filter.addInitParameter("apiKey", apiKey);
        filter.addUrlPatterns("/api/*");
        return filter;
    }

    @Bean
    public FilterRegistrationBean<BookFilter> bookFilter() {
        FilterRegistrationBean<BookFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new BookFilter());
        filter.addUrlPatterns("/api/v2/client/book/*");
        filter.addServletNames("BookServlet");
        return filter;
    }

    @Autowired
    private LettuceConnectionFactory lettuceConnectionFactory;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory);
        template.setDefaultSerializer(new JdkSerializationRedisSerializer());
        template.setKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}