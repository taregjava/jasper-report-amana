package com.halfacode.japserreport.controller;

import com.halfacode.japserreport.dto.ProjectStartReportDto;
import com.halfacode.japserreport.service.report.PreFoundationStageReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class PreFoundationStageReportController {

    private final PreFoundationStageReportService service;

    public PreFoundationStageReportController(PreFoundationStageReportService service) {
        this.service = service;
    }

    @GetMapping(path = "/pre-foundation/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void preFoundationPdfStatic(HttpServletResponse response) throws Exception {
        byte[] pdf = service.generatePreFoundationPdfStatic();
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
    }

    @PostMapping(path = "/pre-foundation/pdf", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_PDF_VALUE)
    public void preFoundationPdfFromBody(@RequestBody ProjectStartReportDto dto, HttpServletResponse response) throws Exception {
        byte[] pdf = service.generatePdf(dto);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "inline; filename=pre_foundation_stage_report.pdf");
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
        response.getOutputStream().flush();
    }
}

