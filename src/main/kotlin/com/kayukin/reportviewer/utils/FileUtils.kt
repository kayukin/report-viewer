package com.kayukin.reportviewer.utils

import com.google.common.io.MoreFiles
import com.google.common.io.RecursiveDeleteOption
import java.io.File
import java.nio.file.Path

object FileUtils {
    fun deleteRecursively(path: Path) {
        MoreFiles.deleteRecursively(path, RecursiveDeleteOption.ALLOW_INSECURE)
    }

    fun deleteDirectoryContents(path: Path) {
        MoreFiles.deleteDirectoryContents(path, RecursiveDeleteOption.ALLOW_INSECURE)
    }

    fun createFileWithDirs(file: File) {
        file.getParentFile().mkdirs()
        file.createNewFile()
    }
}