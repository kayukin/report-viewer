package com.kayukin.reportviewer.service

import com.google.common.collect.Streams
import com.kayukin.reportviewer.configuration.ApplicationProperties
import com.kayukin.reportviewer.configuration.CacheConfig
import com.kayukin.reportviewer.configuration.StaticConfigurer
import com.kayukin.reportviewer.dto.Report
import com.kayukin.reportviewer.utils.FileUtils
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.ListObjectsRequest
import software.amazon.awssdk.services.s3.model.S3Object
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

@Component
class S3Service(
    private val s3Client: S3Client,
    private val downloadDirectory: File,
    private val applicationProperties: ApplicationProperties,
    private val nameHashGenerator: CacheConfig.NameHashGenerator
) {
    fun list(): MutableList<Report?> {
        val listObjectsResponse = s3Client.listObjects(
            ListObjectsRequest.builder()
                .bucket(applicationProperties.s3.bucket)
                .build()
        )
        return Streams.mapWithIndex<S3Object?, Report?>(
            listObjectsResponse.contents().stream(),
            Streams.FunctionWithIndex { s3Object: S3Object?, index: Long ->
                Report(
                    index.toString(),
                    s3Object!!.key()
                )
            })
            .toList()
    }

    fun get(key: String?): ByteArray {
        val obj =
            s3Client.getObjectAsBytes({ builder ->
                builder.bucket(
                    applicationProperties.s3.bucket
                ).key(key)
            })
        return obj.asByteArray()
    }

    @Cacheable(value = ["files"], keyGenerator = "keyGenerator")
    fun downloadAndUnpack(key: String): String? {
        val bytes = get(key)
        var indexUrl: String? = null
        ZipInputStream(ByteArrayInputStream(bytes)).use { stream ->
            var nextEntry: ZipEntry?
            while ((stream.getNextEntry().also { nextEntry = it }) != null) {
                val fileName = nextEntry!!.getName()
                if (fileName.contains(INDEX_HTML)) {
                    indexUrl = formatUrl(key, fileName)
                }
                val path = Paths.get(downloadDirectory.getPath(), hash(key), fileName)
                val file = path.toFile()
                FileUtils.createFileWithDirs(file)
                FileOutputStream(file).use { out ->
                    stream.transferTo(out)
                }
            }
        }
        return indexUrl
    }

    private fun formatUrl(key: String, fileName: String): String {
        val split: Array<String?> = fileName.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return UriComponentsBuilder.fromUriString(applicationProperties.apiBaseUrl)
            .replacePath(StaticConfigurer.FILES_ENDPOINT)
            .pathSegment(hash(key))
            .pathSegment(*split)
            .toUriString()
    }

    private fun hash(key: String): String? {
        return nameHashGenerator.hash(key)
    }

    companion object {
        private const val INDEX_HTML = "index.html"
    }
}