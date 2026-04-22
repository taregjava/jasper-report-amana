package com.jamilxt.java_springboot_japserreport.service.report;

import com.jamilxt.java_springboot_japserreport.dto.ProjectStartReportDto;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

@Service
public class PreFoundationStageReportService {

    private final ProjectStartReportService stageReportService;

    public PreFoundationStageReportService(ProjectStartReportService stageReportService) {
        this.stageReportService = stageReportService;
    }

    public byte[] generatePreFoundationPdfStatic() throws Exception {
        return stageReportService.generateStatic(StageReportProfile.PRE_FOUNDATION);
    }

    public byte[] generatePdf(ProjectStartReportDto dto) throws JRException {
        return stageReportService.generatePdf(dto, StageReportProfile.PRE_FOUNDATION);
    }
}

