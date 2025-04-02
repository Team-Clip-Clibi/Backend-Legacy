package com.clip.global.config;

import com.clip.global.security.CustomAuthenticationFailureHandler;
import com.clip.global.security.CustomAuthenticationSuccessHandler;
import com.clip.global.security.LoginAttemptFilter;
import com.clip.global.security.util.LoginAttemptManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationFailureHandler failureHandler;
    private final CustomAuthenticationSuccessHandler successHandler;
    private final LoginAttemptManager loginAttemptManager;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                "/office/admin/login",
                                "/office/admin/register",
                                "/css/**", "/js/**", "/icon/**", "/images/**"
                        ).permitAll()
                        .anyRequest().authenticated());

        http.addFilterBefore(loginAttemptFilter(loginAttemptManager), UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form
                        .loginPage("/office/admin/login")
                        .loginProcessingUrl("/office/admin/login")
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/office/admin/logout"))
                        .logoutSuccessUrl("/office/admin/login")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                );
        return http.build();
    }

    @Bean
    public LoginAttemptFilter loginAttemptFilter(LoginAttemptManager loginAttemptManager) {
        return new LoginAttemptFilter(loginAttemptManager);
    }
}
