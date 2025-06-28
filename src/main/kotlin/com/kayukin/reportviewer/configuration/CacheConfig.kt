package com.kayukin.reportviewer.configuration

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalCause
import com.kayukin.reportviewer.utils.FileUtils
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.DigestUtils
import java.io.File
import java.time.Duration

private val log = KotlinLogging.logger {}

@Configuration
class CacheConfig {
    @Bean
    fun cacheManager(caffeineCache: Cache<Any, Any>): CacheManager {
        val caffeineCacheManager = CaffeineCacheManager()

        caffeineCacheManager.registerCustomCache(
            FILES_CACHE,
            caffeineCache
        )

        return caffeineCacheManager
    }

    @Bean
    fun caffeineCache(downloadDirectory: File): Cache<Any, Any> {
        return Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(10))
            .evictionListener { key: Any, value: Any, removalCause: RemovalCause ->
                val path = downloadDirectory
                    .toPath()
                    .resolve(key.toString())
                FileUtils.deleteRecursively(path)
                log.trace { "${"Deleted file: {}"} $path" }
            }
            .build()
    }

    @Bean
    fun keyGenerator(): NameHashGenerator {
        return NameHashGenerator()
    }

    class NameHashGenerator : KeyGenerator {
        override fun generate(
            target: Any,
            method: java.lang.reflect.Method,
            vararg params: Any
        ): Any {
            val key = params[0] as String
            return hash(key)
        }

        fun hash(input: String): String {
            return DigestUtils.md5DigestAsHex(input.toByteArray())
        }
    }

    companion object {
        const val FILES_CACHE: String = "files"
    }
}
