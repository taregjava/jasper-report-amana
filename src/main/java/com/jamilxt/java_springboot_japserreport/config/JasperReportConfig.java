package com.jamilxt.java_springboot_japserreport.config;

import jakarta.annotation.PostConstruct;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStream;

@Configuration
public class JasperReportConfig {

    private JasperReport inspectionReport;
    private JasperReport inspectionImagesReport;
    private JasperReport inspectionTableReport;
    @Autowired
    ResourceLoader resourceLoader;

    @PostConstruct
    public void init() throws Exception {
        inspectionReport = compileReport("classpath:report/inspection_report.jrxml");
        inspectionImagesReport = compileReport("classpath:report/inspection_images.jrxml");
        inspectionTableReport = compileReport("classpath:report/inspection_table.jrxml");
    }

    private JasperReport compileReport(String path) throws Exception {
        Resource resource = resourceLoader.getResource(path);

        if (!resource.exists()) {
            throw new RuntimeException("JRXML NOT FOUND: " + path);
        }

        try (InputStream is = resource.getInputStream()) {
            JasperDesign design = JRXmlLoader.load(is);
            return JasperCompileManager.compileReport(design);
        }
    }

    public JasperReport getInspectionReport() {
        return inspectionReport;
    }

    public JasperReport getInspectionImagesReport() {
        return inspectionImagesReport;
    }

    public JasperReport getInspectionTableReport() {
        return inspectionTableReport;
    }
}