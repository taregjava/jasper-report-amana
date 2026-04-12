package com.jamilxt.java_springboot_japserreport.service.report;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

public class ProjectStartReportGenerator {

    public static class TaskRow {
        private Integer index;
        private String task;
        private String status;

        public TaskRow(Integer index, String task, String status) {
            this.index = index;
            this.task = task;
            this.status = status;
        }

        public Integer getIndex() {
            return index;
        }

        public String getTask() {
            return task;
        }

        public String getStatus() {
            return status;
        }
    }

    public static void main(String[] args) throws Exception {
        // Load JRXML
        InputStream jrxml = ProjectStartReportGenerator.class.getResourceAsStream("/report/project_start_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxml);

        // Static params
        Map<String, Object> params = new HashMap<>();
        params.put("ownerName", "محمود محمد");
        params.put("buildingType", "سكني");

        // Static tasks
        List<TaskRow> tasks = new ArrayList<>();
        tasks.add(new TaskRow(1, "وضع أساس المبنى", "☑"));
        tasks.add(new TaskRow(2, "التأكد من الخرسانة", "☐"));
        tasks.add(new TaskRow(3, "اختبارات السلامة", "☑"));

        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(tasks);

        // Fill report (use datasource for detail rows)
        JasperPrint print = JasperFillManager.fillReport(jasperReport, params, ds);

        // Export to PDF file for quick test
        try (FileOutputStream fos = new FileOutputStream("target/project_start_report.pdf")) {
            JasperExportManager.exportReportToPdfStream(print, fos);
        }

        System.out.println("Generated target/project_start_report.pdf");
    }
}

