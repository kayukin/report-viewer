package com.kayukin.reportviewer.service

import com.kayukin.reportviewer.utils.FileUtils
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import java.io.File

@Component
class DownloadFolderInitializer(private val downloadFolder: File) : ApplicationListener<ApplicationReadyEvent> {

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        if (downloadFolder.exists()) {
            FileUtils.deleteDirectoryContents(downloadFolder.toPath())
        } else {
            check(downloadFolder.mkdirs()) { "Failed to create directory: " + downloadFolder.absolutePath }
        }
    }
}