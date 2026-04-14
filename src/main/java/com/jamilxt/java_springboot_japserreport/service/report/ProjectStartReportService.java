package com.jamilxt.java_springboot_japserreport.service.report;

import com.jamilxt.java_springboot_japserreport.dto.ProjectStartReportDto;
import com.jamilxt.java_springboot_japserreport.dto.OwnerInfo;
import com.jamilxt.java_springboot_japserreport.dto.BuildingInfo;
import com.jamilxt.java_springboot_japserreport.dto.ProjectPhotosDTO;
import com.jamilxt.java_springboot_japserreport.dto.TaskRow;
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
        // Load and compile subreports + master
        try (InputStream headerIs = getClass().getResourceAsStream("/report/project_start_report_header.jrxml");
             InputStream ownerIs  = getClass().getResourceAsStream("/report/project_start_report_owner.jrxml");
             InputStream bodyIs   = getClass().getResourceAsStream("/report/project_start_report_body.jrxml");
             InputStream buildingIs = getClass().getResourceAsStream("/report/project_start_report_building.jrxml");
             InputStream tableIs  = getClass().getResourceAsStream("/report/project_start_report_table.jrxml");
             InputStream photosIs = getClass().getResourceAsStream("/report/project_start_report_photos.jrxml");
             InputStream masterIs = getClass().getResourceAsStream("/report/project_start_report_master.jrxml")) {

            JasperReport headerRep = JasperCompileManager.compileReport(headerIs);
            JasperReport ownerRep  = ownerIs == null ? null : JasperCompileManager.compileReport(ownerIs);
            JasperReport bodyRep   = JasperCompileManager.compileReport(bodyIs);
            JasperReport buildingRep = JasperCompileManager.compileReport(buildingIs);
            JasperReport tableRep  = JasperCompileManager.compileReport(tableIs);
            JasperReport photosRep = JasperCompileManager.compileReport(photosIs);
            JasperReport masterRep = JasperCompileManager.compileReport(masterIs);

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

            Map<String,Object> params = new HashMap<>();
            // pass compiled subreports
            params.put("headerSubreport", headerRep);
            params.put("ownerSubreport", ownerRep);
            params.put("bodySubreport", bodyRep);
            params.put("buildingSubreport", buildingRep);
            params.put("tableSubreport", tableRep);
            params.put("photosSubreport", photosRep);
            // pass table datasource
            params.put("tableData", tableDs);

            // header/body params (example)
            // load logo as byte[] so Jasper/iText can recognize the image format reliably during export
            try (InputStream logoIs = getClass().getResourceAsStream("/report/logo.png")) {
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
            params.put("contractorName", "شركة المقاول المحدودة");
            params.put("supervisingEngineeringOffice", "المكتب الهندسي النموذجي");
            params.put("buildingType", "سكني");
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
            params.put("officeName", "المكتب الهندسي النموذجي");
            params.put("digitalStampPath", resolveImageSource("classpath:report/logo.png"));

            JasperPrint jasperPrint = JasperFillManager.fillReport(masterRep, params, new net.sf.jasperreports.engine.JREmptyDataSource());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
            return baos.toByteArray();
        }
    }

    public byte[] generatePdf(ProjectStartReportDto dto) throws JRException {
        try (InputStream headerIs = resourceLoader.getResource("classpath:report/project_start_report_header.jrxml").getInputStream();
             InputStream ownerIs = resourceLoader.getResource("classpath:report/project_start_report_owner.jrxml").getInputStream();
             InputStream bodyIs = resourceLoader.getResource("classpath:report/project_start_report_body.jrxml").getInputStream();
             InputStream buildingIs = resourceLoader.getResource("classpath:report/project_start_report_building.jrxml").getInputStream();
             InputStream tableIs = resourceLoader.getResource("classpath:report/project_start_report_table.jrxml").getInputStream();
             InputStream photosIs = resourceLoader.getResource("classpath:report/project_start_report_photos.jrxml").getInputStream();
             InputStream masterIs = resourceLoader.getResource("classpath:report/project_start_report_master.jrxml").getInputStream()) {

            JasperReport headerRep = JasperCompileManager.compileReport(headerIs);
            JasperReport ownerRep = JasperCompileManager.compileReport(ownerIs);
            JasperReport bodyRep = JasperCompileManager.compileReport(bodyIs);
            JasperReport buildingRep = JasperCompileManager.compileReport(buildingIs);
            JasperReport tableRep = JasperCompileManager.compileReport(tableIs);
            JasperReport photosRep = JasperCompileManager.compileReport(photosIs);
            JasperReport masterReport = JasperCompileManager.compileReport(masterIs);

            List<Map<String, Object>> tableRows = buildTableRows(dto);

            Map<String, Object> params = new HashMap<>();
            // pass compiled subreports + table datasource expected by master
            params.put("headerSubreport", headerRep);
            params.put("ownerSubreport", ownerRep);
            params.put("bodySubreport", bodyRep);
            params.put("buildingSubreport", buildingRep);
            params.put("tableSubreport", tableRep);
            params.put("photosSubreport", photosRep);
            params.put("tableData", new JRBeanCollectionDataSource(tableRows));

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
            params.put("officeName", photos == null ? "" : photos.getOfficeName());
            params.put("digitalStampPath", photos == null ? null : resolveImageSource(photos.getDigitalStampPath()));

            // load logo resource into report param if available
            try (java.io.InputStream is = resourceLoader.getResource("classpath:report/logoSite.png").getInputStream()) {
                byte[] logo = is.readAllBytes();
                params.put("reportLogo", logo);
            } catch (Exception ignore) {
                // ignore missing logo
            }

            // Master contains nested subreports and consumes `tableData` directly.
            JasperPrint print = JasperFillManager.fillReport(masterReport, params, new net.sf.jasperreports.engine.JREmptyDataSource());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(print, out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new JRException("Failed to generate project start report", e);
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
        // Use field names expected by the table subreport/master: itemNo and description
        row.put("groupTitle", groupTitle == null ? "" : groupTitle);
        row.put("sideNumber", sideNumber == null ? "" : sideNumber);
        row.put("itemNo", index);
        row.put("description", task);
        row.put("status", status);
        row.put("notes", notes == null ? "" : notes);
        return row;
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
}
