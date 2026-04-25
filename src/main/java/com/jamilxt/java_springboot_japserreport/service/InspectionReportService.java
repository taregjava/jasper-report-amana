package com.jamilxt.java_springboot_japserreport.service;

import com.jamilxt.java_springboot_japserreport.dto.ConditionDto;
import com.jamilxt.java_springboot_japserreport.dto.ConditionImageDto;
import com.jamilxt.java_springboot_japserreport.dto.InspectionItemDto;
import com.jamilxt.java_springboot_japserreport.dto.InspectionReportDto;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
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
            // DEBUG: inspect incoming dto conditions to see what's being passed in
            if (dto == null) {
                System.out.println("[DEBUG] incoming dto is null");
            } else if (dto.getConditions() == null) {
                System.out.println("[DEBUG] dto.getConditions() == null");
            } else {
                System.out.println("[DEBUG] dto.getConditions().size() = " + dto.getConditions().size());
                for (int ci = 0; ci < dto.getConditions().size(); ci++) {
                    ConditionDto c = dto.getConditions().get(ci);
                    int imgCount = (c == null || c.getImages() == null) ? 0 : c.getImages().size();
                    System.out.println("[DEBUG] condition[" + ci + "] imageCount = " + imgCount);
                    if (c != null && c.getImages() != null) {
                        for (int ii = 0; ii < c.getImages().size(); ii++) {
                            Object ip = c.getImages().get(ii).getImagePath();
                            System.out.println("[DEBUG] condition[" + ci + "].image[" + ii + "].imagePath = " + (ip == null ? "<null>" : ip.toString()));
                        }
                    }
                }
            }
            List<Map<String, Object>> conditionRows = buildConditionRows(dto);

            // DEBUG: conditionRows is always non-null (buildConditionRows returns a list)
            System.out.println("[DEBUG] conditionRows.size() = " + conditionRows.size());
            for (int i = 0; i < Math.min(conditionRows.size(), 5); i++) {
                Object p = conditionRows.get(i).get("imagePath");
                System.out.println("[DEBUG] conditionRows[" + i + "].imagePath = " + (p == null ? "<null>" : p.toString()));
            }

            Map<String, Object> params = new HashMap<>();
            params.put("inspectionTableSubreport", tableSubreport);
            params.put("inspectionConditionsSubreport", conditionsSubreport);
            // Prepare typed collections for JRMapCollectionDataSource to avoid generic mismatch
            @SuppressWarnings("unchecked")
            Collection<Map<String, ?>> tableData = (Collection<Map<String, ?>>) (Collection) tableRows;
            @SuppressWarnings("unchecked")
            Collection<Map<String, ?>> conditionsDataColl = (Collection<Map<String, ?>>) (Collection) conditionRows;

            params.put("tableData", new JRMapCollectionDataSource(tableData));
            params.put("conditionsData", new JRMapCollectionDataSource(conditionsDataColl));
            params.put("conditionImageRows", new JRMapCollectionDataSource(conditionsDataColl));

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

        // If there are no conditions provided, fall back to defaults
        if (conditions == null || conditions.isEmpty()) {
            conditions = defaultConditions();
        }

        List<Map<String, Object>> rows = new ArrayList<>();

        // For each condition, if it has images produce one row per image so the report can show them all.
        for (ConditionDto condition : conditions) {
            if (condition == null) continue;

            if (condition.getImages() != null && !condition.getImages().isEmpty()) {
                for (ConditionImageDto img : condition.getImages()) {
                    rows.add(toConditionRow(condition, img));
                }
            } else {
                // No images -> produce a single row with default image
                rows.add(toConditionRow(condition, null));
            }
        }

        return rows;
    }

    private Map<String, Object> toConditionRow(ConditionDto condition) {
        // Keep backward-compatible single-image behavior by using the first image if present
        ConditionImageDto firstImage = (condition != null && condition.getImages() != null && !condition.getImages().isEmpty())
                ? condition.getImages().get(0)
                : null;
        return toConditionRow(condition, firstImage);
    }

    private Map<String, Object> toConditionRow(ConditionDto condition, ConditionImageDto image) {
        Map<String, Object> row = new HashMap<>();
        if (condition == null) return row;

        row.put("rowNumber", condition.getRowNumber());
        row.put("title", buildConditionTitle(condition));
        row.put("conditionText", condition.getConditionText());
        row.put("noteText", buildConditionNote(condition));

        // Determine image path: prefer the provided image, else fall back to any image in condition, then to default
        Object imagePath = null;
        if (image != null && image.getImagePath() != null) {
            imagePath = image.getImagePath();
        } else if (condition.getImages() != null) {
            for (ConditionImageDto ci : condition.getImages()) {
                if (ci != null && ci.getImagePath() != null) {
                    imagePath = ci.getImagePath();
                    break;
                }
            }
        }
        if (imagePath == null) {
            imagePath = resourceUrlOrNull("classpath:report/front-image.png");
        }

        row.put("imagePath", imagePath);
        // Add optional image-specific metadata so templates can render captions or notes per image
        // ConditionImageDto fields are: rowNumber, conditionText, imagePath, noteText
        row.put("imageDescription", image != null ? safe(image.getConditionText(), "") : "");
        row.put("imageNote", image != null ? safe(image.getNoteText(), "") : "");

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
        ConditionDto tt = new ConditionDto(
                1,
                "مطلوب استكمال اعمال العزل حسب المخطط المعتمد.",
                Boolean.FALSE,
                Boolean.TRUE,
                "سكني",
                1,
                "الواجهة الامامية",
                Collections.singletonList(image1)
        );

        ConditionDto tt22 = new ConditionDto(
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

        return Arrays.asList(first, second, tt, tt22);
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
