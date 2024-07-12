package com.kayukin.reportviewer.utils;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.nio.file.Path;

@UtilityClass
public class FileUtils {
    @SneakyThrows
    public static void deleteRecursively(Path path) {
        MoreFiles.deleteRecursively(path, RecursiveDeleteOption.ALLOW_INSECURE);
    }

    @SneakyThrows
    public static void deleteDirectoryContents(Path path) {
        MoreFiles.deleteDirectoryContents(path, RecursiveDeleteOption.ALLOW_INSECURE);
    }

    @SneakyThrows
    public static void createFileWithDirs(File file) {
        file.getParentFile().mkdirs();
        file.createNewFile();
    }
}
