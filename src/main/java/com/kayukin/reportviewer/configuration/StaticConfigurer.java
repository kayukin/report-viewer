package com.kayukin.reportviewer.configuration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.io.File;
import java.util.List;

import static java.util.Objects.nonNull;

@Configuration
@RequiredArgsConstructor
public class StaticConfigurer implements WebMvcConfigurer {
    private final File downloadDirectory;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        final var path = downloadDirectory.getAbsolutePath() + File.separator;
        final var resource = new FileSystemResource(path);
        registry.addResourceHandler("/downloaded/**")
                .addResourceLocations(resource);
        serveDirectory(registry, "/", "classpath:/static/");
    }

    private void serveDirectory(ResourceHandlerRegistry registry, String endpoint, String location) {
        String[] endpointPatterns = endpoint.endsWith("/")
                ? new String[]{endpoint.substring(0, endpoint.length() - 1), endpoint, endpoint + "**"}
                : new String[]{endpoint, endpoint + "/", endpoint + "/**"};
        registry
                .addResourceHandler(endpointPatterns)
                .addResourceLocations(location.endsWith("/") ? location : location + "/")
                .resourceChain(false)
                .addResolver(new IndexResourceResolver());
    }

    private static class IndexResourceResolver extends PathResourceResolver {
        @Override
        public Resource resolveResource(HttpServletRequest request,
                                        String requestPath,
                                        List<? extends Resource> locations, ResourceResolverChain chain) {
            Resource resource = super.resolveResource(request, requestPath, locations, chain);
            if (nonNull(resource)) {
                return resource;
            }
            return super.resolveResource(request, "/index.html", locations, chain);
        }
    }
}