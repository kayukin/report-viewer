package com.kayukin.reportviewer.controller;

import com.kayukin.reportviewer.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports/")
public class ReportController {
    private final S3Service s3Service;

    @GetMapping("/")
    public List<String> listReports() {
        return s3Service.list();
    }

    @GetMapping("/view")
    public String viewReport(@RequestParam String key) {
        return s3Service.downloadAndUnpack(key);
    }
}