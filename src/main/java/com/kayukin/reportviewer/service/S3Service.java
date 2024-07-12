package com.kayukin.reportviewer.service;

import com.google.common.collect.Streams;
import com.kayukin.reportviewer.configuration.ApplicationProperties;
import com.kayukin.reportviewer.configuration.CacheConfig;
import com.kayukin.reportviewer.dto.Report;
import com.kayukin.reportviewer.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.kayukin.reportviewer.configuration.CacheConfig.FILES_CACHE;
import static com.kayukin.reportviewer.configuration.StaticConfigurer.FILES_ENDPOINT;

@RequiredArgsConstructor
@Component
public class S3Service {
    private static final String INDEX_HTML = "index.html";

    private final S3Client s3Client;
    private final File downloadDirectory;
    private final ApplicationProperties applicationProperties;
    private final CacheConfig.NameHashGenerator nameHashGenerator;

    public List<Report> list() {
        final var listObjectsResponse = s3Client.listObjects(ListObjectsRequest.builder()
                .bucket(applicationProperties.s3().bucket())
                .build());
        return Streams.mapWithIndex(listObjectsResponse.contents().stream(),
                        (s3Object, index) -> Report.builder()
                                .id(String.valueOf(index))
                                .name(s3Object.key())
                                .build())
                .toList();
    }

    public byte[] get(String key) {
        final var object =
                s3Client.getObjectAsBytes(builder -> builder.bucket(applicationProperties.s3().bucket()).key(key));
        return object.asByteArray();
    }

    @Cacheable(value = FILES_CACHE, keyGenerator = "keyGenerator")
    @SneakyThrows
    public String downloadAndUnpack(String key) {
        final var bytes = get(key);
        String indexUrl = null;
        try (var stream = new ZipInputStream(new ByteArrayInputStream(bytes))) {
            ZipEntry nextEntry;
            while ((nextEntry = stream.getNextEntry()) != null) {
                final var fileName = nextEntry.getName();
                if (fileName.contains(INDEX_HTML)) {
                    indexUrl = formatUrl(key, fileName);
                }
                final var path = Paths.get(downloadDirectory.getPath(), hash(key), fileName);
                final var file = path.toFile();
                FileUtils.createFileWithDirs(file);
                try (var out = new FileOutputStream(file)) {
                    stream.transferTo(out);
                }
            }
        }
        return indexUrl;
    }

    private String formatUrl(String key, String fileName) {
        final var split = fileName.split("/");
        return UriComponentsBuilder.fromUriString(applicationProperties.apiBaseUrl())
                .replacePath(FILES_ENDPOINT)
                .pathSegment(hash(key))
                .pathSegment(split)
                .toUriString();
    }

    @SneakyThrows
    private String hash(String key) {
        return nameHashGenerator.hash(key);
    }
}