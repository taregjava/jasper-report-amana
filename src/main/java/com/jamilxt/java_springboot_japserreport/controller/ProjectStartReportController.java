package com.jamilxt.java_springboot_japserreport.controller;

import com.jamilxt.java_springboot_japserreport.dto.BuildingInfo;
import com.jamilxt.java_springboot_japserreport.dto.OwnerInfo;
import com.jamilxt.java_springboot_japserreport.dto.ProjectPhotosDTO;
import com.jamilxt.java_springboot_japserreport.dto.ProjectStartReportDto;
import com.jamilxt.java_springboot_japserreport.dto.TaskRow;
import com.jamilxt.java_springboot_japserreport.service.report.ProjectStartReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/reports")
public class ProjectStartReportController {

    private final ProjectStartReportService service;

    public ProjectStartReportController(ProjectStartReportService service) {
        this.service = service;
    }
    @GetMapping(path = "/project/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void projectStartPdfsss(HttpServletResponse response) throws Exception {
        byte[] pdf = service.generateProjectStartPdfStatic();
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
    }

    @GetMapping(path = "/project-start/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void projectStartPdf(HttpServletResponse response) throws Exception {
        ProjectStartReportDto dto = new ProjectStartReportDto();

        OwnerInfo ownerInfo = new OwnerInfo();
        ownerInfo.setOwnerName("محمود محمد");
        ownerInfo.setIdNumber("1234567890");
        ownerInfo.setMobileNumber("0501234567");
        ownerInfo.setEngineeringOffice("مكتب الهندسة العربي");
        ownerInfo.setDesignerOffice("مكتب التصميم العالمي");
        ownerInfo.setContractorName("شركة المقاولات المتحدة");
        ownerInfo.setContractorLicense("CR-998877");
        ownerInfo.setProjectName("مشروع سكني - حي النخيل");
        ownerInfo.setReportDate("1447/09/12");
        dto.setOwnerInfo(ownerInfo);

        BuildingInfo bi = new BuildingInfo();
        bi.setBuildingType("سكني");
        bi.setBuildingDescription("عمارة سكنية من دورين");
        bi.setLandNumber("45");
        bi.setBlockNumber("12A");
        bi.setPlanNumber("PL-2026-45");
        bi.setLicenseNumber("LIC-2026-441");
        bi.setLicenseDate("1446/03/05");
        bi.setDistrict("حي النموذج");
        bi.setStreet("شارع الأمير عبدالله");
        dto.setBuildingInfo(bi);

        TaskRow t1 = new TaskRow();
        t1.setIndex(1);
        t1.setGroupTitle("متطلبات سور الحماية");
        t1.setSideNumber("2");
        t1.setTaskName("وضع أساس المبنى");
        t1.setCompliant("☑");
        t1.setNotes("تأكيد جودة الخرسانة");

        TaskRow t2 = new TaskRow();
        t2.setIndex(2);
        t2.setSideNumber("2");
        t2.setTaskName("التأكد من الخرسانة");
        t2.setCompliant("☐");
        t2.setNotes("أخذ عينات للفحص المختبري");

        dto.setTasks(Arrays.asList(t1, t2));

        ProjectPhotosDTO photos = new ProjectPhotosDTO();
        photos.setSpatialPortalPhoto("classpath:report/siteMapImage.png");
        photos.setImplementationPhoto("classpath:report/front-image.png");
        photos.setAerialPhoto("classpath:report/logoSite.png");
        photos.setOfficeName("مكتب الهندسة العربي");
        photos.setDigitalStampPath("classpath:report/logo.png");
        dto.setProjectPhotos(photos);

        byte[] pdf = service.generatePdf(dto);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "inline; filename=project_start_report.pdf");
        response.getOutputStream().write(pdf);
        response.getOutputStream().flush();
    }
}
