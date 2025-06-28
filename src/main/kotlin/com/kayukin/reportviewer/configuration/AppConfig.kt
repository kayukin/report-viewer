package com.kayukin.reportviewer.configuration

import com.kayukin.reportviewer.configuration.ApplicationProperties.S3AuthType.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider
import software.amazon.awssdk.services.s3.S3Client
import java.io.File
import java.net.URI

@Configuration
class AppConfig(private val applicationProperties: ApplicationProperties) {
    @Bean
    fun s3Client(): S3Client {
        val s3 = applicationProperties.s3
        val provider = when (s3.authType) {
            STATIC -> StaticCredentialsProvider.create(
                AwsBasicCredentials.create(s3.accessKey, s3.secretKey)
            )

            PROFILE -> ProfileCredentialsProvider.create(s3.profile)
            IRSA -> WebIdentityTokenFileCredentialsProvider.create()
            DEFAULT -> null
        }

        val builder = S3Client.builder()
            .credentialsProvider(provider)
            .region(s3.region)

        if (s3.endpoint?.isNotBlank() ?: false) {
            builder.endpointOverride(URI.create(s3.endpoint))
                .forcePathStyle(true)
        }
        return builder
            .build()
    }

    @Bean
    fun downloadFolder(): File {
        return File(applicationProperties.downloadLocation)
    }
}