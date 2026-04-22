package com.jamilxt.java_springboot_japserreport.service.report;

public enum StageReportProfile {
    PROJECT_START("project start", "تقرير رقم 1", "تقرير مرحلة بدء المشروع"),
    POST_FOUNDATION("post-foundation casting stage", "تقرير رقم 1", "تقرير مرحلة ما بعد صب الأساسات"),
    PRE_FOUNDATION("pre-foundation stage", "تقرير رقم 1", "تقرير مرحلة ما قبل صب الأساسات");

    private final String displayName;
    private final String reportNumberLabel;
    private final String stageTitle;

    StageReportProfile(String displayName, String reportNumberLabel, String stageTitle) {
        this.displayName = displayName;
        this.reportNumberLabel = reportNumberLabel;
        this.stageTitle = stageTitle;
    }

    public String displayName() {
        return displayName;
    }

    public String reportNumberLabel() {
        return reportNumberLabel;
    }

    public String stageTitle() {
        return stageTitle;
    }

    public String reportPath(String suffix) {
        return "classpath:report/stageShared/stage_" + suffix + ".jrxml";
    }
}

