package com.jamilxt.java_springboot_japserreport.service.report;

public enum StageReportProfile {
    PROJECT_START("project start", "تقرير رقم 1", "تقرير مرحلة بدء المشروع", "stageShared"),
    POST_FOUNDATION("post-foundation casting stage", "تقرير رقم 1", "تقرير مرحلة ما بعد صب الأساسات", "postFoundation"),
    PRE_FOUNDATION("pre-foundation stage", "تقرير رقم 1", "تقرير مرحلة ما قبل صب الأساسات", "preFoundation"),

    PRE_POURING("pre-pouring stage", "تقرير رقم 1", "تقرير مرحلة ما قبل صب الميدات", "prePouring"),//prePouring


    PRO_TIP("pro tip stage", "تقرير رقم 1", "تقرير مرحلة ما قبل صب الأعمدة", "proTip");
    private final String displayName;
    private final String reportNumberLabel;
    private final String stageTitle;
    private final String templateFolder;

    StageReportProfile(String displayName, String reportNumberLabel, String stageTitle, String templateFolder) {
        this.displayName = displayName;
        this.reportNumberLabel = reportNumberLabel;
        this.stageTitle = stageTitle;
        this.templateFolder = templateFolder;
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

    public String templateFolder() {
        return templateFolder;
    }

    public String reportPath(String suffix) {
        return "classpath:report/" + templateFolder + "/stage_" + suffix + ".jrxml";
    }
}

