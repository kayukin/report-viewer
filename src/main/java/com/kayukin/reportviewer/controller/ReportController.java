package com.kayukin.reportviewer.controller;

import com.kayukin.reportviewer.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ReportController {
    private final S3Service s3Service;

    @GetMapping("/")
    public String report(Model model) {
        model.addAttribute("reports", s3Service.list());
        return "index";
    }

    @GetMapping("/view")
    public String viewReport(@RequestParam String key, Model model) {
        final var indexUrl = s3Service.downloadAndUnpack(key);
        model.addAttribute("link", indexUrl);
        return "view";
    }
}