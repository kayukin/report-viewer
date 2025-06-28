package com.kayukin.reportviewer.controller

import com.kayukin.reportviewer.dto.Report
import com.kayukin.reportviewer.service.S3Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reports/")
class ReportController(private val s3Service: S3Service) {

    @GetMapping("/")
    fun listReports(): MutableList<Report?> {
        return s3Service.list()
    }

    @GetMapping("/view")
    fun viewReport(@RequestParam key: String): String? {
        return s3Service.downloadAndUnpack(key)
    }
}