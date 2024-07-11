package com.kayukin.reportviewer.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import software.amazon.awssdk.regions.Region;

@ConfigurationProperties(prefix = "reporting")
public record ApplicationProperties(
        S3Configuration s3,
        String downloadLocation,
        String apiBaseUrl
) {
    public record S3Configuration(
            String bucket,
            Region region,
            S3AuthType authType,
            String profile,
            String accessKey,
            String secretKey
    ) {
    }

    public enum S3AuthType {
        STATIC,
        PROFILE,
        IRSA,
        DEFAULT
    }
}