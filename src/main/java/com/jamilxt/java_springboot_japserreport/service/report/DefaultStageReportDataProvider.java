package com.jamilxt.java_springboot_japserreport.service.report;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultStageReportDataProvider implements StageReportDataProvider {

    private static final int CHANGES_ROW_COUNT = 8;


    @Override
    public Map<String, Object> getStaticData(StageReportProfile profile) {

        Map<String, Object> params = new HashMap<>();

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

        // 👇 tables
        // Use ProjectStartReportService.buildTextRowsDataSource to wrap the list for JasperReports
        params.put("changesData",    buildTextRowsDataSource(java.util.List.of(
                "تعديل موقع النافذة في الواجهة الشمالية",
                "تغيير مقاس باب المدخل الرئيسي من 100 إلى 120 سم"
        )));

        return params;
    }

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