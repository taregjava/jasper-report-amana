package com.jamilxt.java_springboot_japserreport.service.report;

import com.jamilxt.java_springboot_japserreport.dto.ProjectStartReportDto;
import com.jamilxt.java_springboot_japserreport.dto.OwnerInfo;
import com.jamilxt.java_springboot_japserreport.dto.BuildingInfo;
import com.jamilxt.java_springboot_japserreport.dto.ProjectPhotosDTO;
import com.jamilxt.java_springboot_japserreport.dto.TaskRow;
import com.jamilxt.java_springboot_japserreport.dto.InspectionResponsibilityDTO;
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
public class ProjectStartReportService {

    private final ResourceLoader resourceLoader;

    public ProjectStartReportService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
// imports omitted for brevity (JasperCompileManager, JasperFillManager, JasperExportManager, JRBeanCollectionDataSource, JasperReport, JasperPrint, etc.)

    public byte[] generateProjectStartPdfStatic() throws Exception {
        return generateStatic(StageReportProfile.PROJECT_START);
    }

    public byte[] generateStatic(StageReportProfile profile) throws Exception {
        StageCompiledReports reports = compileStageReports(profile);

            // Build static grouped rows so static preview matches dynamic checklist design.
            List<Map<String,Object>> rows = new ArrayList<>(java.util.List.of(
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

            Map<String, Object> params = createBaseParams(reports, tableDs);
            params.put("stageReportNumber", profile.reportNumberLabel());
            params.put("stageReportTitle", profile.stageTitle());
            // static sample data for changes tables
            params.put("changesData",    buildTextRowsDataSource(java.util.List.of(
                    "تعديل موقع النافذة في الواجهة الشمالية",
                    "تغيير مقاس باب المدخل الرئيسي من 100 إلى 120 سم"
            )));
            params.put("extraItemsData", buildTextRowsDataSource(java.util.List.of(
                    "لم يتم تركيب حواجز السلامة على السطح"
            )));
            InspectionResponsibilityDTO staticInspection = buildStaticInspectionResponsibility();
            params.put("inspectionResponsibilityData", buildInspectionResponsibilityDataSource(staticInspection));
            params.put("hasInspectionResponsibilityData", hasInspectionResponsibilityData(staticInspection));

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

            // Build photo datasources after photo params are filled.
            params.put("photosTopData", buildTopPhotoRows(params));
            params.put("photosBottomData", buildBottomPhotoRows(params));

            JasperPrint jasperPrint = JasperFillManager.fillReport(reports.master, params, new net.sf.jasperreports.engine.JREmptyDataSource());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
            return baos.toByteArray();
    }

    public byte[] generatePdf(ProjectStartReportDto dto) throws JRException {
        return generatePdf(dto, StageReportProfile.PROJECT_START);
    }

    public byte[] generatePdf(ProjectStartReportDto dto, StageReportProfile profile) throws JRException {
        try {
            StageCompiledReports reports = compileStageReports(profile);

            List<Map<String, Object>> tableRows = buildTableRows(dto);

            Map<String, Object> params = createBaseParams(reports, new JRBeanCollectionDataSource(tableRows));
            params.put("stageReportNumber", profile.reportNumberLabel());
            params.put("stageReportTitle", profile.stageTitle());
            // changes & extra items (padded to 8 rows)
            params.put("changesData",    buildTextRowsDataSource(dto.getChanges()));
            params.put("extraItemsData", buildTextRowsDataSource(dto.getExtraItems()));
            params.put("inspectionResponsibilityData", buildInspectionResponsibilityDataSource(dto.getInspectionResponsibility()));
            params.put("hasInspectionResponsibilityData", hasInspectionResponsibilityData(dto.getInspectionResponsibility()));

            // owner/building values: prefer OwnerInfo / BuildingInfo when present
            String ownerName = null;
            OwnerInfo oi = dto.getOwnerInfo();
            if (oi != null) ownerName = oi.getOwnerName();
            if (ownerName == null) ownerName = "";
            String buildingType = null;
            BuildingInfo bi = dto.getBuildingInfo();
            if (bi != null) buildingType = bi.getBuildingType();
            if (buildingType == null) buildingType = "";

            params.put("ownerName", ownerName);
            params.put("buildingType", buildingType);
            String legacyBuildingType = bi == null ? "" : bi.getBuildingType();
            params.put("buildingTypeResidential", resolveBuildingTypeFlag(
                    bi == null ? null : bi.getBuildingTypeResidential(),
                    legacyBuildingType,
                    "سكني", "residential", "residence"
            ));
            params.put("buildingTypeResidentialCommercial", resolveBuildingTypeFlag(
                    bi == null ? null : bi.getBuildingTypeResidentialCommercial(),
                    legacyBuildingType,
                    "سكني-تجاري", "سكني - تجاري", "residential-commercial", "residential commercial", "mixed"
            ));
            params.put("buildingTypeCommercial", resolveBuildingTypeFlag(
                    bi == null ? null : bi.getBuildingTypeCommercial(),
                    legacyBuildingType,
                    "تجاري", "commercial"
            ));
            params.put("buildingTypeVilla", resolveBuildingTypeFlag(
                    bi == null ? null : bi.getBuildingTypeVilla(),
                    legacyBuildingType,
                    "فيلا", "villa"
            ));
            params.put("buildingTypeOther", resolveOtherBuildingTypeFlag(
                    bi == null ? null : bi.getBuildingTypeOther(),
                    legacyBuildingType,
                    bi == null ? null : bi.getOtherBuildingType()
            ));
            // populate additional owner/building params using preferred parameter names
            params.put("ownerIdNumber", oi == null ? "" : (oi.getOwnerIdNumber() != null ? oi.getOwnerIdNumber() : oi.getIdNumber()));
            params.put("ownerMobile", oi == null ? "" : (oi.getOwnerMobile() != null ? oi.getOwnerMobile() : oi.getMobileNumber()));
            params.put("deedNumber", oi == null ? "" : oi.getDeedNumber());
            params.put("supervisingEngineeringOffice", oi == null ? "" : (oi.getSupervisingEngineeringOffice() != null ? oi.getSupervisingEngineeringOffice() : oi.getEngineeringOffice()));
            params.put("designingEngineeringOffice", oi == null ? "" : oi.getDesigningEngineeringOffice());
            params.put("contractorName", oi == null ? "" : oi.getContractorName());
            params.put("contractorRecord", oi == null ? "" : (oi.getContractorRecord() != null ? oi.getContractorRecord() : oi.getContractorLicense()));
            params.put("contractorMobile", oi == null ? "" : oi.getContractorMobile());
            params.put("projectNameAndAddress", oi == null ? "" : (oi.getProjectNameAndAddress() != null ? oi.getProjectNameAndAddress() : oi.getProjectName()));
            params.put("supervisingEngineerName", dto.getEngineerName() == null ? "" : dto.getEngineerName());
            params.put("reportDate", oi == null ? "" : oi.getReportDate());
            params.put("stageInspectionResult", dto.getStageResult() == null ? "" : dto.getStageResult());
            params.put("stageInspectionNotes", oi == null ? "" : oi.getStageInspectionNotes());
            params.put("stageInspectionAccepted", resolveStageFlag(dto.getStageInspectionAccepted(), dto.getStageResult(), "accepted", "مقبول"));
            params.put("stageInspectionRejected", resolveStageFlag(dto.getStageInspectionRejected(), dto.getStageResult(), "rejected", "مرفوض"));
            params.put("stageInspectionHasNotes", resolveStageFlag(dto.getStageInspectionHasNotes(), dto.getStageResult(), "notes", "ملاحظات"));

            params.put("buildingDescription", bi == null ? "" : bi.getBuildingDescription());
            // map building fields to expected names (best-effort)
            params.put("planNumber", bi == null ? "" : bi.getPlanNumber());
            params.put("pieceNumber", bi == null ? "" : bi.getPieceNumber());
            params.put("floorsCount", bi == null ? "" : bi.getFloorsCount());
            params.put("landNumber", bi == null ? "" : bi.getLandNumber());
            params.put("blockNumber", bi == null ? "" : bi.getBlockNumber());
            params.put("licenseNumber", bi == null ? "" : bi.getLicenseNumber());
            params.put("licenseDate", bi == null ? "" : bi.getLicenseDate());
            params.put("district", bi == null ? "" : bi.getDistrict());
            params.put("street", bi == null ? "" : bi.getStreet());
            params.put("sector", bi == null ? "" : bi.getSector());
            params.put("otherBuildingType", bi == null ? "" : bi.getOtherBuildingType());
            params.put("areaByDeed", bi == null ? null : (bi.getAreaByDeed() != null ? bi.getAreaByDeed() : bi.getAreaDeed()));
            params.put("areaByLicense", bi == null ? null : (bi.getAreaByLicense() != null ? bi.getAreaByLicense() : bi.getAreaLicense()));
            params.put("areaByNature", bi == null ? null : (bi.getAreaByNature() != null ? bi.getAreaByNature() : bi.getAreaNature()));
            params.put("isFullyFinished", bi != null && bi.isFullyBuilt());
            params.put("isPartiallyBuilt", bi != null && bi.isPartiallyBuilt());

            ProjectPhotosDTO photos = dto.getProjectPhotos();
            params.put("spatialPortalPhoto", photos == null ? null : resolveImageSource(photos.getSpatialPortalPhoto()));
            params.put("implementationPhoto", photos == null ? null : resolveImageSource(photos.getImplementationPhoto()));
            params.put("aerialPhoto", photos == null ? null : resolveImageSource(photos.getAerialPhoto()));
            params.put("implementationPhoto1", photos == null ? null : resolveImageSource(firstNonBlank(photos.getImplementationPhoto1(), photos.getImplementationPhoto())));
            params.put("implementationPhoto2", photos == null ? null : resolveImageSource(firstNonBlank(photos.getImplementationPhoto2(), photos.getSpatialPortalPhoto(), photos.getImplementationPhoto())));
            params.put("implementationPhoto3", photos == null ? null : resolveImageSource(firstNonBlank(photos.getImplementationPhoto3(), photos.getImplementationPhoto())));
            params.put("implementationPhoto4", photos == null ? null : resolveImageSource(firstNonBlank(photos.getImplementationPhoto4(), photos.getAerialPhoto(), photos.getImplementationPhoto())));
            params.put("detailPhoto1", photos == null ? null : resolveImageSource(firstNonBlank(photos.getDetailPhoto1(), photos.getImplementationPhoto1(), photos.getImplementationPhoto())));
            params.put("detailPhoto2", photos == null ? null : resolveImageSource(firstNonBlank(photos.getDetailPhoto2(), photos.getImplementationPhoto2(), photos.getSpatialPortalPhoto())));
            params.put("detailPhoto3", photos == null ? null : resolveImageSource(firstNonBlank(photos.getDetailPhoto3(), photos.getImplementationPhoto3(), photos.getAerialPhoto())));
            params.put("detailPhoto4", photos == null ? null : resolveImageSource(firstNonBlank(photos.getDetailPhoto4(), photos.getImplementationPhoto4(), photos.getImplementationPhoto())));
            params.put("detailPhoto5", photos == null ? null : resolveImageSource(firstNonBlank(photos.getDetailPhoto5(), photos.getSpatialPortalPhoto(), photos.getImplementationPhoto())));
            params.put("detailPhoto6", photos == null ? null : resolveImageSource(firstNonBlank(photos.getDetailPhoto6(), photos.getAerialPhoto(), photos.getImplementationPhoto())));
            params.put("detailDescription1", photos == null ? "" : safeText(photos.getDetailDescription1()));
            params.put("detailDescription2", photos == null ? "" : safeText(photos.getDetailDescription2()));
            params.put("detailDescription3", photos == null ? "" : safeText(photos.getDetailDescription3()));
            params.put("detailDescription4", photos == null ? "" : safeText(photos.getDetailDescription4()));
            params.put("detailDescription5", photos == null ? "" : safeText(photos.getDetailDescription5()));
            params.put("detailDescription6", photos == null ? "" : safeText(photos.getDetailDescription6()));
            params.put("officeName", photos == null ? "" : photos.getOfficeName());
            params.put("digitalStampPath", photos == null ? null : resolveImageSource(photos.getDigitalStampPath()));
            params.put("photosTopData", buildTopPhotoRows(params));
            params.put("photosBottomData", buildBottomPhotoRows(params));

            // load logo resource into report param if available
            try (InputStream is = resourceLoader.getResource("classpath:report/logoSite.png").getInputStream()) {
                byte[] logo = is.readAllBytes();
                params.put("reportLogo", logo);
            } catch (Exception ignore) {
                // ignore missing logo
            }

            // Master contains nested subreports and consumes `tableData` directly.
            JasperPrint print = JasperFillManager.fillReport(reports.master, params, new net.sf.jasperreports.engine.JREmptyDataSource());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(print, out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new JRException("Failed to generate " + profile.displayName() + " report", e);
        }
    }

    private StageCompiledReports compileStageReports(StageReportProfile profile) throws Exception {
        try (InputStream headerIs = resourceLoader.getResource(profile.reportPath("report_header")).getInputStream();
             InputStream ownerIs = resourceLoader.getResource(profile.reportPath("report_owner")).getInputStream();
             InputStream bodyIs = resourceLoader.getResource(profile.reportPath("report_body")).getInputStream();
             InputStream buildingIs = resourceLoader.getResource(profile.reportPath("report_building")).getInputStream();
             InputStream tableIs = resourceLoader.getResource(profile.reportPath("report_table")).getInputStream();
             InputStream photosIs = resourceLoader.getResource(profile.reportPath("report_photos")).getInputStream();
             InputStream sitePhotosIs = resourceLoader.getResource(profile.reportPath("site_photos_page")).getInputStream();
             InputStream photosTopGridIs = resourceLoader.getResource(profile.reportPath("report_photos_top_grid")).getInputStream();
             InputStream photosBottomGridIs = resourceLoader.getResource(profile.reportPath("report_photos_bottom_grid")).getInputStream();
             InputStream footerIs = resourceLoader.getResource(profile.reportPath("shared_footer")).getInputStream();
             InputStream mainImagesIs = resourceLoader.getResource(profile.reportPath("main_images")).getInputStream();
             InputStream changesRowsIs = resourceLoader.getResource(profile.reportPath("changes_rows")).getInputStream();
             InputStream changesPageIs = resourceLoader.getResource(profile.reportPath("changes_page")).getInputStream();
             InputStream inspectionResponsibilityIs = resourceLoader.getResource(profile.reportPath("inspection_responsibility")).getInputStream();
             InputStream stageBuildingComponentsIs = resourceLoader.getResource("classpath:report/prePouring/stage_building_components.jrxml").getInputStream();
             InputStream masterIs = resourceLoader.getResource(profile.reportPath("report_master")).getInputStream()) {

            return new StageCompiledReports(
                    JasperCompileManager.compileReport(headerIs),
                    JasperCompileManager.compileReport(ownerIs),
                    JasperCompileManager.compileReport(bodyIs),
                    JasperCompileManager.compileReport(buildingIs),
                    JasperCompileManager.compileReport(tableIs),
                    JasperCompileManager.compileReport(photosIs),
                    JasperCompileManager.compileReport(sitePhotosIs),
                    JasperCompileManager.compileReport(photosTopGridIs),
                    JasperCompileManager.compileReport(photosBottomGridIs),
                    JasperCompileManager.compileReport(footerIs),
                    JasperCompileManager.compileReport(mainImagesIs),
                    JasperCompileManager.compileReport(changesRowsIs),
                    JasperCompileManager.compileReport(changesPageIs),
                    JasperCompileManager.compileReport(inspectionResponsibilityIs),
                    JasperCompileManager.compileReport(stageBuildingComponentsIs),
                    JasperCompileManager.compileReport(masterIs)
            );
        }
    }


    private StageCompiledReports compileStageChanges(StageReportProfile profile) throws Exception {
        try (InputStream headerIs = resourceLoader.getResource(profile.reportPath("report_header")).getInputStream();
             InputStream ownerIs = resourceLoader.getResource(profile.reportPath("report_owner")).getInputStream();
             InputStream bodyIs = resourceLoader.getResource(profile.reportPath("report_body")).getInputStream();
             InputStream buildingIs = resourceLoader.getResource(profile.reportPath("report_building")).getInputStream();
             InputStream tableIs = resourceLoader.getResource(profile.reportPath("report_table")).getInputStream();
             InputStream photosIs = resourceLoader.getResource(profile.reportPath("report_photos")).getInputStream();
             InputStream sitePhotosIs = resourceLoader.getResource(profile.reportPath("site_photos_page")).getInputStream();
             InputStream photosTopGridIs = resourceLoader.getResource(profile.reportPath("report_photos_top_grid")).getInputStream();
             InputStream photosBottomGridIs = resourceLoader.getResource(profile.reportPath("report_photos_bottom_grid")).getInputStream();
             InputStream footerIs = resourceLoader.getResource(profile.reportPath("shared_footer")).getInputStream();
             InputStream mainImagesIs = resourceLoader.getResource(profile.reportPath("main_images")).getInputStream();
             InputStream changesRowsIs = resourceLoader.getResource(profile.reportPath("changes_rows")).getInputStream();
             InputStream changesPageIs = resourceLoader.getResource(profile.reportPath("changes_page")).getInputStream();
             InputStream inspectionResponsibilityIs = resourceLoader.getResource(profile.reportPath("inspection_responsibility")).getInputStream();
             InputStream stageBuildingComponentsIs = resourceLoader.getResource("classpath:report/prePouring/stage_building_components.jrxml").getInputStream();
             InputStream masterIs = resourceLoader.getResource(profile.reportPath("report_master")).getInputStream()) {

            return new StageCompiledReports(
                    JasperCompileManager.compileReport(headerIs),
                    JasperCompileManager.compileReport(ownerIs),
                    JasperCompileManager.compileReport(bodyIs),
                    JasperCompileManager.compileReport(buildingIs),
                    JasperCompileManager.compileReport(tableIs),
                    JasperCompileManager.compileReport(photosIs),
                    JasperCompileManager.compileReport(sitePhotosIs),
                    JasperCompileManager.compileReport(photosTopGridIs),
                    JasperCompileManager.compileReport(photosBottomGridIs),
                    JasperCompileManager.compileReport(footerIs),
                    JasperCompileManager.compileReport(mainImagesIs),
                    JasperCompileManager.compileReport(changesRowsIs),
                    JasperCompileManager.compileReport(changesPageIs),
                    JasperCompileManager.compileReport(inspectionResponsibilityIs),
                    JasperCompileManager.compileReport(stageBuildingComponentsIs),
                    JasperCompileManager.compileReport(masterIs)
            );
        }
    }


    private Map<String, Object> createBaseParams(StageCompiledReports reports, JRBeanCollectionDataSource tableData) {
        Map<String, Object> params = new HashMap<>();
        params.put("headerSubreport", reports.header);
        params.put("ownerSubreport", reports.owner);
        params.put("bodySubreport", reports.body);
        params.put("buildingSubreport", reports.building);
        params.put("tableSubreport", reports.table);
        params.put("photosSubreport", reports.photos);
        params.put("sitePhotosSubreport", reports.sitePhotos);
        params.put("photosTopGridSubreport", reports.photosTopGrid);
        params.put("photosBottomGridSubreport", reports.photosBottomGrid);
        params.put("footerSubreport", reports.footer);
        params.put("mainImagesSubreport", reports.mainImages);
        params.put("changesPageSubreport", reports.changesPage);
        params.put("changesRowsSubreport", reports.changesRows);
        params.put("inspectionResponsibilitySubreport", reports.inspectionResponsibility);
        params.put("stageBuildingComponentsSubreport", reports.stageBuildingComponentsSubreport);
        params.put("tableData", tableData);
        return params;
    }

    private static final class StageCompiledReports {
        private final JasperReport header;
        private final JasperReport owner;
        private final JasperReport body;
        private final JasperReport building;
        private final JasperReport table;
        private final JasperReport photos;
        private final JasperReport sitePhotos;
        private final JasperReport photosTopGrid;
        private final JasperReport photosBottomGrid;
        private final JasperReport footer;
        private final JasperReport mainImages;
        private final JasperReport changesRows;
        private final JasperReport changesPage;
        private final JasperReport inspectionResponsibility;
        private final JasperReport stageBuildingComponentsSubreport;
        private final JasperReport master;

        private StageCompiledReports(
                JasperReport header,
                JasperReport owner,
                JasperReport body,
                JasperReport building,
                JasperReport table,
                JasperReport photos,
                JasperReport sitePhotos,
                JasperReport photosTopGrid,
                JasperReport photosBottomGrid,
                JasperReport footer,
                JasperReport mainImages,
                JasperReport changesRows,
                JasperReport changesPage,
                JasperReport inspectionResponsibility,
                JasperReport stageBuildingComponentsSubreport,
                JasperReport master
        ) {
            this.header = header;
            this.owner = owner;
            this.body = body;
            this.building = building;
            this.table = table;
            this.photos = photos;
            this.sitePhotos = sitePhotos;
            this.photosTopGrid = photosTopGrid;
            this.photosBottomGrid = photosBottomGrid;
            this.footer = footer;
            this.mainImages = mainImages;
            this.changesRows = changesRows;
            this.changesPage = changesPage;
            this.inspectionResponsibility = inspectionResponsibility;
            this.stageBuildingComponentsSubreport = stageBuildingComponentsSubreport;
            this.master = master;
        }
    }

    private List<Map<String, Object>> buildTableRows(ProjectStartReportDto dto) {
        List<TaskRow> tasks = dto.getTasks();
        if (tasks == null || tasks.isEmpty()) {
            // create default grouped rows so the checklist layout can be previewed clearly
            return java.util.List.of(
                    tableRow("متطلبات سور الحماية", "2", 1, "التأكد من وجود سور حماية مناسب حول الموقع", "☑", ""),
                    tableRow(null, "2", 2, "توفر وسائل التنبيه والتحذير حول منطقة العمل", "☐", "يلزم استكمال اللوحات التحذيرية"),
                    tableRow("متطلبات الأعمال الخرسانية", "3", 3, "مطابقة أعمال صب القواعد والميدات للمخططات المعتمدة", "NA", ""),
                    tableRow(null, "3", 4, "أخذ العينات اللازمة للفحص المختبري وتوثيق النتائج", "☑", "تم التوثيق")
            );
        }

        List<Map<String, Object>> rows = new ArrayList<>();
        String currentGroupTitle = "";
        String currentSideNumber = "";

        for (TaskRow r : tasks) {
            if (r.getGroupTitle() != null && !r.getGroupTitle().trim().isEmpty()) {
                currentGroupTitle = r.getGroupTitle().trim();
            }
            if (r.getSideNumber() != null && !r.getSideNumber().trim().isEmpty()) {
                currentSideNumber = r.getSideNumber().trim();
            }
            rows.add(tableRow(currentGroupTitle, currentSideNumber, r.getIndex(), r.getTaskName(), r.getCompliant(), r.getNotes()));
        }

        return rows;
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

    private String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (value != null && !value.trim().isEmpty()) {
                return value.trim();
            }
        }
        return null;
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

    private boolean resolveOtherBuildingTypeFlag(Boolean explicitValue, String legacyBuildingType, String otherBuildingType) {
        if (explicitValue != null) {
            return explicitValue;
        }
        return !safeText(otherBuildingType).trim().isEmpty()
                || resolveBuildingTypeFlag(null, legacyBuildingType, "أخرى", "other");
    }

    private String normalizeFlagText(String text) {
        return safeText(text).replace("-", "").replace(" ", "").trim().toLowerCase();
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

    private Map<String, Object> photoRow(String title, Object image) {
        Map<String, Object> row = new HashMap<>();
        row.put("title", title);
        row.put("image", image);
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
        dto.setInspectionNotes(java.util.List.of(
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
        while (notes.size() < 3) {
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
        return new JRBeanCollectionDataSource(java.util.List.of(row));
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
}
