package com.kayukin.reportviewer.service;

import com.kayukin.reportviewer.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;

@RequiredArgsConstructor
@Component
public class DownloadFolderInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final File downloadFolder;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (downloadFolder.exists()) {
            FileUtils.deleteDirectoryContents(downloadFolder.toPath());
        } else {
            if (!downloadFolder.mkdirs()) {
                throw new IllegalStateException("Failed to create directory: " + downloadFolder.getAbsolutePath());
            }
        }
    }
}
