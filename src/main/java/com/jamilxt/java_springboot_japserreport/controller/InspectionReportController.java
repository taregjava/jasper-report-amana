package com.jamilxt.java_springboot_japserreport.controller;

import com.jamilxt.java_springboot_japserreport.dto.InspectionReportDto;
import com.jamilxt.java_springboot_japserreport.service.InspectionReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/reports")
public class InspectionReportController {

    private final InspectionReportService reportService;

    public InspectionReportController(InspectionReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(path = "/inspection/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void inspectionPdf(HttpServletResponse response) throws Exception {
        // Build a demo InspectionReportDto. In real use, build from DB/service.
        InspectionReportDto dto = new InspectionReportDto();
        dto.setReportTitle("تقرير مرحلة ما بعد صب الأساسات");
        dto.setOwnerName("اسم المالك هنا");
        dto.setBuildingAddress("الرياض - شارع النموذج - قطعة 12");
        dto.setInspectionDate(LocalDate.now().toString());
        // combine checklist text for demo
        dto.setChecklistText("1. تحقق من السلامة\n2. التحقق من العناصر الإنشائية\n3. ملاحظات أخرى...");

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        reportService.writePdfToResponse(dto, response);
    }
}
