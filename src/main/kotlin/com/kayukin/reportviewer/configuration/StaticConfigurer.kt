package com.kayukin.reportviewer.configuration

import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.CacheControl
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver
import org.springframework.web.servlet.resource.ResourceResolverChain
import java.io.File
import java.util.*

@Configuration
class StaticConfigurer(private val downloadDirectory: File) : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val path = downloadDirectory.absolutePath + File.separator
        val resource = FileSystemResource(path)
        registry.addResourceHandler("/$FILES_ENDPOINT/**")
            .setCacheControl(CacheControl.noCache())
            .addResourceLocations(resource)
        serveDirectory(registry, "/", "classpath:/static/")
    }

    private fun serveDirectory(
        registry: ResourceHandlerRegistry,
        endpoint: String,
        location: String
    ) {
        val endpointPatterns: Array<String> = if (endpoint.endsWith("/")) {
            arrayOf(endpoint.substring(0, endpoint.length - 1), endpoint, "$endpoint**")
        } else {
            arrayOf(endpoint, "$endpoint/", "$endpoint/**")
        }
        registry
            .addResourceHandler(*endpointPatterns)
            .addResourceLocations(if (location.endsWith("/")) location else location + "/")
            .resourceChain(false)
            .addResolver(IndexResourceResolver())
    }

    private class IndexResourceResolver : PathResourceResolver() {
        override fun resolveResource(
            request: HttpServletRequest?,
            requestPath: String,
            locations: MutableList<out Resource?>, chain: ResourceResolverChain
        ): Resource? {
            val resource = super.resolveResource(request, requestPath, locations, chain)
            if (Objects.nonNull(resource)) {
                return resource
            }
            return super.resolveResource(request, "/index.html", locations, chain)
        }
    }

    companion object {
        const val FILES_ENDPOINT: String = "downloaded"
    }
}