package com.intexsoft.config;

import com.intexsoft.controller.filter.AuthFilter;
import com.intexsoft.controller.filter.BookFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableScheduling
@EnableCaching
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

   /* @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(new LettuceConnectionFactory());
        template.setDefaultSerializer(new JdkSerializationRedisSerializer());
        template.setKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }*/



    

    @Bean
    public CacheManager cacheManager() {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new JdkSerializationRedisSerializer()));
        return RedisCacheManager.builder(new JedisConnectionFactory())
                .cacheDefaults(cacheConfiguration)
                .build();
    }
}