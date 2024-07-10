package com.kayukin.reportviewer.service;

import com.kayukin.reportviewer.configuration.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.util.UriComponentsBuilder;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RequiredArgsConstructor
@Component
public class S3Service {
    private final S3Client s3Client;
    private final File downloadDirectory;
    private final ApplicationProperties applicationProperties;

    public List<String> list() {
        final var listObjectsResponse = s3Client.listObjects(ListObjectsRequest.builder()
                .bucket(applicationProperties.s3().bucket())
                .build());
        return listObjectsResponse.contents().stream()
                .map(S3Object::key)
                .toList();
    }

    public byte[] get(String key) {
        final var object =
                s3Client.getObjectAsBytes(builder -> builder.bucket(applicationProperties.s3().bucket()).key(key));
        return object.asByteArray();
    }

    @Cacheable("files")
    @SneakyThrows
    public String downloadAndUnpack(String key) {
        final var bytes = get(key);
        String indexUrl = null;
        try (var stream = new ZipInputStream(new ByteArrayInputStream(bytes))) {
            ZipEntry nextEntry;
            while ((nextEntry = stream.getNextEntry()) != null) {
                final var fileName = nextEntry.getName();
                if (fileName.contains("index.html")) {
                    indexUrl = formatUrl(key, fileName);
                }
                final var path = Paths.get(downloadDirectory.getPath(), hash(key), fileName);
                final var file = path.toFile();
                file.getParentFile().mkdirs();
                file.createNewFile();
                IOUtils.copy(stream, new FileOutputStream(file));
            }
        }
        return indexUrl;
    }

    private String formatUrl(String key, String fileName) {
        final var split = fileName.split("/");
        return UriComponentsBuilder.fromUriString(applicationProperties.apiBaseUrl())
                .replacePath("downloaded")
                .pathSegment(hash(key))
                .pathSegment(split)
                .toUriString();
    }

    @SneakyThrows
    private String hash(String key) {
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}