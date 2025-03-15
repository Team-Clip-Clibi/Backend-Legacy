package com.clip.global.config.security.exclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties("exclude-auth-path-patterns")
public class ExcludeAuthPathProperties {
    private final List<AuthPath> paths;

    public List<String> getExcludeAuthPaths() {
        return paths.stream().map(AuthPath::getPathPattern).toList();
    }

    @Getter
    @AllArgsConstructor
    public static class AuthPath {
        private String pathPattern;
        private String method;
    }
}
