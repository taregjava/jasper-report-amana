package com.jamilxt.java_springboot_japserreport.service.report;

import com.jamilxt.java_springboot_japserreport.dto.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProTipReportService {

    private final ResourceLoader resourceLoader;

    public ProTipReportService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
// imports omitted for brevity (JasperCompileManager, JasperFillManager, JasperExportManager, JRBeanCollectionDataSource, JasperReport, JasperPrint, etc.)
    public byte[] generateProjectStartPdfStatic() throws Exception {
    return generateOCVerificationPdfStatic(StageReportProfile.PRO_TIP);
    }
    public byte[] generateOCVerificationPdfStatic(StageReportProfile profile) throws Exception {
        // Load and compile subreports + master
        try (
                InputStream headerIs = resourceLoader.getResource(profile.reportPath("report_header")).getInputStream();
                InputStream ownerIs = resourceLoader.getResource(profile.reportPath("report_owner")).getInputStream();
                InputStream bodyIs = resourceLoader.getResource(profile.reportPath("report_body")).getInputStream();
                InputStream buildingIs = resourceLoader.getResource(profile.reportPath("report_building")).getInputStream();
                InputStream boundaryComplianceIs = resourceLoader.getResource(profile.reportPath("boundary_compliance")).getInputStream();
                InputStream buildingComponentsIs = resourceLoader.getResource(profile.reportPath("building_components")).getInputStream();
                InputStream footerIs = resourceLoader.getResource(profile.reportPath("shared_footer")).getInputStream();
                InputStream mainImagesIs = resourceLoader.getResource(profile.reportPath("main_images")).getInputStream();
                InputStream requirementsPageIs = resourceLoader.getResource(profile.reportPath("requirements_page")).getInputStream();
                InputStream exteriorPhotosIs = resourceLoader.getResource(profile.reportPath("exterior_photos_page")).getInputStream();
                InputStream internalPhotosIs = resourceLoader.getResource(profile.reportPath("internal_photos_page")).getInputStream();
                InputStream inspectionResponsibilityIs = resourceLoader.getResource(profile.reportPath("inspection_responsibility")).getInputStream();
                InputStream masterIs = resourceLoader.getResource(profile.reportPath("report_master")).getInputStream()
        ) {
            JasperReport headerRep = JasperCompileManager.compileReport(headerIs);
            JasperReport ownerRep  = ownerIs == null ? null : JasperCompileManager.compileReport(ownerIs);
            JasperReport bodyRep   = JasperCompileManager.compileReport(bodyIs);
            JasperReport buildingRep = JasperCompileManager.compileReport(buildingIs);
            JasperReport boundaryComplianceRep = boundaryComplianceIs == null ? null : JasperCompileManager.compileReport(boundaryComplianceIs);
            JasperReport buildingComponentsRep = buildingComponentsIs == null ? null : JasperCompileManager.compileReport(buildingComponentsIs);
            JasperReport footerRep = footerIs == null ? null : JasperCompileManager.compileReport(footerIs);
            JasperReport mainImagesRep = mainImagesIs == null ? null : JasperCompileManager.compileReport(mainImagesIs);
            JasperReport requirementsPageRep = requirementsPageIs == null ? null : JasperCompileManager.compileReport(requirementsPageIs);
            JasperReport exteriorPhotosRep = exteriorPhotosIs == null ? null : JasperCompileManager.compileReport(exteriorPhotosIs);
            JasperReport internalPhotosRep = internalPhotosIs == null ? null : JasperCompileManager.compileReport(internalPhotosIs);
            JasperReport inspectionResponsibilityRep = inspectionResponsibilityIs == null ? null : JasperCompileManager.compileReport(inspectionResponsibilityIs);
            JasperReport masterRep = JasperCompileManager.compileReport(masterIs);

            // Build static grouped rows so static preview matches dynamic checklist design.
            List<Map<String,Object>> rows = new ArrayList<>(List.of(
                    tableRow("متطلبات سور الحماية", "2", 1, "التأكد من سلامة الأساسات", "☑", "ملاحظة توضيحية"),
                    tableRow("متطلبات سور الحماية", "2", 2, "مراجعة الرسومات", "☐", ""),
                    tableRow("متطلبات سور الحماية", "2", 3, "تحديد حدود الموقع بشكل صحيح", "☑", ""),
                    tableRow("متطلبات سور الحماية", "2", 4, "التأكد من ارتفاع السور حسب المواصفات", "☐", "يحتاج مراجعة"),

                    tableRow("متطلبات الأعمال الخرسانية", "3", 5, "اختبارات السلامة", "NA", ""),
                    tableRow("متطلبات الأعمال الخرسانية", "3", 6, "توثيق نتائج الفحص الموقعي", "☑", "تم الاستلام"),
                    tableRow("متطلبات الأعمال الخرسانية", "3", 7, "فحص جودة الخرسانة", "☑", ""),
                    tableRow("متطلبات الأعمال الخرسانية", "3", 8, "التأكد من نسب الخلط", "☐", "غير مطابق"),

                    tableRow("متطلبات الأساسات", "4", 9, "فحص التربة", "☑", ""),
                    tableRow("متطلبات الأساسات", "4", 10, "مطابقة العمق مع المخطط", "☑", ""),
                    tableRow("متطلبات الأساسات", "4", 11, "التأكد من العزل", "☐", "بحاجة تعديل"),

                    tableRow("متطلبات الهيكل الإنشائي", "5", 12, "مطابقة الأعمدة للمخططات", "☑", ""),
                    tableRow("متطلبات الهيكل الإنشائي", "5", 13, "فحص الكمرات", "☑", ""),
                    tableRow("متطلبات الهيكل الإنشائي", "5", 14, "التأكد من استقامة العناصر", "☐", ""),

                    tableRow("متطلبات السلامة العامة", "6", 15, "توفر معدات السلامة", "☑", ""),
                    tableRow("متطلبات السلامة العامة", "6", 16, "وجود لوحات تحذيرية", "☐", "غير كافي")
            ));
            JRBeanCollectionDataSource tableDs = new JRBeanCollectionDataSource(rows);
            BoundaryComplianceDTO staticBoundaryCompliance = buildStaticBoundaryCompliance();
            BuildingComponentsDTO staticBuildingComponents = buildStaticBuildingComponents();

            Map<String,Object> params = new HashMap<>();

            params.put(
                    "stageReportNumber",
                 profile.reportNumberLabel()
            );

            params.put(
                    "stageReportTitle",profile.stageTitle()
            );
            // pass compiled subreports
            params.put("headerSubreport", headerRep);
            params.put("ownerSubreport", ownerRep);
            params.put("bodySubreport", bodyRep);
            params.put("buildingSubreport", buildingRep);
            params.put("boundaryComplianceSubreport", boundaryComplianceRep);
            params.put("buildingComponentsSubreport", buildingComponentsRep);
            params.put("footerSubreport", footerRep);
            params.put("mainImagesSubreport", mainImagesRep);
            params.put("requirementsPageSubreport", requirementsPageRep);
            params.put("exteriorPhotosSubreport", exteriorPhotosRep);
            params.put("internalPhotosSubreport", internalPhotosRep);
            params.put("inspectionResponsibilitySubreport", inspectionResponsibilityRep);


            // pass table datasource
            params.put("requirementsData", new JRBeanCollectionDataSource(rows));
            params.put("boundaryComplianceData", staticBoundaryCompliance);
            params.put("buildingComponentsData", staticBuildingComponents);
            // Changes page removed from OC verification flow.
            InspectionResponsibilityDTO staticInspection = buildStaticInspectionResponsibility();
            params.put("inspectionResponsibilityData", buildInspectionResponsibilityDataSource(staticInspection));
            params.put("hasInspectionResponsibilityData", hasInspectionResponsibilityData(staticInspection));
            params.put("hasRequirementsPage", !rows.isEmpty());

            // header/body params (example)
            // load logo as byte[] so Jasper/iText can recognize the image format reliably during export
            try (InputStream logoIs = getClass().getResourceAsStream("/report/logoSite.png")) {
                if (logoIs != null) {
                    byte[] logoBytes = logoIs.readAllBytes();
                    params.put("reportLogo", logoBytes);
                }
            } catch (Exception ignore) {
                // ignore missing logo
            }
            params.put("projectNameAndAddress", "مشروع نموذجي - شارع المثال");
            params.put("reportDate", "1447/01/01 هـ");
            params.put("ownerName", "محمود محمد");
            params.put("ownerIdNumber", "1234567890");
            params.put("ownerMobile", "0500000000");
            params.put("deedNumber", "D-2026-999");
            params.put("designingEngineeringOffice", "مكتب التصميم العالمي");
            params.put("contractorRecord", "CR-998877");
            params.put("contractorMobile", "0550000000");
            params.put("supervisingEngineerName", "م. علي الحربي");
            params.put("stageInspectionResult", "مقبول");
            params.put("stageInspectionNotes", "كل العناصر مطابقة مع ملاحظات طفيفة.");
            params.put("stageInspectionAccepted", Boolean.TRUE);
            params.put("stageInspectionRejected", Boolean.FALSE);
            params.put("stageInspectionHasNotes", Boolean.FALSE);
            params.put("contractorName", "شركة المقاول المحدودة");
            params.put("supervisingEngineeringOffice", "المكتب الهندسي النموذجي");
            params.put("buildingType", "سكني");
            params.put("buildingTypeResidential", Boolean.TRUE);
            params.put("buildingTypeResidentialCommercial", Boolean.FALSE);
            params.put("buildingTypeCommercial", Boolean.FALSE);
            params.put("buildingTypeVilla", Boolean.FALSE);
            params.put("buildingTypeOther", Boolean.FALSE);
            params.put("otherBuildingType", "");
            params.put("buildingCondition", "جيد");
            params.put("buildingDescription", "وصف نموذجي للمبنى...");
            params.put("licenseNumber", "LIC-001");
            params.put("licenseDate", "1446/12/15");
            params.put("planNumber", "PLN-88");
            params.put("pieceNumber", "45");
            params.put("floorsCount", "2");
            params.put("district", "حي النور");
            params.put("street", "شارع الأمير");
            params.put("sector", "قطاع الشمال");
            params.put("landNumber", "245");
            params.put("blockNumber", "17");
            params.put("areaByDeed", 350.0d);
            params.put("areaByLicense", 340.0d);
            params.put("areaByNature", 338.5d);
            params.put("isFullyFinished", Boolean.TRUE);
            params.put("isPartiallyBuilt", Boolean.FALSE);
            // sample photos section params
            params.put("spatialPortalPhoto", resolveImageSource("classpath:report/siteMapImage.png"));
            params.put("implementationPhoto", resolveImageSource("classpath:report/front-image.png"));
            params.put("aerialPhoto", resolveImageSource("classpath:report/logoSite.png"));
            params.put("implementationPhoto1", resolveImageSource("classpath:report/front-image.png"));
            params.put("implementationPhoto2", resolveImageSource("classpath:report/siteMapImage.png"));
            params.put("implementationPhoto3", resolveImageSource("classpath:report/front-image.png"));
            params.put("implementationPhoto4", resolveImageSource("classpath:report/logoSite.png"));
            params.put("detailPhoto1", resolveImageSource("classpath:report/front-image.png"));
            params.put("detailPhoto2", resolveImageSource("classpath:report/siteMapImage.png"));
            params.put("detailPhoto3", resolveImageSource("classpath:report/logoSite.png"));
            params.put("detailPhoto4", resolveImageSource("classpath:report/front-image.png"));
            params.put("detailPhoto5", resolveImageSource("classpath:report/siteMapImage.png"));
            params.put("detailPhoto6", resolveImageSource("classpath:report/logoSite.png"));
            params.put("detailDescription1", "   توضيح الصورة توضيح الصورة توضيح الصورة توضيح الصورة توضيح الصورة توضيح الصورة توضيح الصورة 1 الصورة توضيح الصورة توضيح الصورة توضيح الصورة 1 الصورة توضيح الصورة توضيح الصورة توضيح الصورة 1 ");
            params.put("detailDescription2", "توضيح الصورة 2");
            params.put("detailDescription3", "توضيح الصورة 3");
            params.put("detailDescription4", "توضيح الصورة 4");
            params.put("detailDescription5", "توضيح الصورة 5");
            params.put("detailDescription6", "توضيح الصورة 6");
            params.put("officeName", "المكتب الهندسي النموذجي");
            params.put("digitalStampPath", resolveImageSource("classpath:report/logo.png"));
            params.put("hasExteriorPhotosPage", hasAnyPhoto(
                    params.get("detailPhoto1"), params.get("detailPhoto2"), params.get("detailPhoto3"),
                    params.get("detailPhoto4"), params.get("detailPhoto5"), params.get("detailPhoto6"),
                    params.get("implementationPhoto1"), params.get("implementationPhoto2"),
                    params.get("implementationPhoto3"), params.get("implementationPhoto4"),
                    params.get("spatialPortalPhoto"), params.get("implementationPhoto"), params.get("aerialPhoto")
            ));
            params.put("hasInternalPhotosPage", hasAnyPhoto(
                    params.get("detailPhoto1"), params.get("detailPhoto2"), params.get("detailPhoto3"),
                    params.get("detailPhoto4"), params.get("detailPhoto5"), params.get("detailPhoto6")
            ));

            // Build photo datasources after photo params are filled.
            params.put("exteriorPhotosData", buildExteriorPhotosData(params));
            params.put("internalPhotosData", buildInternalPhotosData(params));
            // photos top/bottom grid page was removed for OC verification.

            JasperPrint jasperPrint = JasperFillManager.fillReport(masterRep, params, new JREmptyDataSource());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
            return baos.toByteArray();
        }
    }



    private Map<String, Object> tableRow(String groupTitle, String sideNumber, Integer index, String task, String status, String notes) {
        Map<String, Object> row = new HashMap<>();
        String statusCode = normalizeChecklistStatus(status);
        boolean matched = "MATCHED".equals(statusCode);
        boolean notMatched = "NOT_MATCHED".equals(statusCode);
        boolean notApplicable = "NOT_APPLICABLE".equals(statusCode);

        // Use field names expected by the table subreport/master: itemNo and description
        row.put("groupTitle", groupTitle == null ? "" : groupTitle);
        row.put("sideNumber", sideNumber == null ? "" : sideNumber);
        row.put("itemNo", index);
        row.put("description", task);
        row.put("status", status == null ? "" : status);
        row.put("statusCode", statusCode);
        row.put("isMatched", matched);
        row.put("isNotMatched", notMatched);
        row.put("isNotApplicable", notApplicable);

        row.put("notes", notes == null ? "" : notes);
        return row;
    }

    private String normalizeChecklistStatus(String status) {
        String value = safeText(status).trim().toLowerCase();
        if (value.isEmpty()) {
            return "";
        }

        if ("matched".equals(value)
                || "match".equals(value)
                || "compliant".equals(value)
                || "yes".equals(value)
                || "true".equals(value)
                || "☑".equals(value)
                || "✔".equals(value)
                || "✓".equals(value)
                || "مطابق".equals(value)) {
            return "MATCHED";
        }

        if ("not_matched".equals(value)
                || "not-matched".equals(value)
                || "not matched".equals(value)
                || "non_compliant".equals(value)
                || "non-compliant".equals(value)
                || "notcompliant".equals(value)
                || "no".equals(value)
                || "false".equals(value)
                || "☐".equals(value)
                || "✖".equals(value)
                || "✗".equals(value)
                || "x".equals(value)
                || "غير مطابق".equals(value)
                || "غيرمطابق".equals(value)) {
            return "NOT_MATCHED";
        }

        if ("not_applicable".equals(value)
                || "not-applicable".equals(value)
                || "not applicable".equals(value)
                || "na".equals(value)
                || "n/a".equals(value)
                || "لا ينطبق".equals(value)
                || "لاينطبق".equals(value)) {
            return "NOT_APPLICABLE";
        }

        return "";
    }

    private Object resolveImageSource(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }
        String value = source.trim();
        if (value.startsWith("classpath:")) {
            try (InputStream is = resourceLoader.getResource(value).getInputStream()) {
                return is.readAllBytes();
            } catch (Exception ignore) {
                return null;
            }
        }
        return value;
    }



    private String safeText(String value) {
        return value == null ? "" : value;
    }

    private boolean resolveStageFlag(Boolean explicitValue, String legacyStageResult, String... matches) {
        if (explicitValue != null) {
            return explicitValue;
        }
        String value = safeText(legacyStageResult).trim().toLowerCase();
        if (value.isEmpty() || matches == null) {
            return false;
        }
        for (String match : matches) {
            if (match != null && value.equals(match.trim().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private boolean resolveBuildingTypeFlag(Boolean explicitValue, String legacyBuildingType, String... aliases) {
        if (explicitValue != null) {
            return explicitValue;
        }
        String normalizedValue = normalizeFlagText(legacyBuildingType);
        if (normalizedValue.isEmpty() || aliases == null) {
            return false;
        }
        for (String alias : aliases) {
            if (normalizeFlagText(alias).equals(normalizedValue)) {
                return true;
            }
        }
        return false;
    }

    private String normalizeFlagText(String text) {
        return safeText(text).replace("-", "").replace(" ", "").trim().toLowerCase();
    }

    private boolean hasAnyPhoto(Object... values) {
        if (values == null) {
            return false;
        }
        for (Object value : values) {
            if (value == null) {
                continue;
            }
            if (value instanceof byte[]) {
                if (((byte[]) value).length > 0) {
                    return true;
                }
                continue;
            }
            if (value instanceof String) {
                if (!((String) value).trim().isEmpty()) {
                    return true;
                }
                continue;
            }
            return true;
        }
        return false;
    }

    private JRBeanCollectionDataSource buildTopPhotoRows(Map<String, Object> params) {
        List<Map<String, Object>> rows = new ArrayList<>();
        rows.add(photoRow("صور أعمال التنفيذ 1", params.get("implementationPhoto1")));
        rows.add(photoRow("صور أعمال التنفيذ 2", params.get("implementationPhoto2")));
        rows.add(photoRow("صور أعمال التنفيذ 3", params.get("implementationPhoto3")));
        rows.add(photoRow("صور أعمال التنفيذ 4", params.get("implementationPhoto4")));
        return new JRBeanCollectionDataSource(rows);
    }

    private JRBeanCollectionDataSource buildBottomPhotoRows(Map<String, Object> params) {
        List<Map<String, Object>> rows = new ArrayList<>();
        rows.add(detailPhotoRow("1", params.get("detailPhoto1"), (String) params.get("detailDescription1")));
        rows.add(detailPhotoRow("2", params.get("detailPhoto2"), (String) params.get("detailDescription2")));
        rows.add(detailPhotoRow("3", params.get("detailPhoto3"), (String) params.get("detailDescription3")));
        rows.add(detailPhotoRow("4", params.get("detailPhoto4"), (String) params.get("detailDescription4")));
        rows.add(detailPhotoRow("5", params.get("detailPhoto5"), (String) params.get("detailDescription5")));
        rows.add(detailPhotoRow("6", params.get("detailPhoto6"), (String) params.get("detailDescription6")));

        rows.add(detailPhotoRow("7", params.get("detailPhoto1"), (String) params.get("detailDescription1")));
        rows.add(detailPhotoRow("8", params.get("detailPhoto2"), (String) params.get("detailDescription2")));
        rows.add(detailPhotoRow("9", params.get("detailPhoto3"), (String) params.get("detailDescription3")));

        return new JRBeanCollectionDataSource(rows);
    }

    private JRBeanCollectionDataSource buildExteriorPhotosData(Map<String, Object> params) {
        List<Map<String, Object>> base = new ArrayList<>();
        base.add(exteriorPhotoRow("1", params.get("detailPhoto1"), (String) params.get("detailDescription1")));
        base.add(exteriorPhotoRow("2", params.get("detailPhoto2"), (String) params.get("detailDescription2")));
        base.add(exteriorPhotoRow("3", params.get("detailPhoto3"), (String) params.get("detailDescription3")));
        base.add(exteriorPhotoRow("4", params.get("detailPhoto4"), (String) params.get("detailDescription4")));
        base.add(exteriorPhotoRow("5", params.get("detailPhoto5"), (String) params.get("detailDescription5")));
        base.add(exteriorPhotoRow("6", params.get("detailPhoto6"), (String) params.get("detailDescription6")));
        base.add(exteriorPhotoRow("7", params.get("implementationPhoto1"), ""));
        base.add(exteriorPhotoRow("8", params.get("implementationPhoto2"), ""));
        base.add(exteriorPhotoRow("9", params.get("implementationPhoto3"), ""));
        base.add(exteriorPhotoRow("10", params.get("implementationPhoto4"), ""));
        base.add(exteriorPhotoRow("11", params.get("spatialPortalPhoto"), ""));

        // Keep visual numbering RTL per row (e.g. 3,2,1 then 6,5,4).
        List<Map<String, Object>> ordered = new ArrayList<>();
        for (int i = 0; i < base.size(); i += 3) {
            int end = Math.min(i + 3, base.size());
            for (int j = end - 1; j >= i; j--) {
                ordered.add(base.get(j));
            }
        }
        return new JRBeanCollectionDataSource(ordered);
    }

    private JRBeanCollectionDataSource buildInternalPhotosData(Map<String, Object> params) {
        List<Map<String, Object>> rows = new ArrayList<>();
        rows.add(exteriorPhotoRow("1", params.get("detailPhoto1"), (String) params.get("detailDescription1")));
        rows.add(exteriorPhotoRow("2", params.get("detailPhoto2"), (String) params.get("detailDescription2")));
        rows.add(exteriorPhotoRow("3", params.get("detailPhoto3"), (String) params.get("detailDescription3")));
        rows.add(exteriorPhotoRow("4", params.get("detailPhoto4"), (String) params.get("detailDescription4")));
        rows.add(exteriorPhotoRow("5", params.get("detailPhoto5"), (String) params.get("detailDescription5")));
        rows.add(exteriorPhotoRow("6", params.get("detailPhoto6"), (String) params.get("detailDescription6")));
        return new JRBeanCollectionDataSource(rows);
    }

    private Map<String, Object> photoRow(String title, Object image) {
        Map<String, Object> row = new HashMap<>();
        row.put("title", title);
        row.put("image", image);
        return row;
    }

    private Map<String, Object> exteriorPhotoRow(String number, Object image, String description) {
        Map<String, Object> row = new HashMap<>();
        row.put("number", number);
        row.put("image", image);
        row.put("description", safeText(description));
        return row;
    }

    private Map<String, Object> detailPhotoRow(String number, Object image, String description) {
        Map<String, Object> row = new HashMap<>();
        row.put("number", number);
        row.put("image", image);
        row.put("description", safeText(description));
        return row;
    }

    private InspectionResponsibilityDTO buildStaticInspectionResponsibility() {
        InspectionResponsibilityDTO dto = new InspectionResponsibilityDTO();
        dto.setSupervisorName("م. أحمد عبدالله");
        dto.setSupervisorIdOrLicense("LIC-445522");
        dto.setSupervisorSignaturePath("classpath:report/logo.png");
        dto.setOfficeStampPath("classpath:report/logo.png");
        dto.setInspectionNotes(List.of(
                "يلزم استكمال حواجز السلامة في منطقة السطح.",
                "مراجعة مطابقة التمديدات الكهربائية مع المخطط.",
                "تقديم صور توثيقية إضافية بعد الإغلاق النهائي."
        ));
        dto.setInspectionStatus("PENDING");
        dto.setInspectorName("م. خالد العتيبي");
        dto.setInspectorIdOrLicense("INSP-223311");
        dto.setInspectorSignaturePath("classpath:report/logo.png");
        dto.setInspectionBodyStampPath("classpath:report/logo.png");
        return dto;
    }

    private JRDataSource buildInspectionResponsibilityDataSource(InspectionResponsibilityDTO data) {
        InspectionResponsibilityDTO dto = data == null ? new InspectionResponsibilityDTO() : data;
        List<String> notes = dto.getInspectionNotes() == null ? new ArrayList<>() : new ArrayList<>(dto.getInspectionNotes());
        while (notes.size() < 12) {
            notes.add("");
        }

        Map<String, Object> row = new HashMap<>();
        row.put("supervisorName", safeText(dto.getSupervisorName()));
        row.put("supervisorIdOrLicense", safeText(dto.getSupervisorIdOrLicense()));
        row.put("supervisorSignaturePath", resolveImageSource(dto.getSupervisorSignaturePath()));
        row.put("officeStampPath", resolveImageSource(dto.getOfficeStampPath()));
        row.put("inspectionNotes", notes);
        row.put("inspectionStatus", safeText(dto.getInspectionStatus()));
        row.put("inspectorName", safeText(dto.getInspectorName()));
        row.put("inspectorIdOrLicense", safeText(dto.getInspectorIdOrLicense()));
        row.put("inspectorSignaturePath", resolveImageSource(dto.getInspectorSignaturePath()));
        row.put("inspectionBodyStampPath", resolveImageSource(dto.getInspectionBodyStampPath()));
        return new JRBeanCollectionDataSource(List.of(row));
    }

    private boolean hasInspectionResponsibilityData(InspectionResponsibilityDTO dto) {
        if (dto == null) {
            return false;
        }
        if (!safeText(dto.getSupervisorName()).isEmpty()) return true;
        if (!safeText(dto.getSupervisorIdOrLicense()).isEmpty()) return true;
        if (!safeText(dto.getSupervisorSignaturePath()).isEmpty()) return true;
        if (!safeText(dto.getOfficeStampPath()).isEmpty()) return true;
        if (!safeText(dto.getInspectionStatus()).isEmpty()) return true;
        if (!safeText(dto.getInspectorName()).isEmpty()) return true;
        if (!safeText(dto.getInspectorIdOrLicense()).isEmpty()) return true;
        if (!safeText(dto.getInspectorSignaturePath()).isEmpty()) return true;
        if (!safeText(dto.getInspectionBodyStampPath()).isEmpty()) return true;
        if (dto.getInspectionNotes() != null) {
            for (String note : dto.getInspectionNotes()) {
                if (note != null && !note.trim().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Builds a JRDataSource for the changes / extra-items row subreport.
     * The list is capped and padded with empty strings so that always exactly
     * {@code CHANGES_ROW_COUNT} rows are rendered, keeping the fixed 8-row table grid intact.
     */
    private static final int CHANGES_ROW_COUNT = 8;

    private JRDataSource buildTextRowsDataSource(List<String> items) {
        List<Map<String, Object>> rows = new ArrayList<>();
        if (items != null) {
            for (String item : items) {
                if (rows.size() >= CHANGES_ROW_COUNT) {
                    break;
                }
                Map<String, Object> row = new HashMap<>();
                row.put("rowText", item == null ? "" : item);
                rows.add(row);
            }
        }
        // pad with empty strings to always reach CHANGES_ROW_COUNT rows
        while (rows.size() < CHANGES_ROW_COUNT) {
            Map<String, Object> row = new HashMap<>();
            row.put("rowText", "");
            rows.add(row);
        }
        return new JRBeanCollectionDataSource(rows);
    }

    // ============ Boundary Compliance & Building Components Helpers ============

    /**
     * Builds a static BoundaryComplianceDTO with sample data
     */
    private BoundaryComplianceDTO buildStaticBoundaryCompliance() {
        BoundaryComplianceDTO dto = new BoundaryComplianceDTO();
        
        // Setbacks (Boundary distances from property limits)
        dto.setNorthSetback(new BoundaryComplianceDTO.SetbackDirection(
            "شمال", "شارع", 5.0, 5.0, 5.0, 0.0, true, "مطابق"
        ));
        dto.setSouthSetback(new BoundaryComplianceDTO.SetbackDirection(
            "جنوب", "جار", 3.0, 3.0, 2.8, 0.0, false, "ناقص بـ 20 سم"
        ));
        dto.setEastSetback(new BoundaryComplianceDTO.SetbackDirection(
            "شرق", "شارع ثانوي", 4.0, 4.0, 4.0, 0.0, true, ""
        ));
        dto.setWestSetback(new BoundaryComplianceDTO.SetbackDirection(
            "غرب", "جار", 3.5, 3.5, 3.5, 0.0, true, "مطابق"
        ));
        
        // Protrusions (Balconies, overhangs)
        dto.setNorthProtrusion(new BoundaryComplianceDTO.ProtrustionDirection(
            "شمال", "بلكونة", 1.5, 1.5, 1.6, 0.1, false, "زيادة 10 سم غير مسموح"
        ));
        dto.setSouthProtrusion(new BoundaryComplianceDTO.ProtrustionDirection(
            "جنوب", "شمسية", 1.0, 1.0, 1.0, 0.0, true, ""
        ));
        dto.setEastProtrusion(new BoundaryComplianceDTO.ProtrustionDirection(
            "شرق", "بلكونة", 1.5, 1.5, 1.5, 0.0, true, "مطابق"
        ));
        dto.setWestProtrusion(new BoundaryComplianceDTO.ProtrustionDirection(
            "غرب", "كسرة", 0.8, 0.8, 0.8, 0.0, true, ""
        ));
        
        return dto;
    }

    /**
     * Builds a static BuildingComponentsDTO with sample data
     */
    private BuildingComponentsDTO buildStaticBuildingComponents() {
        BuildingComponentsDTO dto = new BuildingComponentsDTO();
        
        // Building floors
        dto.setBasement(new BuildingComponentsDTO.ComponentVerification(
            "قبو", "مستودع", 1, 2.8, 350.0, 350.0, true, ""
        ));
        dto.setGroundFloor(new BuildingComponentsDTO.ComponentVerification(
            "الدور الأرضي", "مختلط", 4, 3.5, 400.0, 398.0, false, "ناقص بـ 2 م²"
        ));
        dto.setMezzanine(new BuildingComponentsDTO.ComponentVerification(
            "طابق الميزانين", "تجاري", null, null, null, null, false, true, "لا ينطبق على هذا المبنى"
        ));
        dto.setFirstFloor(new BuildingComponentsDTO.ComponentVerification(
            "الدور الأول", "سكني", 4, 3.2, 350.0, 350.0, true, "مطابق"
        ));
        dto.setSecondFloor(new BuildingComponentsDTO.ComponentVerification(
            "الدور الثاني", "سكني", 4, 3.2, 350.0, 350.0, true, ""
        ));
        dto.setThirdFloor(new BuildingComponentsDTO.ComponentVerification(
            "الدور الثالث", "سكني", 4, 3.2, 350.0, 350.0, true, ""
        ));
        dto.setFourthFloor(new BuildingComponentsDTO.ComponentVerification(
            "الدور الرابع", "سكني", 3, 3.2, 280.0, 280.0, true, ""
        ));
        dto.setRoofAnnex(new BuildingComponentsDTO.ComponentVerification(
            "الملحق العلوي", "خزان", 1, 2.0, 100.0, 100.0, true, ""
        ));
        dto.setStairs(new BuildingComponentsDTO.ComponentVerification(
            "درج", "دوران", 0, 3.5, 50.0, 50.0, true, ""
        ));
        dto.setFences(new BuildingComponentsDTO.ComponentVerification(
            "أسوار", "حماية", 0, 2.0, 150.0, 150.0, true, ""
        ));
        dto.setElectricityRoom(new BuildingComponentsDTO.ComponentVerification(
            "غرفة كهرباء", "خدمات", 1, 2.5, 20.0, 20.0, true, ""
        ));
        
        // Parking
        dto.setParking(new BuildingComponentsDTO.ParkingVerification(
            15, 14, false, "ناقص مقف واحد"
        ));
        
        // Utilities
        dto.setElectricityBox(new BuildingComponentsDTO.UtilityRoomVerification(
            3.0, 2.0, 2.9, 2.0, true, "مطابق"
        ));
        
        return dto;
    }


//    private static final class StageCompiledReports {
//        private final JasperReport header;
//        private final JasperReport owner;
//        private final JasperReport body;
//        private final JasperReport building;
//        private final JasperReport table;
//        private final JasperReport photos;
//        private final JasperReport sitePhotos;
//        private final JasperReport photosTopGrid;
//        private final JasperReport photosBottomGrid;
//        private final JasperReport footer;
//        private final JasperReport mainImages;
//        private final JasperReport changesRows;
//        private final JasperReport changesPage;
//        private final JasperReport inspectionResponsibility;
//        private final JasperReport stageBuildingComponentsSubreport;
//        private final JasperReport master;
//
//        private StageCompiledReports(
//                JasperReport header,
//                JasperReport owner,
//                JasperReport body,
//                JasperReport building,
//                JasperReport table,
//                JasperReport photos,
//                JasperReport sitePhotos,
//                JasperReport photosTopGrid,
//                JasperReport photosBottomGrid,
//                JasperReport footer,
//                JasperReport mainImages,
//                JasperReport changesRows,
//                JasperReport changesPage,
//                JasperReport inspectionResponsibility,
//                JasperReport stageBuildingComponentsSubreport,
//                JasperReport master
//        ) {
//            this.header = header;
//            this.owner = owner;
//            this.body = body;
//            this.building = building;
//            this.table = table;
//            this.photos = photos;
//            this.sitePhotos = sitePhotos;
//            this.photosTopGrid = photosTopGrid;
//            this.photosBottomGrid = photosBottomGrid;
//            this.footer = footer;
//            this.mainImages = mainImages;
//            this.changesRows = changesRows;
//            this.changesPage = changesPage;
//            this.inspectionResponsibility = inspectionResponsibility;
//            this.stageBuildingComponentsSubreport = stageBuildingComponentsSubreport;
//            this.master = master;
//        }
//    }

}
