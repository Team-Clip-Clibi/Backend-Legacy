package com.clip.infra.aws.s3.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class S3PathProperties {
    @Value("${cloud.aws.s3.banner_img}")
    private String BANNER_IMG_PATH;
}
