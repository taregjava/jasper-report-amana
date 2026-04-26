package com.jamilxt.java_springboot_japserreport.controller;

import com.jamilxt.java_springboot_japserreport.service.report.ProTipReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ProTipController {

    private final ProTipReportService proTipReportService;

    @GetMapping(path = "/pre-tip/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void projectStartPdfsss(HttpServletResponse response) throws Exception {
        byte[] pdf = proTipReportService.generateProjectStartPdfStatic();
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
    }

    @GetMapping(path = "/pre-tip2/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void projectStartPdfsss2(HttpServletResponse response) throws Exception {
        byte[] pdf = proTipReportService.generateProTipStatic();
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
    }

}
