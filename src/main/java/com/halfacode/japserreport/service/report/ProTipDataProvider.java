package com.halfacode.japserreport.service.report;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProTipDataProvider implements StageReportDataProvider {

    @Override
    public Map<String, Object> getStaticData(StageReportProfile profile) {

        Map<String, Object> params = new HashMap<>();

        // 🔥 ProTip-specific data
        params.put("stageReportTitle", "Pro Tip Report");
        params.put("stageInspectionResult", "Advisory");

//        // ✅ IMPORTANT: wrap with JRBeanCollectionDataSource
//        params.put("changesData",
//                new JRBeanCollectionDataSource(
//                        List.of(
//                                new TextRow("يفضل استخدام مواد عزل عالية الجودة"),
//                                new TextRow("التأكد من تصريف المياه بشكل صحيح")
//                        )
//                )
//        );
//
//        params.put("extraItemsData",
//                new JRBeanCollectionDataSource(
//                        List.of(
//                                new TextRow("مراجعة المخططات قبل التنفيذ")
//                        )
//                )
//        );

        return params;
    }
}