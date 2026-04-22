package com.jamilxt.java_springboot_japserreport.service.report;

public enum StageReportProfile {
    PROJECT_START("projectStart", "project_start", "project start"),
    POST_FOUNDATION("postFoundation", "post-foundation", "post-foundation casting stage");

    private final String folderName;
    private final String filePrefix;
    private final String displayName;

    StageReportProfile(String folderName, String filePrefix, String displayName) {
        this.folderName = folderName;
        this.filePrefix = filePrefix;
        this.displayName = displayName;
    }

    public String folderName() {
        return folderName;
    }

    public String filePrefix() {
        return filePrefix;
    }

    public String displayName() {
        return displayName;
    }

    public String reportPath(String suffix) {
        return "classpath:report/" + folderName + "/" + filePrefix + "_" + suffix + ".jrxml";
    }
}

