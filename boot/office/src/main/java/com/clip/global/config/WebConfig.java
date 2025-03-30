package com.clip.global.config;

import com.clip.global.security.LoginAttemptFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.servlet.Filter;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class WebConfig {

    @Bean
    public LoginAttemptFilter loginAttemptFilterInstance(StringRedisTemplate redisTemplate) {
        return new LoginAttemptFilter(redisTemplate);
    }

    @Bean
    public FilterRegistrationBean<Filter> loginAttemptFilter(LoginAttemptFilter loginAttemptFilterInstance) {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(loginAttemptFilterInstance);
        registrationBean.addUrlPatterns("/office/admin/login");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}