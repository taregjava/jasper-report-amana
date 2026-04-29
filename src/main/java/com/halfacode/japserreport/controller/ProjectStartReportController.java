package com.halfacode.japserreport.controller;

import com.halfacode.japserreport.dto.BuildingInfo;
import com.halfacode.japserreport.dto.OwnerInfo;
import com.halfacode.japserreport.dto.ProjectPhotosDTO;
import com.halfacode.japserreport.dto.ProjectStartReportDto;
import com.halfacode.japserreport.dto.TaskRow;
import com.halfacode.japserreport.service.report.PrePouringService;
import com.halfacode.japserreport.service.report.ProTipService;
import com.halfacode.japserreport.service.report.ProjectStartReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/reports")
public class ProjectStartReportController {

    private final ProjectStartReportService service;

    private final PrePouringService prePouringService;

    private final ProTipService proTipService;

    public ProjectStartReportController(ProjectStartReportService service, PrePouringService prePouringService, ProTipService proTipService) {
        this.service = service;
        this.prePouringService = prePouringService;
        this.proTipService = proTipService;
    }
    @GetMapping(path = "/pre-pouring/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void projectStartPdfsss(HttpServletResponse response) throws Exception {
        byte[] pdf = prePouringService.generatePrePouringPdfStatic();
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
    }

    @GetMapping(path = "/project/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void generatePrePouringPdfStatic(HttpServletResponse response) throws Exception {
        byte[] pdf = service.generateProjectStartPdfStatic();
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
    }

    @PostMapping(path = "/project/pdf", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_PDF_VALUE)
    public void projectStartPdfFromBody(@RequestBody ProjectStartReportDto dto, HttpServletResponse response) throws Exception {
        byte[] pdf = service.generatePdf(dto);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "inline; filename=project_start_report.pdf");
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
        response.getOutputStream().flush();
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
        photos.setImplementationPhoto1("classpath:report/front-image.png");
        photos.setImplementationPhoto2("classpath:report/siteMapImage.png");
        photos.setImplementationPhoto3("classpath:report/front-image.png");
        photos.setImplementationPhoto4("classpath:report/logoSite.png");
        photos.setDetailPhoto1("classpath:report/front-image.png");
        photos.setDetailPhoto2("classpath:report/siteMapImage.png");
        photos.setDetailPhoto3("classpath:report/logoSite.png");
        photos.setDetailPhoto4("classpath:report/front-image.png");
        photos.setDetailPhoto5("classpath:report/siteMapImage.png");
        photos.setDetailPhoto6("classpath:report/logoSite.png");
        photos.setDetailDescription1("تفصيل تنفيذ رقم 1");
        photos.setDetailDescription2("تفصيل تنفيذ رقم 2");
        photos.setDetailDescription3("تفصيل تنفيذ رقم 3");
        photos.setDetailDescription4("تفصيل تنفيذ رقم 4");
        photos.setDetailDescription5("تفصيل تنفيذ رقم 5");
        photos.setDetailDescription6("تفصيل تنفيذ رقم 6");
        photos.setOfficeName("مكتب الهندسة العربي");
        photos.setDigitalStampPath("classpath:report/logo.png");
        dto.setProjectPhotos(photos);

        byte[] pdf = service.generatePdf(dto);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "inline; filename=project_start_report.pdf");
        response.getOutputStream().write(pdf);
        response.getOutputStream().flush();
    }

    @GetMapping(path = "/pro-tip/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void proTipPdf(HttpServletResponse response) throws Exception {
        byte[] pdf = proTipService.generateProTipPdfStatic();
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
        response.getOutputStream().flush();
    }
}
