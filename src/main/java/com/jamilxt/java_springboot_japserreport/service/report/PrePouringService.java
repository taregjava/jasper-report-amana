package com.jamilxt.java_springboot_japserreport.service.report;

import com.jamilxt.java_springboot_japserreport.dto.ProjectStartReportDto;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PrePouringService {

    private final ProjectStartReportService stageReportService;

    public PrePouringService(ProjectStartReportService stageReportService) {
        this.stageReportService = stageReportService;
    }

    public byte[] generatePrePouringPdfStatic() throws Exception {

        Map<String, Object> customData = new HashMap<>();

        customData.put("ownerName", "PrePouring Owner");
        customData.put("stageInspectionResult", "Pending");

        return stageReportService.generateStaticWithOverrides(
                StageReportProfile.PRE_POURING,
                customData
        );
    }

}
