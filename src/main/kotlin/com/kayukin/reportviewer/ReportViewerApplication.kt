package com.kayukin.reportviewer

import com.kayukin.reportviewer.configuration.ApplicationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(ApplicationProperties::class)
class ReportViewerApplication

fun main(args: Array<String>) {
    runApplication<ReportViewerApplication>(*args)
}