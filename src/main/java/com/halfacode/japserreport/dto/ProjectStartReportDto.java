package com.halfacode.japserreport.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectStartReportDto {

    private OwnerInfo ownerInfo;            // بيانات الملكية
    private BuildingInfo buildingInfo;      // معلومات المبنى

    private String engineerName;            // اسم المهندس المشرف
    private String contractorMobile;        // رقم جوال المقاول
    private String stageResult;             // نتيجة فحص المرحلة (مقبول – مرفوض – ملاحظات)
    private Boolean stageInspectionAccepted; // اختيار: مقبول
    private Boolean stageInspectionRejected; // اختيار: مرفوض
    private Boolean stageInspectionHasNotes; // اختيار: ملاحظات

    private List<TaskRow> tasks;            // جدول المهام
    private ProjectPhotosDTO projectPhotos; // صور المبنى
    private InspectionResponsibilityDTO inspectionResponsibility; // إقرار المسؤولية واعتماد جهة التفتيش

    private List<String> changes;           // التغييرات على المخططات المعتمدة (max 8 items)
    private List<String> extraItems;        // البنود التي لم تذكر في التقرير (max 8 items)

    // Getters & Setters
}