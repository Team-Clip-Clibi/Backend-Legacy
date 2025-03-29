package com.clip.global.config.security;

import com.clip.global.config.jwt.JwtAuthenticationFilter;
import com.clip.global.config.jwt.TokenProvider;
import com.clip.global.config.security.exclude.ExcludeAuthPathProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final ExcludeAuthPathProperties excludeAuthPathProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(request -> {
            excludeAuthPathProperties.getPaths().iterator()
                    .forEachRemaining(authPath ->
                            request.requestMatchers(HttpMethod.valueOf(authPath.getMethod()), authPath.getPathPattern()).permitAll()
                    );
            request.anyRequest().authenticated();
        });

        // Jwt 커스텀 필터 등록
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // Token Exception Handling
        http.exceptionHandling(except -> except
                .authenticationEntryPoint((request, response, authException) -> response.sendError(response.getStatus(), "토큰 오류"))
        );

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider, excludeAuthPathProperties);
    }

}
