package com.clip.global.config;

import com.clip.global.security.LoginAttemptFilter;
import com.clip.global.security.util.LoginAttemptManager;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public LoginAttemptFilter loginAttemptFilterInstance(LoginAttemptManager loginAttemptManager) {
        return new LoginAttemptFilter(loginAttemptManager);
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