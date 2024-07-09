package com.kayukin.reportviewer;

import com.kayukin.reportviewer.configuration.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(ApplicationProperties.class)
public class ReportViewerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReportViewerApplication.class, args);
    }
}