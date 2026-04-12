package com.jamilxt.java_springboot_japserreport.service;

import com.jamilxt.java_springboot_japserreport.dto.ConditionDto;
import com.jamilxt.java_springboot_japserreport.dto.ConditionImageDto;
import com.jamilxt.java_springboot_japserreport.dto.InspectionItemDto;
import com.jamilxt.java_springboot_japserreport.dto.InspectionReportDto;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InspectionReportService {

    private final ResourceLoader resourceLoader;

    public InspectionReportService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public byte[] generatePdf(InspectionReportDto dto) throws Exception {
        try (java.io.InputStream masterIs = resourceLoader.getResource("classpath:report/inspection_report.jrxml").getInputStream();
             java.io.InputStream tableIs = resourceLoader.getResource("classpath:report/inspection_table.jrxml").getInputStream();
             java.io.InputStream conditionsIs = resourceLoader.getResource("classpath:report/inspection_conditions_flat.jrxml").getInputStream()) {

            JasperReport masterReport = JasperCompileManager.compileReport(masterIs);
            JasperReport tableSubreport = JasperCompileManager.compileReport(tableIs);
            JasperReport conditionsSubreport = JasperCompileManager.compileReport(conditionsIs);

            List<Map<String, Object>> tableRows = buildTableRows(dto);
            List<Map<String, Object>> conditionRows = buildConditionRows(dto);

            Map<String, Object> params = new HashMap<>();
            params.put("inspectionTableSubreport", tableSubreport);
            params.put("inspectionConditionsSubreport", conditionsSubreport);
            params.put("tableData", new JRBeanCollectionDataSource(tableRows));
            params.put("conditionsData", new JRBeanCollectionDataSource(conditionRows));
            params.put("conditionImageRows", new JRBeanCollectionDataSource(conditionRows));

            params.put("entity", "أمانة منطقة الرياض");
            params.put("controlNumber", "رقم 328872");
            params.put("dateAndControl", safe(dto.getInspectionDate(), "2026-04-07"));
            params.put("pageInfo", "1 / 1");
            params.put("ownerName", safe(dto.getOwnerName(), "اسم المالك"));
            params.put("proofNumber", "3001");
            params.put("plotNumber", "12");
            params.put("districtName", safe(dto.getBuildingAddress(), "حي نموذجي"));
            params.put("streetName", "شارع الأمير" );
            params.put("licenseNumber", "LIC-2026-0091");
            params.put("buildingName", safe(dto.getReportTitle(), "تقرير امتثال"));
            params.put("reportStatus", "مطابق جزئيا");
            params.put("supervisingOffice", "مكتب الاشراف العام");
            params.put("landArea", "500 م2");
            params.put("transactionNumber", "TXN-10042");
            params.put("reportType", "تقرير فني");
            params.put("constructionStage", "مرحلة بعد صب الاساسات");
            params.put("buildingLicense", "BL-441");
            params.put("requestNumber", "REQ-778");
            params.put("zoningPlan", "سكني");
            params.put("activityType", "سكني");
            params.put("buildingStage", "قائم");
            params.put("regulatoryPlan", "معتمد");

            params.put("reportLogo", resourceUrlOrNull("classpath:report/logo.png"));
            params.put("siteMapImage", resourceUrlOrNull("classpath:report/siteMapImage.png"));
            params.put("frontImage", resourceUrlOrNull("classpath:report/front-image.png"));
            params.put("rightImage", resourceUrlOrNull("classpath:report/front-image.png"));
            params.put("leftImage", resourceUrlOrNull("classpath:report/front-image.png"));

            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, params, new JREmptyDataSource(1));
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
    }

    private List<Map<String, Object>> buildTableRows(InspectionReportDto dto) {
        if (dto != null && dto.getChecklist() != null && !dto.getChecklist().isEmpty()) {
            return dto.getChecklist().stream()
                    .map(this::toTableRow)
                    .collect(Collectors.toList());
        }

        List<Map<String, Object>> staticRows = new ArrayList<>();
        staticRows.add(tableRow(1, "توفر مواقف سيارات", "MATCHED", "سكني", "1"));
        staticRows.add(tableRow(2, "سلامة العناصر الانشائية", "NOT_MATCHED", "سكني", "1"));
        staticRows.add(tableRow(3, "توفر وسائل السلامة", "MATCHED", "تجاري", "2"));
        return staticRows;
    }

    private Map<String, Object> toTableRow(InspectionItemDto item) {
        return tableRow(
                item.getItemNumber(),
                item.getDescription(),
                safe(item.getStatus(), "NOT_MATCHED"),
                item.getClassification(),
                item.getRepeat() == null ? "" : String.valueOf(item.getRepeat())
        );
    }

    private Map<String, Object> tableRow(Integer itemNumber, String description, String status, String classification, String repeat) {
        Map<String, Object> row = new HashMap<>();
        row.put("itemNumber", itemNumber);
        row.put("description", description);
        row.put("status", status);
        row.put("classification", classification);
        row.put("repeat", repeat);
        row.put("match", "");
        row.put("notMatch", "");
        return row;
    }
//todo : loop over conditions||| for each condition loop over imageCondition
    private List<Map<String, Object>> buildConditionRows(InspectionReportDto dto) {
        List<ConditionDto> conditions = (dto != null) ? dto.getConditions() : null;

        if (conditions != null && !conditions.isEmpty()) {
            return conditions.stream()
                    .map(this::toConditionRow)
                    .collect(Collectors.toList());
        }

        return defaultConditions().stream()
                .map(this::toConditionRow)
                .collect(Collectors.toList());
    }

    private Map<String, Object> toConditionRow(ConditionDto condition) {
        Map<String, Object> row = new HashMap<>();
        row.put("rowNumber", condition.getRowNumber());
        row.put("title", buildConditionTitle(condition));
        row.put("conditionText", condition.getConditionText());
        row.put("noteText", buildConditionNote(condition));
        row.put("imagePath", firstImageOrDefault(condition));
        return row;
    }

    private List<ConditionDto> defaultConditions() {
        ConditionImageDto image1 = new ConditionImageDto(
                1,
                "مطلوب استكمال اعمال العزل حسب المخطط المعتمد.",
                null,
                "يلزم التنفيذ خلال 14 يوما مع اعادة الزيارة."
        );
        ConditionImageDto image2 = new ConditionImageDto(
                2,
                "معالجة التشققات في الجدار الجنوبي قبل الاغلاق.",
                null,
                "توثيق المعالجة بصور محدثة."
        );

        ConditionDto first = new ConditionDto(
                1,
                "مطلوب استكمال اعمال العزل حسب المخطط المعتمد.",
                Boolean.FALSE,
                Boolean.TRUE,
                "سكني",
                1,
                "الواجهة الامامية",
                Collections.singletonList(image1)
        );

        ConditionDto second = new ConditionDto(
                2,
                "معالجة التشققات في الجدار الجنوبي قبل الاغلاق.",
                Boolean.FALSE,
                Boolean.TRUE,
                "سكني",
                1,
                "الجهة الجنوبية",
                Collections.singletonList(image2)
        );

        return Arrays.asList(first, second);
    }

    private String buildConditionTitle(ConditionDto condition) {
        if (condition == null) {
            return "";
        }
        return String.format(
                "التصنيف: %s | الجانب: %s",
                safe(condition.getClassification(), "غير محدد"),
                safe(condition.getSide(), "غير محدد")
        );
    }

    private String buildConditionNote(ConditionDto condition) {
        if (condition == null) {
            return "";
        }

        String compliantText = Boolean.TRUE.equals(condition.getCompliant()) ? "مطابق" : "غير مطابق";
        String nonCompliantText = Boolean.TRUE.equals(condition.getNonCompliant()) ? "نعم" : "لا";
        String occurrence = condition.getOccurrenceCount() == null ? "0" : String.valueOf(condition.getOccurrenceCount());

        return String.format(
                "الحالة: %s | غير مطابق: %s | عدد التكرار: %s",
                compliantText,
                nonCompliantText,
                occurrence
        );
    }

    private Object firstImageOrDefault(ConditionDto condition) {
        if (condition != null && condition.getImages() != null && !condition.getImages().isEmpty()) {
            ConditionImageDto first = condition.getImages().get(0);
            if (first != null && first.getImagePath() != null) {
                return first.getImagePath();
            }
        }

        return resourceUrlOrNull("classpath:report/front-image.png");
    }

    private Object resourceUrlOrNull(String location) {
        try {
            return resourceLoader.getResource(location).exists()
                    ? resourceLoader.getResource(location).getURL()
                    : null;
        } catch (Exception ex) {
            return null;
        }
    }

    private String safe(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value;
    }

    public void writePdfToResponse(InspectionReportDto dto, HttpServletResponse response) throws Exception {
        byte[] pdf = generatePdf(dto);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=inspection_report.pdf");
        response.getOutputStream().write(pdf);
        response.getOutputStream().flush();
    }
}
