package com.jamilxt.java_springboot_japserreport.controller;

import com.jamilxt.java_springboot_japserreport.dto.ConditionDto;
import com.jamilxt.java_springboot_japserreport.dto.ConditionImageDto;
import com.jamilxt.java_springboot_japserreport.dto.InspectionReportDto;
import com.jamilxt.java_springboot_japserreport.service.InspectionReportService;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestController
@RequestMapping("/test")
public class TestReportController {

    private final InspectionReportService reportService;
    private final ResourceLoader resourceLoader;

    public TestReportController(InspectionReportService reportService, ResourceLoader resourceLoader) {
        this.reportService = reportService;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping(value = "/multi-images", produces = MediaType.APPLICATION_PDF_VALUE)
    public void multiImages(HttpServletResponse response) throws Exception {
        InspectionReportDto dto = new InspectionReportDto();

        // Load two images from classpath into byte[] (front-image.png and logo.png)
        byte[] img1 = null;
        byte[] img2 = null;
        byte[] img3 = null;
        byte[] img4 = null;
        try (var is1 = resourceLoader.getResource("classpath:report/front-image.png").getInputStream()) {
            img1 = is1.readAllBytes();
        } catch (Exception ignored) {}
        try (var is2 = resourceLoader.getResource("classpath:report/logo.png").getInputStream()) {
            img2 = is2.readAllBytes();
        } catch (Exception ignored) {}
        // additional test images
        try (var is3 = resourceLoader.getResource("classpath:report/siteMapImage.png").getInputStream()) {
            img3 = is3.readAllBytes();
        } catch (Exception ignored) {}
        try (var is4 = resourceLoader.getResource("classpath:images/img.png").getInputStream()) {
            img4 = is4.readAllBytes();
        } catch (Exception ignored) {}

        ConditionImageDto ci1 = new ConditionImageDto(1, "صورة مثال 1", img1, "ملاحظة 1");
        ConditionImageDto ci2 = new ConditionImageDto(2, "صورة مثال 2", img2, "ملاحظة 2");
        ConditionImageDto ci3 = new ConditionImageDto(3, "صورة مثال 3", img3, "ملاحظة 3");
        ConditionImageDto ci4 = new ConditionImageDto(4, "صورة مثال 4", img4, "ملاحظة 4");

        ConditionDto condition = new ConditionDto(
                1,
                "بند تجريبي مع عدة صور",
                Boolean.FALSE,
                Boolean.TRUE,
                "سكني",
                1,
                "الواجهة الامامية",
                Arrays.asList(ci1, ci2, ci3, ci4)
        );

        dto.setConditions(Arrays.asList(condition));

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "inline; filename=test-multi-images.pdf");
        reportService.writePdfToResponse(dto, response);
    }
}
