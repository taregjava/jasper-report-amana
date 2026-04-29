package com.halfacode.japserreport;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

public class InspectionReportStaticDataTest {

    @Test
    public void generateStaticPdf() throws Exception {
        // load master and subreports from classpath
        InputStream masterIs = getClass().getResourceAsStream("/report/inspection_report.jrxml");
        InputStream tableIs = getClass().getResourceAsStream("/report/inspection_table.jrxml");
        InputStream imagesIs = getClass().getResourceAsStream("/report/inspection_images.jrxml");
        InputStream conditionsIs = getClass().getResourceAsStream("/report/inspection_conditions_flat.jrxml");

        JasperReport masterReport = JasperCompileManager.compileReport(masterIs);
        JasperReport tableReport = JasperCompileManager.compileReport(tableIs);
        JasperReport imagesReport = JasperCompileManager.compileReport(imagesIs);
        JasperReport conditionsReport = JasperCompileManager.compileReport(conditionsIs);

        // create sample table items (match status) – fields must match InspectionItemDto used by project
        List<Map<String, Object>> tableItems = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> row = new HashMap<>();
            row.put("itemNumber", i);
            row.put("description", "بند رقم " + i);
            row.put("status", i % 2 == 0 ? "MATCHED" : "NOT_MATCHED");
            row.put("classification", "سكني");
            row.put("repeat", 1);
            tableItems.add(row);
        }

        // conditions list (flat) – reuse the same structure
        List<Map<String, Object>> conditions = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Map<String, Object> c = new HashMap<>();
            c.put("m", i);
            c.put("item", "حالة " + i);
            c.put("match", "");
            c.put("notMatch", "");
            c.put("category", "عام");
            c.put("repeat", "1");
            conditions.add(c);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("inspectionTableSubreport", tableReport);
        params.put("inspectionImagesSubreport", imagesReport);
        params.put("inspectionConditionsSubreport", conditionsReport);

        params.put("tableData", new JRBeanCollectionDataSource(tableItems));
        params.put("imageData", new JRBeanCollectionDataSource(Collections.emptyList()));
        params.put("conditionsData", new JRBeanCollectionDataSource(conditions));

        // basic textual params
        params.put("ownerName", "مالك الاختبار");
        params.put("proofNumber", "123");
        params.put("plotNumber", "45");
        params.put("districtName", "حي الاختبار");
        params.put("streetName", "شارع الاختبار");
        params.put("reportLogo", getClass().getResourceAsStream("/report/logoSite.png"));

        JasperPrint print = JasperFillManager.fillReport(masterReport, params, new JREmptyDataSource(1));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        exporter.exportReport();

        // write to disk for inspection
        try (FileOutputStream fos = new FileOutputStream("target/reports/static-inspection.pdf")) {
            fos.write(out.toByteArray());
        }

        System.out.println("Wrote target/reports/static-inspection.pdf (" + out.size() + " bytes)");
    }
}

