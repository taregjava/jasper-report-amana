package com.halfacode.japserreport.controller;

import com.halfacode.japserreport.dto.ProjectStartReportDto;
import com.halfacode.japserreport.service.report.OCVerificationReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class OCVerificationReportController {

    private final OCVerificationReportService service;

    public OCVerificationReportController(OCVerificationReportService service) {
        this.service = service;
    }

    @GetMapping(path = "/oc-verification/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void ocVerificationPdfStatic(HttpServletResponse response) throws Exception {
        byte[] pdf = service.generateOCVerificationPdfStatic();
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
    }

    @PostMapping(path = "/oc-verification/pdf", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_PDF_VALUE)
    public void ocVerificationPdfFromBody(@RequestBody ProjectStartReportDto dto, HttpServletResponse response) throws Exception {
        byte[] pdf = service.generatePdf(dto);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "inline; filename=oc_verification_report.pdf");
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
        response.getOutputStream().flush();
    }
}
