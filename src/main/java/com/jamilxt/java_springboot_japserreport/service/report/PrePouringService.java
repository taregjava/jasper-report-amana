package com.jamilxt.java_springboot_japserreport.service.report;

import com.jamilxt.java_springboot_japserreport.dto.ProjectStartReportDto;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

@Service
public class PrePouringService {

    private final ProjectStartReportService stageReportService;

    public PrePouringService(ProjectStartReportService stageReportService) {
        this.stageReportService = stageReportService;
    }

    public byte[] generatePrePouringPdfStatic() throws Exception {
        // Uses the static data method for PRE_POURING profile
        return stageReportService.generateStatic(StageReportProfile.PRE_POURING);
    }

    public byte[] generatePdf(ProjectStartReportDto dto) throws JRException {
        // Use PRE_POURING profile for prePouring reports
        return stageReportService.generatePdf(dto, StageReportProfile.PRE_POURING);
    }
}
