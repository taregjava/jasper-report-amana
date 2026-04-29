package com.halfacode.japserreport.service.report;

import com.halfacode.japserreport.dto.ProjectStartReportDto;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

@Service
public class PostFoundationCastingStageReportService {

    private final ProjectStartReportService stageReportService;

    public PostFoundationCastingStageReportService(ProjectStartReportService stageReportService) {
        this.stageReportService = stageReportService;
    }

    public byte[] generatePostFoundationPdfStatic() throws Exception {
        return stageReportService.generateStatic(StageReportProfile.POST_FOUNDATION);
    }

    public byte[] generatePdf(ProjectStartReportDto dto) throws JRException {
        return stageReportService.generatePdf(dto, StageReportProfile.POST_FOUNDATION);
    }
}
