package com.jamilxt.java_springboot_japserreport.service.report;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProTipService {

    private final ProjectStartReportService stageReportService;

    public ProTipService(ProjectStartReportService stageReportService) {
        this.stageReportService = stageReportService;
    }

    public byte[] generateProTipPdfStatic() throws Exception {
        Map<String, Object> customData = new HashMap<>();
        customData.put("stageReportTitle", "Pro Tip Report");
        customData.put("stageInspectionResult", "Advisory");
        return stageReportService.generateStaticWithOverrides(StageReportProfile.PRO_TIP, customData);
    }
}

