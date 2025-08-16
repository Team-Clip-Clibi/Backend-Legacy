package com.clip.api.version.controller;

import com.clip.api.version.dto.AppVersionResponse;
import com.clip.meta.entity.OSType;
import com.clip.meta.repository.AppVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppVersionController {
    private final AppVersionRepository appVersionRepository;

    @GetMapping("/app/version/{type}")
    public AppVersionResponse getMinimumVersion(@PathVariable OSType type) {
        return new AppVersionResponse(appVersionRepository.findMinVersion(type));
    }
}
