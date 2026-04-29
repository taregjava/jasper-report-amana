package com.halfacode.japserreport.service.report;

import java.util.Map;

public interface StageReportDataProvider {
    Map<String, Object> getStaticData(StageReportProfile profile);
}
