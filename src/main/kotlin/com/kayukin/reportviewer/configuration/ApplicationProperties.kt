package com.kayukin.reportviewer.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import software.amazon.awssdk.regions.Region

@ConfigurationProperties(prefix = "reporting")
data class ApplicationProperties(
    val s3: S3Configuration,
    val downloadLocation: String,
    val apiBaseUrl: String
) {
    data class S3Configuration(
        val endpoint: String?,
        val bucket: String,
        val region: Region,
        val authType: S3AuthType,
        val profile: String?,
        val accessKey: String?,
        val secretKey: String?
    )

    enum class S3AuthType {
        STATIC,
        PROFILE,
        IRSA,
        DEFAULT
    }
}