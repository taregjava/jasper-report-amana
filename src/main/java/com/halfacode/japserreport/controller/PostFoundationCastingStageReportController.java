package com.halfacode.japserreport.controller;

import com.halfacode.japserreport.dto.BuildingInfo;
import com.halfacode.japserreport.dto.OwnerInfo;
import com.halfacode.japserreport.dto.ProjectPhotosDTO;
import com.halfacode.japserreport.dto.ProjectStartReportDto;
import com.halfacode.japserreport.dto.TaskRow;
import com.halfacode.japserreport.service.report.PostFoundationCastingStageReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/reports")
public class PostFoundationCastingStageReportController {

    private final PostFoundationCastingStageReportService service;

    public PostFoundationCastingStageReportController(PostFoundationCastingStageReportService service) {
        this.service = service;
    }

    @GetMapping(path = "/post-foundation/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void postFoundationPdfStatic(HttpServletResponse response) throws Exception {
        byte[] pdf = service.generatePostFoundationPdfStatic();
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
    }

    @PostMapping(path = "/post-foundation/pdf", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_PDF_VALUE)
    public void postFoundationPdfFromBody(@RequestBody ProjectStartReportDto dto, HttpServletResponse response) throws Exception {
        byte[] pdf = service.generatePdf(dto);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "inline; filename=post_foundation_casting_stage_report.pdf");
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
        response.getOutputStream().flush();
    }

}
