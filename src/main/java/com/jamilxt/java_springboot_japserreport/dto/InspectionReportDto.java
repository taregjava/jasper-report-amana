package com.jamilxt.java_springboot_japserreport.dto;

import java.util.List;

public class InspectionReportDto {

    private String reportTitle;
    private String ownerName;
    private String buildingAddress;
    private String inspectionDate;
    // use existing InspectionItemDto from the project
    private List<InspectionItemDto> checklist;
    // use existing ImageDto
    private List<ImageDto> images;
    private String checklistText; // simple combined checklist block for JRXML demo

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getBuildingAddress() {
        return buildingAddress;
    }

    public void setBuildingAddress(String buildingAddress) {
        this.buildingAddress = buildingAddress;
    }

    public String getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(String inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public List<InspectionItemDto> getChecklist() {
        return checklist;
    }

    public void setChecklist(List<InspectionItemDto> checklist) {
        this.checklist = checklist;
    }

    public List<ImageDto> getImages() {
        return images;
    }

    public void setImages(List<ImageDto> images) {
        this.images = images;
    }

    public String getChecklistText() {
        return checklistText;
    }

    public void setChecklistText(String checklistText) {
        this.checklistText = checklistText;
    }
}
