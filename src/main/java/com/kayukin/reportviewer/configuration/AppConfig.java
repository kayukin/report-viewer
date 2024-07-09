package com.kayukin.reportviewer.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.File;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final ApplicationProperties applicationProperties;

    @Bean
    public S3Client s3Client() {
        final var s3 = applicationProperties.s3();
        final var credentials = AwsBasicCredentials.create(s3.accessKey(), s3.secretKey());
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(s3.region())
                .build();
    }

    @Bean
    public File downloadFolder() {
        return new File(applicationProperties.downloadLocation());
    }
}