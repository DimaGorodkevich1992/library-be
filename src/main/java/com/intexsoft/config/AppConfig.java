package com.intexsoft.config;

import com.intexsoft.controller.filter.AuthFilter;
import com.intexsoft.controller.filter.BookFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class AppConfig {
    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter() {
        FilterRegistrationBean<AuthFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new AuthFilter());
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


}
