package com.jamilxt.java_springboot_japserreport.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectStartReportDto {

    private OwnerInfo ownerInfo;            // بيانات الملكية
    private BuildingInfo buildingInfo;      // معلومات المبنى

    private String engineerName;            // اسم المهندس المشرف
    private String contractorMobile;        // رقم جوال المقاول
    private String stageResult;             // نتيجة فحص المرحلة (مقبول – مرفوض – ملاحظات)

    private List<TaskRow> tasks;            // جدول المهام
    private ProjectPhotosDTO projectPhotos; // صور المبنى

    // Getters & Setters
}