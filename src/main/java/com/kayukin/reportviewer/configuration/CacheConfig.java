package com.kayukin.reportviewer.configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.kayukin.reportviewer.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.time.Duration;

@Slf4j
@Configuration
public class CacheConfig {

    public static final String FILES_CACHE = "files";

    @Bean
    public CacheManager cacheManager(Cache<Object, Object> caffeineCache) {
        final var caffeineCacheManager = new CaffeineCacheManager();

        caffeineCacheManager.registerCustomCache(FILES_CACHE, caffeineCache);

        return caffeineCacheManager;
    }

    @Bean
    public Cache<Object, Object> caffeineCache(File downloadDirectory) {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(10))
                .evictionListener((Object key, Object value, RemovalCause removalCause) -> {
                    final var path = downloadDirectory
                            .toPath()
                            .resolve(key.toString());
                    FileUtils.deleteRecursively(path);
                    log.trace("Deleted file: {}", path);
                })
                .build();
    }

    @Bean
    public NameHashGenerator keyGenerator() {
        return new NameHashGenerator();
    }

    public static class NameHashGenerator implements KeyGenerator {

        @Override
        public Object generate(Object target, Method method, Object... params) {
            final var key = (String) params[0];
            return hash(key);
        }

        public String hash(String input) {
            return DigestUtils.md5DigestAsHex(input.getBytes());
        }
    }
}
