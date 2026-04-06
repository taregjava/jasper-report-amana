package com.jamilxt.java_springboot_japserreport.service;

import com.jamilxt.java_springboot_japserreport.dto.InspectionReportDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.InputStream;
import java.util.*;

@Service
public class InspectionReportService {

    private final ResourceLoader resourceLoader;

    public InspectionReportService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public byte[] generatePdf(InspectionReportDto dto) throws Exception {
        // compile JRXML from classpath
        try (InputStream is = resourceLoader.getResource("classpath:report/dynamic/inspection_report_ar.jrxml").getInputStream()) {
            JasperReport jasperReport = JasperCompileManager.compileReport(is);
            // For simplicity we use a single-element collection as the data source
            List<InspectionReportDto> list = Collections.singletonList(dto);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
            Map<String, Object> params = new HashMap<>();
            // add report title and logo if present
            params.put("REPORT_TITLE", dto.getReportTitle() == null ? "تقرير" : dto.getReportTitle());
            try {
                InputStream logoStream = resourceLoader.getResource("classpath:report/logo.png").getInputStream();
                params.put("LOGO", logoStream);
            } catch (Exception ex) {
                // ignore if logo missing
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
    }

    public void writePdfToResponse(InspectionReportDto dto, HttpServletResponse response) throws Exception {
        byte[] pdf = generatePdf(dto);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=inspection_report.pdf");
        response.getOutputStream().write(pdf);
        response.getOutputStream().flush();
    }
}
