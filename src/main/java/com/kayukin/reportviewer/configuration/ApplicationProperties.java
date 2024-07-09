package com.kayukin.reportviewer.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import software.amazon.awssdk.regions.Region;

@ConfigurationProperties(prefix = "reporting")
public record ApplicationProperties(S3Configuration s3, String downloadLocation) {
    public record S3Configuration(String bucket, Region region, String accessKey, String secretKey) {
    }
}